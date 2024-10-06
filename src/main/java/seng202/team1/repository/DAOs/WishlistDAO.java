package seng202.team1.repository.DAOs;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import seng202.team1.models.Wine;
import seng202.team1.models.WineBuilder;
import seng202.team1.repository.DatabaseManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Data Access Object for the Wishlist functionality.
 * Author: Elise Newman and Yuhao Zhang
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
     * Takes a result set of wines with its tags and process them into an ArrayList of wines.
     *
     * @param resultSet {@link ResultSet} the result set received after a SELECT statement in the
     *                                   database. Each row should contain the wine id, name, description
     *                                   and price and the tag name and type. Rows are seperated by tags.
     *                                   The result set must be ordered by wine id.
     * @return {@link ArrayList} of wines containing all wines in the result set
     * @throws SQLException when a column mentioned in result set is not provided.
     */
    //TODO delete this
    private ArrayList<Wine> processResultSetIntoWines(ResultSet resultSet) throws SQLException
    {
        ArrayList<Wine> wineList = new ArrayList<>();

        int currentID = -1;
        WineBuilder currentWineBuilder = null;

        while (resultSet.next()) {
            if (resultSet.getInt("id") != currentID) {
                if (currentWineBuilder != null) {
                    wineList.add(currentWineBuilder.build());
                }

                currentWineBuilder = WineBuilder.genericSetup(resultSet.getInt("id"),
                        resultSet.getString("wine_name"),
                        resultSet.getString("description"),
                        resultSet.getInt("price"));

                currentID = resultSet.getInt("id");
            }

            if (currentWineBuilder == null) {
                throw new NullPointerException("Current Wine Builder is null!");
            }

            switch (resultSet.getString("tag_type")) {
                case "Variety":
                    currentWineBuilder.setVariety(resultSet.getString("tag_name"));
                    break;
                case "Province":
                    currentWineBuilder.setProvince(resultSet.getString("tag_name"));
                    break;
                case "Region":
                    currentWineBuilder.setRegion(resultSet.getString("tag_name"));
                    break;
                case "Vintage":
                    currentWineBuilder.setVintage(resultSet.getInt("tag_name"));
                    break;
                case "Country":
                    currentWineBuilder.setCountry(resultSet.getString("tag_name"));
                    break;
                case "Winery":
                    currentWineBuilder.setWinery(resultSet.getString("tag_name"));
                    break;
                default:
                    LOG.error("Tag type {} is not supported!", resultSet.getString("tag_type"));
            }
        }
        if (currentWineBuilder != null) {
            wineList.add(currentWineBuilder.build());
        }

        return wineList;
    }

    /**
     * Selects wine elements from copy.db where the userid maps to the wine's wineid in the wishlist table.
     *
     * @param userId is the ID of the active user
     * @return wineList array containing all wines in the user's wishlist
     */
    public ArrayList<Wine> fetchWines(int userId) {
        ArrayList<Wine> wineList;
        String sql =    "SELECT id, wine_name, description, points, price, tag.name as tag_name, tag.type as tag_type\n"
                + "FROM (SELECT id, name as wine_name, description, points, price\n"
                + "      FROM wine\n"
                + "      JOIN wishlist ON wine.id = wishlist.wineID\n"
                + "WHERE wishlist.userID = ?)\n"
                + "JOIN owned_by ON id = owned_by.wid\n"
                + "JOIN tag ON owned_by.tname = tag.name\n"
                + "ORDER BY id;";

        try (
                Connection conn = databaseManager.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql);
        ) {
            pstmt.setInt(1, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                wineList = processResultSetIntoWines(rs);
            }
            return wineList;
        } catch (SQLException e) {
            LOG.error("Error in WishlistDAO.fetchWines(): SQLException {}", e.getMessage());
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
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, userID);
                ps.setInt(2, wineID);
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            LOG.error("Error in WishlistDAO.addWine(): SQLException {}", e.getMessage());
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
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, userID);
                ps.setInt(2, wineID);
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            LOG.error("Error in WishlistDAO.removeWine(): SQLException {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /**
     * Checks if a wine is in the wishlist table in the database.
     * @param wineID the id of the wine to be checked
     * @param userID the id of the user checking the wine
     * @return true if the wine is in the wishlist
     */
    public boolean checkWine(int wineID, int userID) {
        String sql = "SELECT COUNT(*)\n"
                      + "FROM wishlist\n"
                      + "WHERE wineID = ? AND userID = ?\n";
        try (
                Connection conn = databaseManager.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql);
        ) {
            pstmt.setInt(1, wineID);
            pstmt.setInt(2, userID);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    int count = rs.getInt(1);
                    return count > 0;
                }
            }
        } catch (SQLException e) {
            LOG.error("Error in WishlistDAO.checkWine(): SQLException {}", e.getMessage());
            throw new RuntimeException(e);
        }
        return false;
    }

    /**
     * Checks the existence of a wine in the database.
     * @param wineID the id of the wine in question
     * @return true if present in wine table
     */
    // TODO rebase into Wine
    public boolean checkWineID(int wineID) {
        String uidSql = "SELECT COUNT (*) FROM wine WHERE wine.id = ?";
        try (Connection conn = databaseManager.connect();
                PreparedStatement pstmt = conn.prepareStatement(uidSql);
        ) {
            pstmt.setInt(1, wineID);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    int count = rs.getInt(1);
                    return count > 0;
                }
            }
        } catch (SQLException e) {
            LOG.error("Error in WishlistDAO.checkWineID(): SQLException {}", e.getMessage());
            throw new RuntimeException(e);
        }
        return false;
    }

    /**
     * Checks the existence of a user in the database.
     * @param userID is the id of the active user
     * @return true if present in database
     */
    // TODO delete this
    public boolean checkUserID(int userID) {
        String uidSql = "SELECT COUNT (*) FROM user WHERE user.id = ?";
        try (Connection conn = databaseManager.connect();
                PreparedStatement pstmt = conn.prepareStatement(uidSql);
        ) {
            pstmt.setInt(1, userID);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    int count = rs.getInt(1);
                    return count > 0;
                }
            }
        } catch (SQLException e) {
            LOG.error("Error in WishlistDAO.getUserID(): SQLException {}", e.getMessage());
            throw new RuntimeException(e);
        }
        return false;
    }
}
