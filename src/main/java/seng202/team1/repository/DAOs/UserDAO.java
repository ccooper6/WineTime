package seng202.team1.repository.DAOs;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import seng202.team1.models.User;
import seng202.team1.repository.DatabaseManager;

import java.sql.*;
import java.util.Objects;

/**
 * Data Access Object for the User class.
 */
public class UserDAO {

    private final DatabaseManager DATABASEMANAGER;
    private static final Logger LOG = LogManager.getLogger(UserDAO.class);

    /**
     * Constructor for UserDAO.
     */
    public UserDAO() {
        DATABASEMANAGER = DatabaseManager.getInstance();
    }

    /**
     * This method takes a username and checks that the user is in the database and that the password matches.
     * If they do, the corresponding User will be returned, otherwise null
     * @param username the hashed username
     * @param password the hashed password
     * @return The user is the credentials that are found in the database, else null
     */
    public User tryLogin(int username, int password) {
        String sql = "SELECT * FROM user WHERE username = ?";
        try (Connection conn = DATABASEMANAGER.connect();
                PreparedStatement userPS = conn.prepareStatement(sql)) {
            userPS.setString(1, String.valueOf(username));
            ResultSet rs = userPS.executeQuery();

            if (!rs.next()) {
                return null;
            }
            int hashedPassword = rs.getInt("password");

            if (Objects.equals(hashedPassword, password)) {
                int id = rs.getInt("id");
                String name = rs.getString("name");

                return new User(id, name, username);
            } else {
                return null;
            }
        } catch (SQLException e) {
            LOG.error("Error: Could not login the user, {}", e.getMessage());
            return null;
        }
    }

    /**
     * Adds a new user to the database.
     * @param toAdd The user to add. Must contain username, hashed password and name
     * @return The result of the sql query. 0 if user already exists, 1 if successful, 2 if an error occurred
     */
    public int add(User toAdd) {
        String sql = "INSERT INTO user (username, password, name) VALUES (?, ?, ?)";
        try (Connection conn = DATABASEMANAGER.connect();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, toAdd.getHashedUsername());
            ps.setInt(2, toAdd.getHashedPassword());
            ps.setString(3, toAdd.getName());
            ps.executeUpdate();
            LOG.info("Successfully registered new user");
            return 1; // Username created successfully
        } catch (SQLException e) {
            if (e.getErrorCode() == 19) {
                return 0; // Duplicate username
            }
            LOG.error("Error: Could not register new user, {}", e.getMessage());
            return 2; // Other error occurred
        }
    }

    /**
     * Returns the name of the user with the given username.
     * @param username the hashed username
     * @return the name of the user
     */
    public String getName(int username) {
        String sql = "SELECT name FROM user WHERE username = ?";
        try (Connection conn = DATABASEMANAGER.connect();
                PreparedStatement userPS = conn.prepareStatement(sql)) {
            userPS.setInt(1, username);
            ResultSet rs = userPS.executeQuery();

            if (!rs.next()) {
                return null;
            }
            return rs.getString("name");
        } catch (SQLException e) {
            LOG.error("Error: Could not get user's name {}", e.getMessage());
            return null;
        }
    }

    /**
     * Checks whether the given user exists in the database.
     * @param id is the users id
     * @return whether the user exists
     */
    public boolean userExists(int id)
    {
        String sql = "SELECT id FROM user WHERE id = ?";
        try (Connection conn = DatabaseManager.getInstance().connect();
                PreparedStatement wishlistPS = conn.prepareStatement(sql)) {
            wishlistPS.setInt(1, id);
            try (ResultSet rs = wishlistPS.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            LOG.error("Error: Could not check if wine exists, {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }
}


