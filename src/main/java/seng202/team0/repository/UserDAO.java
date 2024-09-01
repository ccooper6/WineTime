package seng202.team0.repository;

import org.apache.commons.lang3.NotImplementedException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import seng202.team0.App;
import seng202.team0.exceptions.DuplicateEntryException;
import seng202.team0.models.User;
import seng202.team0.models.Wine;

import java.sql.*;
import java.util.ArrayList;
import java.util.Objects;

public class UserDAO implements DAOInterface<User> {

    private final DatabaseManager databaseManager;
    private static final Logger log = LogManager.getLogger(UserDAO.class);

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

    @Override
    public int add(User toAdd) throws DuplicateEntryException {
        String sql = "INSERT INTO user (username, password, name) VALUES (?, ?, ?)";
        try (Connection conn = databaseManager.connect();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, toAdd.getEncryptedUserName());
            ps.setInt(2, toAdd.getHashedPassword());
            ps.setString(3, toAdd.getName());
            ps.executeUpdate();
            System.out.println("Added user: " + toAdd.getEncryptedUserName()); // Can delete this in the future...
            return 1; // Username created successfully
            /*try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1); // Return the generated key
                } else {
                    return -1; // No key was generated
                }
            }*/
        } catch (SQLException sqlException) {
            if (sqlException.getErrorCode() == 19) {
                return 0; // Duplicate username
            }
            log.error(sqlException.getMessage());
            return 2; // Other error occurred
        }
    }

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

    }

    @Override
    public void update(User toUpdate) {

    }
}


