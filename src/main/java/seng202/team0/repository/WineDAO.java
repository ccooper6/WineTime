package seng202.team0.repository;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import seng202.team0.exceptions.DuplicateEntryException;
import seng202.team0.exceptions.InvalidWineException;
import seng202.team0.models.Wine;

import javax.swing.plaf.nimbus.State;
import javax.lang.model.type.ArrayType;
import javax.swing.plaf.nimbus.State;
import java.io.*;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.ArrayList;
import java.util.List;
import java.util.*;

public class WineDAO implements DAOInterface<Wine> {

    private final DatabaseManager databaseManager;

    private static final Logger log = LogManager.getLogger(UserDAO.class);

    Set<String> white = new HashSet<>(Arrays.asList("White Blend", "Pinot Gris", "Riesling", "Gewürztraminer", "Chardonnay", "Chenin Blanc", "Sauvignon Blanc", "Viognier-Chardonnay",
                                                    "Catarratto", "Inzolia", "Bordeaux-style White Blend", "Grillo", "Petit Manseng", "Vernaccia", "Grüner Veltliner", "Viognier",
                                                    "Vermentino", "Grenache Blanc", "Pinot Blanc", "Alsace white blend", "Portuguese White", "Sauvignon", "Torrontés", "Verdejo",
                                                    "Fumé Blanc", "Furmint", "Pinot Bianco", "Ugni Blanc-Colombard", "Friulano", "Assyrtico", "Savagnin", "Vignoles", "Muscat",
                                                    "Muscadelle", "Garganega", "Pinot Grigio", "Zierfandler", "Cortese", "Melon", "Rhône-style White Blend", "Vidal", "Verdelho",
                                                    "Mersanne", "Scheurebe", "Kerner", "Vilana", "Glera", "Viura", "Roter Veltliner", "Sémillon", "Antão Vaz", "Verdejo-Viura",
                                                    "Verduzzo", "Verdicchio", "Silvaner", "Colombard", "Carricante", "Sylvaner", "Fiano", "Roussanne", "Avesso", "Chinuri",
                                                    "Muscat Blanc à Petits Grains", "Xarel-lo", "Greco", "Trebbiano", "Chenin Blanc-Chardonnay", "Insolia", "Ribolla Gialla",
                                                    "Weissburgunder", "Roditis", "Traminer", "Marsanne-Roussanne", "Prié Blanc", "Zibibbo", "Falanghina", "Müller-Thurgau",
                                                    "Pinot Meunier", "Pansa Blanca", "Muskat Ottonel", "Sauvignon Blanc-Semillon", "Semillon-Sauvignon Blanc", "Bical", "Moscatel",
                                                    "Viura-Chardonnay", "Malvasia Bianca", "Gelber Muskateller", "Albariño", "Marsanne"));
    Set<String> red = new HashSet<>(Arrays.asList("Portuguese Red", "Pinot Noir", "Tempranillo-Merlot", "Frappato", "Cabernet Sauvignon", "Nerello Mascalese", "Malbec", "Tempranillo Blend",
                                                    "Meritage", "Red Blend", "Merlot", "Nero d'Avola", "Gamay", "Primitivo", "Petit Verdot", "Monica", "Gangiovese", "Cabernet Franc",
                                                    "Bordeaux-style Red Blend", "Aglianico", "Zinfandel", "Syrah", "Nebbiolo", "Shiraz-Cabernet Sauvignon", "Barbera", "Rhône-style Red Blend",
                                                    "Graciano", "Tannat-Cabernet", "Sangiovese Grosso", "Prugnolo Gentile","G-S-M", "Bonarda", "Shiraz", "Montepulciano", "Grenache",
                                                    "Syrah-Viognier", "Blaufränkisch", "Carignan-Grenache", "Sagrantino", "Cabernet Sauvignon-Syrah", "Shiraz-Viognier", "Tempranillo",
                                                    "Mencía", "Zweigelt", "Cannonau", "Syrah-Grenache", "Dolcetto", "Garnacha Tintorera", "Pinot Nero", "Pinotage", "Pinot Noir-Gamay",
                                                    "Cabernet Sauvignon-Carmenère", "Früburgunder", "Sousão", "Cinsault", "Tinta Miúda", "Monastrell", "Port", "Merlot-Malbec",
                                                    "Cabernet Sauvignon-Merlot", "Duras", "Papaskarasi", "Tannat-Syrah", "Charbono", "Merlot-Argaman", "Provence red blend", "Tannat",
                                                    "Garnacha", "Negroamaro", "Mourvèdre", "Syrah-Cabernet", "Cabernet Sauvignon-Sangiovese", "Austrian Red Blend", "Teroldego",
                                                    "Claret", "Baga", "Malbec-Merlot", "Monastrell-Syrah", "Malbec-Tannat", "Malbec-Cabernet Franc", "Petite Sirah", "Sangiovese",
                                                    "Touriga Nacional", "Carmenère"));
    Set<String> rose = new HashSet<>(Arrays.asList("Rosé", "Rosato", "Moscato", "Sherry", "Rosado"));
    Set<String> sparkling = new HashSet<>(Arrays.asList("Champagne Blend", "Prosecco", "Sparkling Blend", "Portuguese Sparkling"));

