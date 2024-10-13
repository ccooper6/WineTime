package seng202.team1.repository.DAOs;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvValidationException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.text.CharacterIterator;
import java.text.Normalizer;
import java.text.StringCharacterIterator;

/**
 * The class that handles populating the main.db.
 */
public class WineCsvDao {


    private static final Logger LOG = LogManager.getLogger(WineCsvDao.class);

    /**
     * Adds a wine into the database as well as its associated tags if there are any new tags. It then links
     * the wine to the tag using the owned_by relationship. Returns the next wine numeric id upon successful addition.
     * @param wineValues a string array of wine attributes from the wine csv file
     * @param wineID an integer num id for the added wine
     * @return the next wine id to be used
     */
    public int add(String[] wineValues, int wineID) {
        String wineSql = "INSERT INTO wine (id, name, price, description, points, vintage, normalised_name) VALUES (?, ?, ?, ?, ?, ?, ?)";
        String tagSql = "INSERT INTO tag (name, type, normalised_name) VALUES (?, ?, ?)";
        String ownedBySql = "INSERT INTO owned_by (wid, tname) VALUES (?, ?)";
        try (Connection conn = connectToMainDB()) {
            int vintage = extractVintage(wineValues[11]);
            try (PreparedStatement winePs = conn.prepareStatement(wineSql)) {
                executeWinePs(winePs, wineValues, wineID, vintage);
            }
            try (PreparedStatement tagPs = conn.prepareStatement(tagSql)) {
                try (PreparedStatement ownedByPs = conn.prepareStatement(ownedBySql)) {
                    handleTagAdding(wineValues, wineID, tagPs, ownedByPs, vintage);
                }
            }
        } catch (SQLException e) {
            // if not duplicate wine
            if (e.getErrorCode() != 19) {
                LOG.error("Error in WineCsvDao.add(): SQLException: {}", e.getMessage());
            }
            return wineID; //wine not added, reuse unused wineID
        }
        return wineID + 1;
    }

    /**
     * Handles the addition of all the tags for a specified wine based off their wine id.
     * @param wineValues a String array of wine attributes
     * @param wineID a wine ID integer
     * @param tagPs the tag's {@link PreparedStatement}
     * @param ownedByPs the ownedBy relationship {@link PreparedStatement}
     * @param vintage the vintage of the wine.
     */
    private void handleTagAdding(String[] wineValues, int wineID, PreparedStatement tagPs, PreparedStatement ownedByPs, int vintage) {
        if (vintage != 0) {
            executeTagPs(tagPs, Integer.toString(vintage), ownedByPs, "Vintage", wineID);
        } else {
            executeTagPs(tagPs, null, ownedByPs, "Vintage", wineID);
        }
        if (!wineValues[13].isEmpty()) {
            executeTagPs(tagPs, wineValues[13], ownedByPs, "Winery", wineID);
        }
        if (!wineValues[1].isEmpty()) {
            executeTagPs(tagPs, wineValues[1], ownedByPs, "Country", wineID);
        }
        if (!wineValues[11].isEmpty()) {
            executeTagPs(tagPs, wineValues[12], ownedByPs, "Variety", wineID);
        }
        if (!wineValues[6].isEmpty()) {
            executeTagPs(tagPs, wineValues[7], ownedByPs, "Region", wineID);
        }
        if (!wineValues[7].isEmpty()) {
            executeTagPs(tagPs, wineValues[8], ownedByPs, "Region", wineID);
        }
        if (!wineValues[5].isEmpty()) {
            executeTagPs(tagPs, wineValues[6], ownedByPs, "Province", wineID);
        }
    }

