package seng202.team0.repository;

import org.apache.commons.lang3.NotImplementedException;
import seng202.team0.exceptions.DuplicateEntryException;
import seng202.team0.models.User;

import java.sql.*;
import java.util.List;
import java.util.Objects;

public class UserDAO implements DAOInterface<User> {

    private final DatabaseManager databaseManager;

    public UserDAO() {databaseManager = DatabaseManager.getInstance();}
    @Override
    public List getAll() {
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
    public boolean tryLogin(String username, String password) {
        String sql = "SELECT hashed_password FROM users WHERE username = ?";
        try (
                Connection conn = databaseManager.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql);
        ) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();

            if (!rs.next()) {
                return false;
            }

            String hashed_password = rs.getString("hashed_password");

            return Objects.equals(hashed_password, password);



        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }

    @Override
    public int add(User toAdd) throws DuplicateEntryException {
        String sql = "INSERT INTO users (username, password) VALUES (?, ?)";
        try (Connection conn = databaseManager.connect();
            PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setString(1, toAdd.getEncryptedUserName());
            ps.setString(2, toAdd.getHashedPassword());
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            int insertId = -1;
            if (rs.next()) {
                insertId = rs.getInt(1);
            }
            return insertId;
        } catch (SQLException sqlException) {
            if (sqlException.getErrorCode() == 19) {
                throw new DuplicateEntryException("Duplicate username");
            }
            return -1;
        }
    }

    @Override
    public void delete(int id) {

    }

    @Override
    public void update(User toUpdate) {

    }
}


