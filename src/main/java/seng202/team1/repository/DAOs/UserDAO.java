package seng202.team1.repository.DAOs;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import seng202.team1.exceptions.DuplicateEntryException;
import seng202.team1.models.User;
import seng202.team1.repository.DatabaseManager;

import java.sql.*;
import java.util.Objects;

/**
 * Data Access Object for the User class.
 * @author Caleb Cooper, Isaac Macdonald, Yuhao Zhang, Wen Sheng Thong
 */
public class UserDAO {

    private final DatabaseManager databaseManager;
    private static final Logger LOG = LogManager.getLogger(UserDAO.class);

    /**
     * Constructor for UserDAO.
     */
    public UserDAO() {
        databaseManager = DatabaseManager.getInstance();
    }

    /**
     * This method takes a username and checks that the user is in the database and that the password matches.
     * If they do, the corresponding User will be returned, otherwise null
     * @param username the encrypted username
     * @param password the hashed password
     * @return The user is the credentials are found in the database, else null
     */
    public User tryLogin(String username, int password) {
        String sql = "SELECT * FROM user WHERE username = ?";
        try (Connection conn = databaseManager.connect();
             PreparedStatement userPS = conn.prepareStatement(sql)) {
            userPS.setString(1, username);
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

    public int add(User toAdd) throws DuplicateEntryException {
        String sql = "INSERT INTO user (username, password, name) VALUES (?, ?, ?)";
        try (Connection conn = databaseManager.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, toAdd.getEncryptedUserName());
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
     * @param username the encrypted username
     * @return the name of the user
     */
    public String getName(String username) {
        String sql = "SELECT name FROM user WHERE username = ?";
        try (Connection conn = databaseManager.connect();
             PreparedStatement userPS = conn.prepareStatement(sql)) {
            userPS.setString(1, username);
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

    public void delete(int id) {
        String sql = "DELETE FROM user WHERE id = ?";
        try (Connection conn = databaseManager.connect();
             PreparedStatement userPS = conn.prepareStatement(sql)) {
            userPS.setInt(1, id);
            userPS.executeUpdate();

        } catch (SQLException e) {
            LOG.error("Error: Could not delete user, {}", e.getMessage());
        }
    }
}