    /**
     * Execute the wine prepared statement. Returns the id number of the wine added
     * @param winePs The wine {@link PreparedStatement}
     * @param wineValues A string array of wine attributes obtained from the csv file
     * @param wineID the numeric int id for the wine
     * @param vintage the vintage of the wine.
     * @throws SQLException handled by method caller
     */
    public void executeWinePs(PreparedStatement winePs, String[] wineValues, int wineID, int vintage) throws SQLException {
        winePs.setInt(1, wineID);
        //adds wine name
        winePs.setString(2, wineValues[11]);
        //adds wine price
        if (!wineValues[5].isEmpty()) {
            winePs.setInt(3, Integer.parseInt(wineValues[5]) );
        } else {
            winePs.setNull(3, Types.INTEGER);
        }
        //adds wine description
        winePs.setString(4, wineValues[2]);
        //adds wine points
        if (!wineValues[4].isEmpty()) {
            winePs.setInt(5, Integer.parseInt(wineValues[4]) );
        } else {
            winePs.setNull(5, Types.INTEGER);
        }
        if (vintage != 0) {
            winePs.setInt(6, vintage);
        } else {
            winePs.setNull(6, Types.INTEGER);
        }

        winePs.setString(7, Normalizer.normalize(wineValues[11], Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "").toLowerCase());
        winePs.executeUpdate();
    }

    /**
     * Executes the variety tag prepared statement.
     * @param tagPs the tag's {@link PreparedStatement}
     * @param tagName A string of the tagName
     * @param ownedByPs the {@link PreparedStatement} for an owned_by relationship
     * @param tagType the type of tag being added to the tag database.
     * @param wineId the wine integer id
     */
    private void executeTagPs(PreparedStatement tagPs, String tagName, PreparedStatement ownedByPs, String tagType, int wineId) {
        try {
            if (tagName == null || tagName.isEmpty()) {
                tagPs.setNull(1, Types.VARCHAR);
                tagPs.setString(2, tagType);
                tagPs.setNull(3, Types.VARCHAR);
                tagPs.executeUpdate();
                executeOwnedByPs(ownedByPs, wineId, tagName);
            } else {
                tagPs.setString(1, tagName);
                tagPs.setString(2, tagType);
                tagPs.setString(3, Normalizer.normalize(tagName, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "").toLowerCase());
                tagPs.executeUpdate();
                executeOwnedByPs(ownedByPs, wineId, tagName);
            }
        } catch (SQLException e) {
            if (e.getErrorCode() == 19) {
                //tag already exists, link via owned_by anyway.
                executeOwnedByPs(ownedByPs, wineId, tagName);
            } else {
                LOG.error("Error in WineCsvDao.executeTagPs(): SQLException: {}", e.getMessage());
            }
        }
    }

    /**
     * Links the inputted wineId entity to the tagName entity via the owned_by relationship.
     * @param ownedByPs the {@link PreparedStatement} for the owned_by relationship
     * @param wineId the wine integer id
     * @param tagName the string tag name
     */

    private void executeOwnedByPs(PreparedStatement ownedByPs, int wineId, String tagName) {
        try {
            if (tagName == null || tagName.isEmpty()) {
                ownedByPs.setNull(1, Types.VARCHAR);
            } else {
                ownedByPs.setInt(1, wineId);
            }
            ownedByPs.setString(2, tagName);
            ownedByPs.executeUpdate();
        } catch (SQLException e) {
            // not a duplicate
            if (e.getErrorCode() != 19) {
                LOG.info(e.getMessage());
            }
        }
    }

