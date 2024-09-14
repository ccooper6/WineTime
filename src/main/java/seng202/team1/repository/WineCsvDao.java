package seng202.team1.repository;

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
 * The class that handles populating the og.db.
 * Written by Wen Sheng Thong wst44, despite IntelliJ saying its written by
 * Yuhao Zhang.
 */
public class WineCsvDao {


    private static final Logger log = LogManager.getLogger(WineCsvDao.class);
    /**
     * Adds a wine into the database as well as its associated tags if there are any new tags. It then links
     * the wine to the tag using the owned_by relationship. Returns the next wine numeric id upon successful addition.
     * @param wineValues a string array of wine attributes from the wine csv file
     * @param wineID an integer num id for the added wine
     * @return the next wine id to be used
     */

    public int add(String[] wineValues, int wineID) {
        String wineSql = "INSERT INTO wine (id, name, price, description, points, normalised_name) VALUES (?, ?, ?, ?, ?, ?)";
        String tagSql = "INSERT INTO tag (name, type, normalised_name) VALUES (?, ?, ?)";
        String ownedBySql = "INSERT INTO owned_by (wid, tname) VALUES (?, ?)";
        try (Connection conn = emptyConnect()) {
            try (PreparedStatement winePs = conn.prepareStatement(wineSql)) {
                executeWinePs(winePs, wineValues, wineID);
            }
            try (PreparedStatement tagPs = conn.prepareStatement(tagSql)) {
                try (PreparedStatement ownedByPs = conn.prepareStatement(ownedBySql)) {
                    handleTagAdding(wineValues, wineID, tagPs, ownedByPs);
                }
            }
        } catch (SQLException e) {
            if (e.getErrorCode() == 19) {
                log.info("DUPLICATE WINE FOUND AT ROW " + wineValues[0]);
            } else {
                log.error(e.getMessage());
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
     */

    private void handleTagAdding(String[] wineValues, int wineID, PreparedStatement tagPs, PreparedStatement ownedByPs) {
        int vintage;
        if ((vintage = extractVintage(wineValues[10])) != 0) {
            executeTagPs(tagPs, Integer.toString(vintage), ownedByPs, "Vintage", wineID);
        }
        if (!wineValues[12].isEmpty()) {
            executeTagPs(tagPs, wineValues[12], ownedByPs, "Winery", wineID);
        }
        if (!wineValues[1].isEmpty()) {
            executeTagPs(tagPs, wineValues[1], ownedByPs, "Country", wineID);
        }
        if (!wineValues[11].isEmpty()) {
            executeTagPs(tagPs, wineValues[11], ownedByPs, "Variety", wineID);
        }
        if (!wineValues[6].isEmpty()) {
            executeTagPs(tagPs, wineValues[6], ownedByPs, "Region", wineID);
        }
        if (!wineValues[7].isEmpty()) {
            executeTagPs(tagPs, wineValues[7], ownedByPs, "Region", wineID);
        }
        if (!wineValues[5].isEmpty()) {
            executeTagPs(tagPs, wineValues[5], ownedByPs, "Province", wineID);
        }
    }

    /**
     * Execute the wine prepared statement. Returns the id number of the wine added
     * @param winePs The wine {@link PreparedStatement}
     * @param wineValues A string array of wine attributes obtained from the csv file
     * @param wineID the numeric int id for the wine
     * @throws SQLException handled by method caller
     */

    public void executeWinePs(PreparedStatement winePs, String[] wineValues, int wineID) throws SQLException {
        winePs.setInt(1, wineID);
        //adds wine name
        winePs.setString(2, wineValues[10]);
        //adds wine price
        if (!wineValues[4].isEmpty()) {
            winePs.setInt(3, Integer.parseInt(wineValues[4]) );
        } else {
            winePs.setNull(3, Types.INTEGER);
        }
        //adds wine description
        winePs.setString(4, wineValues[13]);
        //adds wine points
        if (!wineValues[4].isEmpty()) {
            winePs.setInt(5, Integer.parseInt(wineValues[3]) );
        } else {
            winePs.setNull(5, Types.INTEGER);
        }

        winePs.setString(6, Normalizer.normalize(wineValues[10], Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "").toLowerCase());
        winePs.executeUpdate();
    }

    /**
     * Executes the variety tag prepared statement.
     * @param tagPs the tag's {@link PreparedStatement}
     * @param tagName A string of the tagName
     * @param ownedByPs the {@link PreparedStatement} for an owned_by relationship
     * @param tagType the type of tag being added to the tag database.
     */

    public void executeTagPs(PreparedStatement tagPs, String tagName, PreparedStatement ownedByPs, String tagType, int wineId) {
        try {
            if (!tagName.isEmpty()) {
                tagPs.setString(1, tagName);
                tagPs.setString(2, tagType);
                tagPs.setString(3, Normalizer.normalize(tagName, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "").toLowerCase());
                tagPs.executeUpdate();
                executeOwnedByPs(ownedByPs, wineId, tagName);
//                System.out.println("Successfully added " + tagType +tagName);
            }
        } catch (SQLException e) {
            if (e.getErrorCode() == 19) {
                //tag already exists, link via owned_by anyway.
                executeOwnedByPs(ownedByPs, wineId, tagName);
            } else {
                log.error(e.getMessage());
            }
        }
    }

    /**
     * Links the inputted wineId entity to the tagName entity via the owned_by relationship
     * @param ownedByPs the {@link PreparedStatement} for the owned_by relationship
     * @param wineId the wine integer id
     * @param tagName the string tag name
     */

    public void executeOwnedByPs(PreparedStatement ownedByPs, int wineId, String tagName) {
        try {
            ownedByPs.setInt(1, wineId);
            ownedByPs.setString(2, tagName);
            ownedByPs.executeUpdate();
        } catch (SQLException e) {
            if (e.getErrorCode() != 19) {
                log.info(e.getMessage());
            }
        }
    }

    /**
     * Extracts the wine's vintage from the inserted wine's name.
     * Returns the vintage year if found, if no year is found in the name, return a 0.
     * @param wineName the wineName String
     * @return the vintage year if found, returns a 0 otherwise.
     */

    public int extractVintage(String wineName) {
        StringBuilder vintageString = new StringBuilder();
        boolean notFoundVintage = true;
        boolean typingVintage = false;
        CharacterIterator iterator = new StringCharacterIterator(wineName);
        while (notFoundVintage && iterator.current() != CharacterIterator.DONE) {
            char currentChar = iterator.current();
            if (Character.isDigit(currentChar)) {
                typingVintage = true;
                vintageString.append(currentChar);
            } else if (typingVintage) {
                typingVintage = false;
                notFoundVintage = false;
            }
            iterator.next();
        }
        if (!vintageString.isEmpty()) {
            int vintage = Integer.parseInt(vintageString.toString());
            if (vintage >= 1800 && vintage <= 2024) {
                return vintage;
            }
        }
        return 0;
    }

    private int getLines(String filename) throws IOException
    {
        BufferedReader bufferedReader = new BufferedReader(new FileReader(filename));
        int count = 0;
        while((bufferedReader.readLine()) != null)
        {
            count++;
        }

        return count;
    }

    /**
     * Reads through each line of the 130k wine csv file and processes them. Takes in the csv file location as inputs as
     * well as a designated output ArrayList for any wine row that is erroneous.
     * @param winePath The url of the 130k wine csv.
     */

    public void wineCsvReader(String winePath) throws IOException, CsvValidationException {
        int totalRows = getLines(winePath);

        String[] wineValues;
        CSVReader csv = new CSVReaderBuilder(new InputStreamReader(new FileInputStream(winePath), StandardCharsets.UTF_8)).withSkipLines(1).build();
        int wineID = 1;
        int num = -1;
        while ((wineValues = csv.readNext()) != null) {
            wineID = add(wineValues, wineID);
            System.out.println(wineID + "/" + totalRows + ":" + wineValues[11]);
            if (wineID > num + 1000) {
                System.out.println(wineID + "/" + totalRows);
                num += 1000;
            }
        }
        csv.close();
    }
    /**
     * Connects to the empty database
     * @return database connection
     */

    public Connection emptyConnect() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection("jdbc:sqlite:src/main/resources/sql/og.db");
        } catch (SQLException e) {
            log.error("Failed to connect to original database");
        }
        return conn;
    }

    /**
     * Called upon first run of jar, fills the database created by {@link DatabaseManager} with the initial
     * 130k wines.
     */
    public void initializeAllWines() {
        String path = WineCsvDao.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        path = URLDecoder.decode(path, StandardCharsets.UTF_8);
        File jarDir = new File(path);

        String buildPath = String.valueOf(jarDir.getParentFile());
        buildPath = buildPath.substring(0, buildPath.length() - 12);
        String wineFilePath = buildPath + "resources/main/csvFiles/Wine130kNoFancyCharUTF8.csv";
        //noinspection TryWithIdenticalCatches
        try {
            wineCsvReader(wineFilePath);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (CsvValidationException e) {
            log.error("failed to read csv file");
            throw new RuntimeException(e);
        }
    }

    /**
     * Calls initializeAllWines()
     * @param args System terminal arguments
     */

    public static void main(String[] args) {
        WineCsvDao wine = new WineCsvDao();
        wine.initializeAllWines();
    }
}
