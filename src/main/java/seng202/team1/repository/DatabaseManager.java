package seng202.team1.repository;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import seng202.team1.exceptions.InstanceAlreadyExistsException;

import java.io.*;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;
import java.util.Objects;

/**
 * Singleton class responsible for interaction with SQLite database.
 */
public class DatabaseManager {
    private static DatabaseManager instance = null;
    private static final Logger LOG = LogManager.getLogger(DatabaseManager.class);
    public String databasePath;
    private boolean reset = false;

    /**
     * Private constructor for singleton purposes.
     * Creates database if it does not already exist in specified location
     * @param urlIn string url of database to load (if applicable)
     */
    private DatabaseManager(String urlIn) {
        this.databasePath = Objects.requireNonNullElseGet(urlIn, this::getDatabasePath);
        initialiseDB();
    }

    /**
     * Singleton method to get current Instance if exists otherwise create it.
     * @return the single instance DatabaseSingleton
     */
    public static DatabaseManager getInstance() {
        if (instance == null) {
            instance = new DatabaseManager(null);
        }
        return instance;
    }

    /**
     * WARNING Allows for setting specific database url (currently only needed for test databases, but may be useful
     * in future) USE WITH CAUTION. This does not override the current singleton instance so must be the first call.
     * @param url string url of database to load (this needs to be full url e.g. "jdbc:sqlite:./src/...")
     * @return current singleton instance
     * @throws InstanceAlreadyExistsException if there is already a singleton instance
     */
    public static DatabaseManager initialiseInstanceWithUrl(String url) throws InstanceAlreadyExistsException {
        if (instance == null) {
            instance = new DatabaseManager(url);
        } else {
            throw new InstanceAlreadyExistsException("Database Manager instance already exists, cannot create with url: " + url);
        }
        return instance;
    }

    /**
     * If called, forces a reset of the database on next initialisation and re-initialises.
     */
    public void forceReset() {
        reset = true;
        initialiseDB();
    }

    /**
     *  WARNING Sets the current singleton instance to null.
     */
    public static void REMOVE_INSTANCE() {
        instance = null;
    }

    /**
     * Connect to the database.
     * @return database connection
     */
    public Connection connect() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(this.databasePath);
        } catch (SQLException e) {
            LOG.error("Error in DatabaseManager.connect(): SQLException {}", e.getMessage());
        }
        return conn;
    }

    /**
     * Gets path to the database relative to the jar file.
     * @return jdbc encoded url location of database
     */
    private String getDatabasePath() {
        String path = DatabaseManager.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        path = URLDecoder.decode(path, StandardCharsets.UTF_8);
        File jarDir = new File(path);
        return "jdbc:sqlite:" + jarDir.getParentFile() + "/WineTime.db";
    }

    /**
     * Called upon the initialisation of the DatabaseManager class when the user clicks log in. Copies the database from
     * within the jar to a location outside the jar as "copy.db" in the directory of the jar to be used by the app.
     * When being run in a test environment, copies the database into the test resource directory as "test_database.db"
     */
    public void initialiseDB() {
        // removes "jdbc:sqlite:"
        String copyPath = this.databasePath.substring(12);
        Path copy = Paths.get(copyPath);

        if (reset) {
            try {
                Files.deleteIfExists(copy);
                LOG.info("Existing database file deleted due to reset flag.");
            } catch (IOException e) {
                LOG.error("Error: Could not delete existing database, {}", e.getMessage());
            } finally {
                reset = false;
            }
        }

        File dbFile = copy.toFile();
        if (dbFile.exists()) {
            if (!dbFile.isFile()) {
                LOG.error("Error: Source database is not a file");
            }

            return;
        }

        // Differentiate what main.db to use based on whether we are running tests or main application jar
        try {
            if (System.getProperty("test.env") == null) {
                InputStream ogPath = DatabaseManager.class.getResourceAsStream("/sql/main.db");
                LOG.info("Copying database from: {} to: {}", ogPath, copy);

                assert ogPath != null;
                Files.copy(ogPath, copy);
                LOG.info("Database copied successfully to main environment.");
            } else {
                Path ogPath = Paths.get("src/main/resources/sql/main.db");

                LOG.info("Copying test database from: {} to: {}", ogPath, copy);
                Files.copy(ogPath, copy);
                LOG.info("Database copied successfully to test environment.");
            }
        } catch (FileNotFoundException e) {
            LOG.error("Error: Could not find source database");
        } catch (IOException e) {
            LOG.error("Error: Could not initialise database {}", e.getMessage());
        }
    }
}

