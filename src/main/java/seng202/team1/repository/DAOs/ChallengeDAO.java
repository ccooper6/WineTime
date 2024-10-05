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
 * Data Access Object for the challenge tracker functionality,
 * updates the database with information on user and challenge, and gets information
 * from the database.
 * @author Lydia Jackson, Wen Sheng Thong
 */
public class ChallengeDAO {
    private static final Logger LOG = LogManager.getLogger(ChallengeDAO.class);
    private final DatabaseManager databaseManager = DatabaseManager.getInstance();

    /**
     * Inserts challenge into the database.
     * @param name name of the challenge
     * @param description description of the challenge
     */
    public void insertChallenge(String name, String description) {
        String sql = "INSERT INTO challenge (name, description) VALUES (?, ?)";
        try (Connection conn = databaseManager.connect()) {
            try (PreparedStatement chalps = conn.prepareStatement(sql)) {
                chalps.setString(1, name);
                chalps.setString(2, description);
                chalps.executeUpdate();
            }
        } catch (SQLException e) {
            LOG.error(e.getMessage());
        }

    }

    /**
     * Inserts challenge wine into the database.
     * @param wineID wine id
     * @param cname challenge name
     */
    public void insertWineChal(int wineID, int uid, String cname) {
        String sql = "INSERT INTO challenge_wine (wineID, cname, uid) VALUES (?, ?, ?)";
        try (Connection conn = databaseManager.connect()) {
            try (PreparedStatement chalps = conn.prepareStatement(sql)) {
                chalps.setInt(1, wineID);
                chalps.setString(2, cname);
                chalps.setInt(3, uid);
                chalps.executeUpdate();
                conn.commit();
            }
        } catch (SQLException e) {
            LOG.error(e.getMessage());
        }

    }

    /**
     * Inserts the user and challenge into active challenges in database.
     * @param useID user id
     * @param cname challenge name
     */
    public void userToChallenge(int useID, String cname) {
        String sql = "INSERT INTO active_challenge (userID, cname) VALUES (?, ?)";
        try (Connection conn = databaseManager.connect()) {
            try (PreparedStatement chalps = conn.prepareStatement(sql)) {
                chalps.setInt(1, useID);
                chalps.setString(2, cname);
                chalps.executeUpdate();
                conn.commit();
            }
        } catch (SQLException e) {
            LOG.error(e.getMessage());
        }
    }

    /**
     * Checks to see if there are challenges in the database.
     * @return boolean if challenges in database
     */
    private Boolean challengeExists() {
        String test = "SELECT * FROM challenge";
        try (Connection conn = databaseManager.connect()) {
            try (PreparedStatement ps = conn.prepareStatement(test)) {
                ResultSet rs = ps.executeQuery();
                return rs.next();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Checks to see if there are wines for the challenge in database.
     * @return boolean if wine in challenge_wine.
     */
    private Boolean challengeHasWines() {
        String test = "SELECT * FROM challenge_wine";
        try (Connection conn = databaseManager.connect()) {
            try (PreparedStatement ps = conn.prepareStatement(test)) {
                ResultSet rs = ps.executeQuery();
                return rs.next();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Checks if the user has a challenge assigned to them.
     * @param userID user id
     * @param cname challenge name
     * @return boolean if the user has started a challenge.
     */
    private Boolean userHasChallenge(int userID, String cname) {
        String test = "SELECT * FROM active_challenge WHERE userID = ? AND cname = ?";
        try (Connection conn = databaseManager.connect()) {
            try (PreparedStatement ps = conn.prepareStatement(test)) {
                ps.setInt(1, userID);
                ps.setString(2, cname);
                ResultSet rs = ps.executeQuery();
                return rs.next();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Gets the name of the active challenge for the user.
     * @param userID user id
     * @return challenge name
     */
    public String getChallengeForUser(int userID) {
        String sql = "SELECT * FROM active_challenge WHERE userID = ?";
        try (Connection conn = databaseManager.connect()) {
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, userID);
                ResultSet rs = ps.executeQuery();
                System.out.println(rs.getFetchSize());
                return rs.getString("cname");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * Puts the challenge into the copy database with a fixed name and description.
     */
    public void initaliseChallenge() {
        if (!challengeExists()) {
            insertChallenge("Variety Challenge", "Expand your palate with the variety challenge, "
                    + "this challenge encourages you to explore wines of different varieties, with wines of a range of "
                    + "colours, from different places and different grapes.");
        }
    }

    /**
     * Inserts the wine into the challenge.
     * @param wineIds an array list of integer wine id
     * @param challengeName the name of the challenge
     * @param uid current user uid
     */
    public void wineInChallenge(ArrayList<Integer> wineIds, int uid, String challengeName) {
        for (Integer wineId : wineIds) {
            insertWineChal(wineId, uid, challengeName);
        }
    }


    /**
     * Checks the user already has the challenge active, if not calls a method to update the database.
     * @param uid user id
     * @param cname challenge name
     */
    public void userActivatesChallenge(int uid, String cname, ArrayList<Integer> wineIds) {
        if (!userHasChallenge(uid, cname)) {
            userToChallenge(uid, cname);
            wineInChallenge(wineIds, uid, cname);
        } else {
            System.out.println("User already registered for this challenge");
        }
    }

    /**
     * Gets the wines for the challenge, and returns them as array list of wines.
     * @param cname challenge name
     * @param uid the user id
     * @return ArrayList of wines for the challenge
     */
    public ArrayList<Wine> getWinesForChallenge(String cname, int uid) {
        ArrayList<Wine> wineList;
        String sql =    "SELECT id, wine_name, description, points, price, tag.name as tag_name, tag.type as tag_type\n"
                + "FROM (SELECT id, name as wine_name, description, points, price\n"
                + "      FROM wine\n"
                + "      JOIN challenge_wine ON wine.id = challenge_wine.wineID\n"
                + "WHERE challenge_wine.cname = ?\n"
                + "AND challenge_wine.uid = ?)\n"
                + "JOIN owned_by ON id = owned_by.wid\n"
                + "JOIN tag ON owned_by.tname = tag.name\n"
                + "ORDER BY id;";

        try (
                Connection conn = databaseManager.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql);
        ) {
            pstmt.setString(1, cname);
            pstmt.setInt(2, uid);
            try (ResultSet rs = pstmt.executeQuery()) {
                wineList = processResultSetIntoWines(rs);
            }
            return wineList;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Processes the result set from the database into an array list of wines.
     * @param resultSet the result set from the database
     * @return array list of wines
     * @throws SQLException if there is an error with the SQL
     */
    private ArrayList<Wine> processResultSetIntoWines(ResultSet resultSet) throws SQLException
    {
        ArrayList<Wine> wineList = new ArrayList<Wine>();
        int currentID = -1;
        WineBuilder currentWineBuilder = null;
        while (resultSet.next())
        {
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
     * removes the challenge from the users active challenges.
     * @param uid users id
     * @param cname the name of the challenge
     */
    public void challengeCompleted(int uid, String cname)
    {
        String sql = "DELETE FROM active_challenge WHERE userID = ? AND cname = ?;";
        try (Connection conn = databaseManager.connect()) {
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, uid);
                ps.setString(2, cname);
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        String sql1 = "DELETE FROM challenge_wine WHERE cname = ? AND uid = ?;";
        try (Connection conn = databaseManager.connect()) {
            try (PreparedStatement chalps = conn.prepareStatement(sql1)) {
                chalps.setString(1, cname);
                chalps.setInt(2, uid);
                chalps.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }




}
