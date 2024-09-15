package seng202.team1.repository;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import seng202.team1.models.Wine;
import seng202.team1.models.WineBuilder;

import java.sql.*;
import java.util.ArrayList;

public class WishlistDAO {
    /**
     * An instance of the databaseManager to setup the connection to the database
     */
    private final DatabaseManager databaseManager = DatabaseManager.getInstance();
    /**
     * A logger to handle the logging of any errors
     */
    private static final Logger log = LogManager.getLogger(LogWineDao.class);
    private static WishlistDAO instance;

    /**
     * Returns the singleton of the WishlistDAO if it exists, else one is created
     * @return Singleton of the WishlistDAO
     */
    public static WishlistDAO getInstance() {
        if (instance == null) {
            instance = new WishlistDAO();
        }
        return instance;
    }
    /**
     * Takes a result set of wines with its tags and process them into an ArrayList of wines
     *
     * @param resultSet {@link ResultSet} the result set received after a SELECT statement in the
     *                                   database. Each row should contain the wine id, name, description
     *                                   and price and the tag name and type. Rows are seperated by tags.
     *                                   The result set must be ordered by wine id.
     * @return {@link ArrayList} of wines containing all wines in the result set
     * @throws SQLException when a column mentioned in result set is not provided.
     */
    private ArrayList<Wine> processResultSetIntoWines(ResultSet resultSet) throws SQLException
    {
        ArrayList<Wine> wineList = new ArrayList<Wine>();

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
                    log.error("Tag type {} is not supported!", resultSet.getString("tag_type"));
            }
        }
        if (currentWineBuilder != null) {
            wineList.add(currentWineBuilder.build());
        }

        return wineList;
    }

    /**
     * Selects wine elements from copy.db where the userid maps to the wine's wineid in the wishlist table
     *
     * @param UserId is the ID of the active user
     * @return wineList array containing all wines in the user's wishlist
     */
    public ArrayList<Wine> fetchWines(int UserId) {
        ArrayList<Wine> wineList;
        String sql =    "SELECT id, wine_name, description, points, price, tag.name as tag_name, tag.type as tag_type\n" +
                        "FROM (SELECT id, name as wine_name, description, points, price\n" +
                        "      FROM wine\n" +
                        "      JOIN wishlist ON wine.id = wishlist.wineID\n" +
                        "WHERE wishlist.userID = ?)\n" +
                        "JOIN owned_by ON id = owned_by.wid\n" +
                        "JOIN tag ON owned_by.tname = tag.name\n" +
                        "ORDER BY id;";

        try (
                Connection conn = databaseManager.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql);
        ) {
            pstmt.setInt(1, UserId);
            try (ResultSet rs = pstmt.executeQuery()) {
                wineList = processResultSetIntoWines(rs);
            }
            return wineList;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void addWine(int wineID, int userID) {
        String sql = "INSERT INTO wishlist (userID, wineID) VALUES (?,?)";
        try(Connection conn = databaseManager.connect()) {
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, userID);
                ps.setInt(2, wineID);
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void removeWine(int wineID, int userID) {
        String sql = "DELETE FROM wishlist WHERE userID = ? AND wineID = ?";
        try(Connection conn = databaseManager.connect()) {
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, userID);
                ps.setInt(2, wineID);
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean checkWine(int wineID, int userID) {
        String sql = "SELECT COUNT(*)\n" +
                      "FROM wishlist\n" +
                      "WHERE wineID = ? AND userID = ?\n";
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
            throw new RuntimeException(e);
        }
        return false;
    }
}
