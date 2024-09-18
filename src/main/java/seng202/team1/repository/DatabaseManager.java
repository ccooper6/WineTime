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

/**
 * Singleton class responsible for interaction with SQLite database.
 * @author Morgan English, Caleb Cooper, Yuhao Zhang, Isaac Macdonald
 */
public class DatabaseManager {
    private static DatabaseManager instance = null;
    private static final Logger log = LogManager.getLogger(DatabaseManager.class);
    public String url;
    private boolean reset = false;

    /**
     * Private constructor for singleton purposes.
     * Creates database if it does not already exist in specified location
     * @param urlIn string url of database to load (if applicable)
     */
    private DatabaseManager(String urlIn) {
        if (urlIn != null) {
            this.url = urlIn;
        } else {
            this.url = getDatabasePath();
        }
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
            conn = DriverManager.getConnection(this.url);
        } catch (SQLException e) {
            log.error(e);
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
        return "jdbc:sqlite:" + jarDir.getParentFile() + "/copy.db";
    }

    /**
     * Called upon the initialisation of the DatabaseManager class when the user clicks log in. Copies the database from
     * within the jar to a location outside the jar as "copy.db" in the directory of the jar to be used by the app.
     * When being run in a test environment, copies the database into the test resource directory as "test_database.db"
     */
    public void initialiseDB() {
        // removes "jdbc:sqlite:"
        String copyPath = this.url.substring(12);
        Path copy = Paths.get(copyPath);

        if (reset) {
            try {
                Files.deleteIfExists(copy);
                log.info("Existing database file deleted due to reset flag.");
            } catch (IOException e) {
                log.error("Error deleting existing database file", e);
            } finally {
                reset = false;
            }
        }

        // Differentiate what og.db to use based on whether we are running tests or main application jar
        try {
            if (System.getProperty("test.env") == null) {
                InputStream ogPath = DatabaseManager.class.getResourceAsStream("/sql/og.db");
                log.info("Copying database from: " + ogPath + " to: " + copy);
                Files.copy(ogPath, copy);
                log.info("Database copied successfully.");
            } else {
                Path ogPath = Paths.get("src/main/resources/sql/og.db");
                log.info("Copying test database from: " + ogPath + " to: " + copy);
                Files.copy(ogPath, copy);
                log.info("Database copied successfully.");
            }
        } catch (FileNotFoundException e) {
            log.info("DB File already exists. - Did not replace");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}

