package seng202.team1.repository.DAOs;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import seng202.team1.models.Wine;
import seng202.team1.repository.DatabaseManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Data Access Object for the Wishlist functionality.
 */
public class WishlistDAO {

    private final DatabaseManager databaseManager = DatabaseManager.getInstance();
    private static final Logger LOG = LogManager.getLogger(WishlistDAO.class);
    private static WishlistDAO instance;

    /**
     * Returns the singleton of the WishlistDAO if it exists, else one is created.
     * @return Singleton of the WishlistDAO
     */
    public static WishlistDAO getInstance() {
        if (instance == null) {
            instance = new WishlistDAO();
        }
        return instance;
    }


    /**
     * Selects wine elements from copy.db where the userid maps to the wine's wineid in the wishlist table.
     * @param userId is the ID of the current user
     * @return wineList array containing all wines in the user's wishlist
     */
    public ArrayList<Wine> fetchWines(int userId) {
        ArrayList<Wine> wineList;
        String sql = """
                SELECT id, wine_name, description, points, price, tag.name as tag_name, tag.type as tag_type
                FROM (SELECT id, name as wine_name, description, points, price
                      FROM wine
                      JOIN wishlist ON wine.id = wishlist.wineID
                WHERE wishlist.userID = ?)
                JOIN owned_by ON id = owned_by.wid
                JOIN tag ON owned_by.tname = tag.name
                ORDER BY id;""";

        try (Connection conn = databaseManager.connect();
             PreparedStatement wishlistPS = conn.prepareStatement(sql)) {
            wishlistPS.setInt(1, userId);
            try (ResultSet rs = wishlistPS.executeQuery()) {
                wineList = SearchDAO.processResultSetIntoWines(rs);
            }
            return wineList;
        } catch (SQLException e) {
            LOG.error("Error: Could not fetch wines from wishlist, {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /**
     * Adds a wine to the wishlist table in the database.
     * @param wineID the id of the wine to be added
     * @param userID the id of the user adding the wine
     */
    public void addWine(int wineID, int userID) {
        String sql = "INSERT INTO wishlist (userID, wineID) VALUES (?,?)";
        try (Connection conn = databaseManager.connect()) {
            try (PreparedStatement wishlistPS = conn.prepareStatement(sql)) {
                wishlistPS.setInt(1, userID);
                wishlistPS.setInt(2, wineID);
                wishlistPS.executeUpdate();
            }
        } catch (SQLException e) {
            LOG.error("Error: Could not add wine to wishlist {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /**
     * Removes a wine from the wishlist table in the database.
     * @param wineID the id of the wine to be removed
     * @param userID the id of the user removing the wine
     */
    public void removeWine(int wineID, int userID) {
        String sql = "DELETE FROM wishlist WHERE userID = ? AND wineID = ?";
        try (Connection conn = databaseManager.connect()) {
            try (PreparedStatement wishlistPS = conn.prepareStatement(sql)) {
                wishlistPS.setInt(1, userID);
                wishlistPS.setInt(2, wineID);
                wishlistPS.executeUpdate();
            }
        } catch (SQLException e) {
            LOG.error("Error: Could not remove wine from wishlist {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /**
     * Checks if a wine is in the wishlist table in the database for the current user.
     * @param wineID the id of the wine to be checked
     * @param userID the id of the current user
     * @return true if the wine is in the users wishlist
     */
    public boolean checkWine(int wineID, int userID) {
        String sql = "SELECT COUNT(*) "
                      + "FROM wishlist "
                      + "WHERE wineID = ? AND userID = ? ";
        try (Connection conn = databaseManager.connect();
             PreparedStatement wishlistPS = conn.prepareStatement(sql)) {
            wishlistPS.setInt(1, wineID);
            wishlistPS.setInt(2, userID);
            try (ResultSet rs = wishlistPS.executeQuery()) {
                if (rs.next()) {
                    int count = rs.getInt(1);
                    return count > 0;
                }
            }
        } catch (SQLException e) {
            LOG.error("Error: Could not check if wine is in wishlist {}", e.getMessage());
            throw new RuntimeException(e);
        }
        return false;
    }
}
