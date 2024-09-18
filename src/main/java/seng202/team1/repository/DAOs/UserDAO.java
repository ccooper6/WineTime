package seng202.team1.repository.DAOs;

import org.apache.commons.lang3.NotImplementedException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import seng202.team1.exceptions.DuplicateEntryException;
import seng202.team1.gui.controllers.WineLoggingPopupController;
import seng202.team1.models.User;
import seng202.team1.repository.DatabaseManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.Objects;

/**
 * Data Access Object for the User class.
 * @author Caleb Cooper, Isaac Macdonald, Yuhao Zhang, Wen Sheng Thong
 */
public class UserDAO implements DAOInterface<User> {

    private final DatabaseManager databaseManager;
    private static final Logger log = LogManager.getLogger(UserDAO.class);

    /**
     * Constructor for UserDAO.
     */
    public UserDAO() {
        databaseManager = DatabaseManager.getInstance();
    }

    @Override
    public ArrayList getAll() {
        throw new NotImplementedException();
    }

    @Override
    public User getOne(int id) {
        throw new NotImplementedException();
    }

    /**
     * This method takes a username and checks that the user is in the database and that the password matches.
     * @param username the encrypeted username
     * @param password the hashed password
     * @return whether the user was already in the database and the password matched.
     */
    public boolean tryLogin(String username, int password) {
        String sql = "SELECT password FROM user WHERE username = ?";
        try (
                Connection conn = databaseManager.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql);
        ) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();

            if (!rs.next()) {
                return false;
            }
            int hashedPassword = rs.getInt("password");

            return Objects.equals(hashedPassword, password);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }

    /**
     * Returns the int user id of the current user. Called during initialization of
     * {@link WineLoggingPopupController}
     * @param currentUser the current user
     * @return int uid
     */
    public int getUId(User currentUser) {
        int uid = 0;
        String uidSql = "SELECT id FROM user WHERE username = ? AND name = ?";
        try (Connection conn = databaseManager.connect()) {
            try (PreparedStatement uidPs = conn.prepareStatement(uidSql)) {
                uidPs.setString(1, currentUser.getEncryptedUserName());
                uidPs.setString(2, currentUser.getName());
                uid = uidPs.executeQuery().getInt(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return uid;
    }

    @Override
    public int add(User toAdd) throws DuplicateEntryException {
        String sql = "INSERT INTO user (username, password, name) VALUES (?, ?, ?)";
        try (Connection conn = databaseManager.connect();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, toAdd.getEncryptedUserName());
            ps.setInt(2, toAdd.getHashedPassword());
            ps.setString(3, toAdd.getName());
            ps.executeUpdate();
            log.info("Added user: " + toAdd.getEncryptedUserName());
            return 1; // Username created successfully
        } catch (SQLException sqlException) {
            if (sqlException.getErrorCode() == 19) {
                return 0; // Duplicate username
            }
            log.error(sqlException.getMessage());
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
        try (
                Connection conn = databaseManager.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql);
        ) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();

            if (!rs.next()) {
                return null;
            }
            return rs.getString("name");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM user WHERE id = ?";
        try (
                Connection conn = databaseManager.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql);
        ) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(User toUpdate) {
        throw new NotImplementedException();
    }
}