    /**
     * Getter method for the set of white tags
     * @return set of white tags
     */
    public Set<String> getWhite() {
        return white;
    }
    /**
     * Getter method for the set of red tags
     * @return set of red tags
     */
    public Set<String> getRed() {
        return red;
    }
    /**
     * Getter method for the set of rose tags
     * @return set of rose tags
     */
    public Set<String> getRose() {
        return rose;
    }
    /**
     * Getter method for the set of sparkling tags
     * @return set of sparkling tags
     */
    public Set<String> getSparkling() {
        return sparkling;
    }

    public WineDAO() {
        databaseManager = DatabaseManager.getInstance();

    }
    @Override
    public List getAll() {
        return null;
    }

    /**
     * This method get a wine based on it's id
     * @param id id of object to get
     * @return
     */
    @Override
    public Wine getOne(int id) {

        Wine wine = null;
        String sql = "SELECT * FROM wine WHERE id= ?";
        try (Connection conn = databaseManager.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    wine = new Wine(
                            rs.getString("name"),
                            rs.getString("description"),
                            rs.getInt("price"),
                            rs.getInt("vintage"),
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            ""
                    );
                    return wine;
                }
            }
            return null;
        } catch (SQLException e) {
            log.error(e.getMessage());
            return null;
        }

    }

    /**
     * This method take in two years and return all wines between those two years.
     * @param earliest the earliest vintage
     * @param latest the latest vintage
     * @return an arraylist of all wines between the two vintages.
     */
    public ArrayList<Wine> getWinesFromVintage(int earliest, int latest) {

        ArrayList<Wine> wines = new ArrayList<>();
        Wine wine = null;
        String sql = "SELECT * FROM wine WHERE vintage>=? and vintage<=?";
        try (Connection conn = databaseManager.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, earliest);
            ps.setInt(2, latest);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    wine = new Wine(
                            rs.getString("name"),
                            rs.getString("description"),
                            rs.getInt("price"),
                            rs.getInt("vintage"),
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            ""
                    );
                    wines.add(wine);
                }
                return wines;
            }

        } catch (SQLException e) {
            log.error(e.getMessage());
            return null;
        }

    }
    public ArrayList<String> getVarietyTags() {
        ArrayList<String> tags = new ArrayList<>();
        String sql = "SELECT name FROM tag";
        try (Connection conn = databaseManager.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    tags.add(rs.getString("name"));
                }
                return tags;
            }
        } catch (SQLException e) {
            log.error(e.getMessage());
            return null;
        }
    }

        Wine wine = null;
        String sql = "SELECT * FROM wine WHERE id= ?";
        try (Connection conn = databaseManager.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    wine = new Wine(
                            rs.getString("name"),
                            rs.getString("description"),
                            rs.getInt("price"),
                            rs.getInt("vintage"),
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            ""
                    );
                    return wine;
                }
            }
            return null;
        } catch (SQLException e) {
            log.error(e.getMessage());
            return null;
        }

    }

    public List<Wine> findWinesByName(String wineName) {
        List<Wine> wines = new ArrayList<>();
        String query = "SELECT * FROM wine WHERE name LIKE ?";
        try (Connection conn = databaseManager.connect()) {
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, "%" + wineName + "%");
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    Wine wine = new Wine();
                    wine.setName(rs.getString("name"));
                    wine.setVintage(rs.getInt("vintage"));
                    wine.setPrice(rs.getInt("price"));
                    wine.setDescription(rs.getString("description"));

                    wines.add(wine);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return wines;
    }


    /**
     * This method take in two years and return all wines between those two years.
     * @param earliest the earliest vintage
     * @param latest the latest vintage
     * @return an arraylist of all wines between the two vintages.
     */
    public ArrayList<Wine> getWinesFromVintage(int earliest, int latest) {

        ArrayList<Wine> wines = new ArrayList<>();
        Wine wine = null;
        String sql = "SELECT * FROM wine WHERE vintage>=? and vintage<=?";
        try (Connection conn = databaseManager.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, earliest);
            ps.setInt(2, latest);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    wine = new Wine(
                            rs.getString("name"),
                            rs.getString("description"),
                            rs.getInt("price"),
                            rs.getInt("vintage"),
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            ""
                    );
                    wines.add(wine);
                }
                return wines;
            }

        } catch (SQLException e) {
            log.error(e.getMessage());
            return null;
        }

    }

    @Override
    public int add(Wine toAdd) throws DuplicateEntryException {
        return 0;
    }

    /**
     * Adds a wine into the database as well as its associated tags if there are any new tags. It then links
     * the wine to the tag using the owned_by relationship. Returns the wine numeric id upon successful addition.
     * @param wineValues a string array of wine attributes from the wine csv file
     * @param wineDescription a String description of the wine
     * @param wineVintage the vintage of the wine
     * @return the wine id
     * @throws DuplicateEntryException if wine already exists.
     */
    public int add(String[] wineValues, String wineDescription, int wineVintage) throws DuplicateEntryException {
        String wineSql = "INSERT INTO wine (id, name, vintage, price, description) VALUES (?, ?, ?, ?, ?)";
        String tagSql = "INSERT INTO tag (name, type) VALUES (?, ?)";
        String ownedBySql = "INSERT INTO owned_by (wid, tname) VALUES (?, ?)";
        try (Connection conn = databaseManager.connect()) {
            try (PreparedStatement winePs = conn.prepareStatement(wineSql)) {
                executeWinePs(winePs, wineValues, wineDescription, wineVintage);
            }
            try (PreparedStatement tagPs = conn.prepareStatement(tagSql)) {
                try (PreparedStatement ownedByPs = conn.prepareStatement(ownedBySql)) {
                    executeTagPs(tagPs, wineValues, ownedByPs, "Variety");
                }
            }
        } catch (SQLException e) {
            if (e.getErrorCode() == 19) {
                System.out.println("Duplicate wine at row " + wineValues[0]);
            }
            log.error(e.getMessage());
        }
        return Integer.parseInt(wineValues[0]);
    }

    /**
     * Execute the wine prepared statement.
     * @param winePs The wine {@link PreparedStatement}
     * @param wineValues A string array of wine attributes obtained from the csv file
     * @param wineDescription A string of wine description
     * @param wineVintage an integer representing the wine vintage
     * @throws SQLException handled by method caller
     */
    public void executeWinePs(PreparedStatement winePs, String[] wineValues, String wineDescription, int wineVintage) throws SQLException {
        winePs.setInt(1, Integer.parseInt(wineValues[0]));
        winePs.setString(2, wineValues[10]);
        winePs.setInt(3, wineVintage);
        if (!wineValues[4].isEmpty()) {
            winePs.setInt(4, Integer.parseInt(wineValues[4]) );
        } else {
            winePs.setNull(4, Types.INTEGER);
        }
        winePs.setString(5, wineDescription);
        winePs.executeUpdate();
//        System.out.println("Added wine number " + wineValues[0] + " successfully. Name: " + wineValues[10]);
    }

    /**
     * Executes the variety tag prepared statement.
     * @param tagPs the tag's {@link PreparedStatement}
     * @param wineValues A string wine attributes obtained from the csv file
     * @param ownedByPs the {@link PreparedStatement} for an owned_by relationship
     */
    public void executeTagPs(PreparedStatement tagPs, String[] wineValues, PreparedStatement ownedByPs, String tagType) {
        try {
            //TODO Modularize wineValues into a String tagName and an int wineId
            if (!wineValues[11].isEmpty()) {
                tagPs.setString(1, wineValues[11]);
                tagPs.setString(2, tagType);
                tagPs.executeUpdate();
                executeOwnedByPs(ownedByPs, Integer.parseInt(wineValues[0]), wineValues[11]);
//                System.out.println("Successfully added " + tagType +wineValues[11]);
            }
        } catch (SQLException e) {
            if (e.getErrorCode() == 19) {
                //tag already exists, link via owned_by anyway.
                if (!wineValues[11].isEmpty()) {
                    executeOwnedByPs(ownedByPs, Integer.parseInt(wineValues[0]), wineValues[11]);
                }
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
            log.error(e.getMessage());
        }
    }

    @Override
    public void delete(int id) {

    }

    @Override
    public void update(Wine toUpdate) {

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

    /**
     * Splits a csv wine row into a string array. Prints out the row number if the provided csv wine row is invalid.
     * @param line wine csv string
     * @param wineNum the current wine number
     * @return A string array of wine values
     */
    public String[] splitWineRow(String line, int wineNum) throws InvalidWineException {
        String[] wineValues = line.split(",");
        if (wineValues.length != 13) {
            throw new InvalidWineException("Failed to read wine at row #" + wineNum);
        }
        return wineValues;
    }

    /**
     * Reads through each line of the 130k wine csv file and processes them. Takes in the csv file location as inputs as
     * well as a designated output ArrayList for any wine row that is erroneous.
     * @param winePath The url of the 130k wine csv.
     * @param wineDescriptionPath The url of the 130k wine description csv.
     */
    public void wineCsvReader(String winePath, String wineDescriptionPath) throws IOException {
        String line = "";
        //TODO replace buffered reader with csvreader.
        BufferedReader br = new BufferedReader(new FileReader(winePath));
        BufferedReader br2 = new BufferedReader(new FileReader(wineDescriptionPath));
        int i = 0;
        while ((line = br.readLine()) != null) {
            if (i != 0 && i < 2000) {
                try {
                    String[] wineValues = splitWineRow(line, i);
                    String wineDescription = br2.readLine();
                    add(wineValues, wineDescription, extractVintage(wineValues[10]));
                } catch (InvalidWineException e) {
                    System.out.println(e);
                } catch (DuplicateEntryException e) {
                    throw new RuntimeException(e);
                }
            }
            if (i % 100 == 0) {
                log.info(i);
            }
            i++;
        }
    }

    /**
     * Called upon first run of jar, fills the database created by {@link DatabaseManager} with the initial
     * 130k wines.
     */
    public void initializeAllWines() {
        //TODO: Please fix file path when building! "/csvFiles/Wine130k.csv"

        String path = WineDAO.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        path = URLDecoder.decode(path, StandardCharsets.UTF_8);
        File jarDir = new File(path);

        String buildPath = String.valueOf(jarDir.getParentFile());
        buildPath = buildPath.substring(0, buildPath.length() - 12);
        String wineFilePath = buildPath + "resources/main/csvFiles/WineNoDescription130k.csv";
        String wineFileDescriptionPath = buildPath + "resources/main/csvFiles/WineDescription130k.csv";
        //noinspection TryWithIdenticalCatches
        try {
            wineCsvReader(wineFilePath, wineFileDescriptionPath);
        } catch (FileNotFoundException e) {
//            throw new RuntimeException(e);
            log.error(e.getMessage());
//            System.exit(0);
        } catch (IOException e) {
//            throw new RuntimeException(e);
            log.error(e.getMessage());
//            System.exit(0);
        }
    }

    public static void main(String[] args) {
        WineDAO wineDAO = new WineDAO();
        wineDAO.initializeAllWines();
    }

}