    /**
     * Extracts the wine's vintage from the inserted wine's name.
     * Returns the vintage year if found, if no year is found in the name, return a 0.
     * @param wineName the wineName String
     * @return the vintage year if found, returns a 0 otherwise.
     */
    private int extractVintage(String wineName) {
        int vintage = 0;
        int numberOfVintagesFound = 0;
        StringBuilder vintageString = new StringBuilder();
        boolean isNegative = false;
        boolean typingVintage = false;
        CharacterIterator iterator = new StringCharacterIterator(wineName);
        try {
            while (numberOfVintagesFound <= 1 && iterator.current() != CharacterIterator.DONE) {
                char currentChar = iterator.current();
                if (currentChar == '-') {
                    isNegative = true;
                }
                if (Character.isDigit(currentChar) && !isNegative) {
                    typingVintage = true;
                    vintageString.append(currentChar);
                } else {
                    if (!Character.isDigit(currentChar) && currentChar != '-') {
                        isNegative = false;
                    }
                    if (typingVintage) {
                        typingVintage = false;
                        //check if the found number is valid, if yes keep track of it and keep iterating, else continue lmao
                        if (Integer.parseInt(vintageString.toString()) >= 1800 && Integer.parseInt(vintageString.toString()) <= 2024) {
                            numberOfVintagesFound++;
                            vintage = Integer.parseInt(vintageString.toString());
                            vintageString.setLength(0);
                        } else {
                            vintageString.setLength(0);
                        }
                    }
                }
                iterator.next();
            }
        } finally {
            if (!vintageString.isEmpty()) {
                if (Integer.parseInt(vintageString.toString()) >= 1800 && Integer.parseInt(vintageString.toString()) <= 2024) {
                    numberOfVintagesFound++;
                    vintage = Integer.parseInt(vintageString.toString());
                }
            }
        }
        if (numberOfVintagesFound == 1) {
            return vintage;
        } else if (numberOfVintagesFound > 1){
            LOG.info("Wine {} has {} potential vintage, please manually review", wineName, numberOfVintagesFound);
        }
        return 0;
    }

    /**
     * Gets the number of lines in a file.
     * @param filename the file name
     * @return the number of lines in the file
     * @throws IOException if the file is not found
     */
    private int getLines(String filename) throws IOException
    {
        BufferedReader bufferedReader = new BufferedReader(new FileReader(filename));
        int count = 0;
        while ((bufferedReader.readLine()) != null)
        {
            count++;
        }

        return count;
    }

    /**
     * Reads through each line of the 130k wine csv file and processes them. Takes in the csv file location as inputs as
     * well as a designated output ArrayList for any wine row that is erroneous.
     * @param winePath The url of the 130k wine csv.
     * @throws IOException Throws when something goes wrong with the input/output
     * @throws CsvValidationException Throws when there is an error validating the CSV
     */

    private void wineCsvReader(String winePath) throws IOException, CsvValidationException {
        int totalRows = getLines(winePath);

        String[] wineValues;
        CSVReader csv = new CSVReaderBuilder(new InputStreamReader(new FileInputStream(winePath), StandardCharsets.UTF_8)).withSkipLines(1).build();
        int wineID = 1;

        while ((wineValues = csv.readNext()) != null) {
            wineID = add(wineValues, wineID);
            // Display how many wines have been added / total wines to user on terminal
            System.out.println(wineID + "/" + totalRows + ":" + wineValues[11]);
        }
        csv.close();
    }

    /**
     * Connects to main database. Database must be empty.
     * @return database connection
     */
    private Connection connectToMainDB() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection("jdbc:sqlite:src/main/resources/sql/main.db");
        } catch (SQLException e) {
            LOG.error("Failed to connect to original database");
        }
        return conn;
    }

    /**
     * Called upon first run of jar, fills the database created by DatabaseManager with the initial
     * 130k wines.
     */
    public void initializeAllWines() {
        String path = WineCsvDao.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        path = URLDecoder.decode(path, StandardCharsets.UTF_8);
        File jarDir = new File(path);

        String buildPath = String.valueOf(jarDir.getParentFile());
        buildPath = buildPath.substring(0, buildPath.length() - 12);
        String wineFilePath = buildPath + "resources/main/csvFiles/WinesFixedv3.csv";
        //noinspection TryWithIdenticalCatches
        try {
            wineCsvReader(wineFilePath);
        } catch (FileNotFoundException e) {
            LOG.error("Error in WineCsvDao.initialiseAllWines(): FileNotFoundException: {}", e.getMessage());
            throw new RuntimeException(e);
        } catch (IOException e) {
            LOG.error("Error in WineCsvDao.initialiseAllWines(): IOException: {}", e.getMessage());
            throw new RuntimeException(e);
        } catch (CsvValidationException e) {
            LOG.error("Failed to read csv file");
            throw new RuntimeException(e);
        }
    }

    /**
     * Calls initializeAllWines().
     * @param args System terminal arguments
     */

    public static void main(String[] args) {
        WineCsvDao wine = new WineCsvDao();
        wine.initializeAllWines();
    }
}
