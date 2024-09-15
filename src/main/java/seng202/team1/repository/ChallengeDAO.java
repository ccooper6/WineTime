package seng202.team1.repository;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import seng202.team1.models.User;
import seng202.team1.models.Wine;
import seng202.team1.models.WineBuilder;
import seng202.team1.services.ChallengeService;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for the challenge tracker functionality
 * updates the database with information on user and challenge, and gets information
 * from the database.
 * @author Lydia Jackson
 * @author Wen Sheng
 */

public class ChallengeDAO {

    private static final Logger log = LogManager.getLogger(LogWineDao.class);

    private final DatabaseManager databaseManager = DatabaseManager.getInstance();

    /**
     * inserts challenge into the database.
     * @param name
     * @param description
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
            log.error(e.getMessage());
        }

    }

    /**
     * inserts challenge wine into the database.
     * @param wineID
     * @param cname
     */

    public void insertWineChal(int wineID, String cname) {
        String sql = "INSERT INTO challenge_wine (wineID, cname) VALUES (?, ?)";
        try (Connection conn = databaseManager.connect()) {
            try (PreparedStatement chalps = conn.prepareStatement(sql)) {
                chalps.setInt(1, wineID);
                chalps.setString(2, cname);
                chalps.executeUpdate();
                conn.commit();
            }
        } catch (SQLException e) {
            log.error(e.getMessage());
        }

    }

    /**
     * inserts the user and challenge into active challenges in database.
     * @param useID
     * @param cname
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
            log.error(e.getMessage());
        }
    }

    /**
     * checks to see if there are challenges in the database.
     * @return boolean if challenges in database
     */
    private Boolean challengeExsists() {
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
     * checks to see if there are wines for the challenge in database.
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
     * checks if the user has a challenge assigned to them
     * @param userID
     * @param cname
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
        }catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * gets the name of the active challenge for the user.
     * @param userID
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
     * gets the wines ids for the wines associated with the given challenge.
     * @param cname
     * @return list of wine ids
     */
    public List<Integer> getWineIdsFromChallenge(String cname) {
        String test = "SELECT * FROM challenge_wine WHERE cname = ?";
        List<Integer> wineIds = new ArrayList<>();
        try (Connection conn = databaseManager.connect()) {
            try (PreparedStatement ps = conn.prepareStatement(test)) {
                ps.setString(1, cname);
                ResultSet rs = ps.executeQuery();
                System.out.println("wine ids for challenge (in chaldao) " + rs.getInt("wineID"));
                while (rs.next()) {
                    wineIds.add(rs.getInt("wineID"));
//                    System.out.println("wine ids for challenge (in chaldao) " + rs.getInt("wineID"));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return wineIds;
    }

    /**
     * gives the number of active challenges for the user
     * @param userID
     * @return number of active challenge for the user.
     */

    public int getNumActiveChallenges(int userID) {
        String sql = "SELECT * FROM active_challenge WHERE userID = ?";
        try (Connection conn = databaseManager.connect()) {
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, userID);
                ResultSet rs = ps.executeQuery();
                System.out.println(rs.getFetchSize());
                return rs.getFetchSize();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * puts the challenge in the data base with a set name and description.
     */
    public void initaliseChallenge() {
        if (challengeExsists() == false) {
            insertChallenge("Variety Challenge", "Expand your palate with the variety challenge, " +
                    "this challenge encourages you to explore wines of different varieties, with wines of a range of " +
                    "colours, from different places and different grapes.");
        }
    }

    /**
     * inserts the wine into the challenge.
     */
    public void wineInChallenge() {
        if (challengeHasWines() == false) {
            insertWineChal(3, "Variety Challenge");
            insertWineChal(57, "Variety Challenge");
            insertWineChal(298, "Variety Challenge");
            insertWineChal(494, "Variety Challenge");
            insertWineChal(500, "Variety Challenge");
        }
    }

    /**
     * checks the user already has the challenge active, if not calls a method to update the database.
     * @param userID
     * @param cname
     */

    public void userActivatesChallenge(int userID, String cname) {
        if (!userHasChallenge(userID, cname)) {
            userToChallenge(userID, cname);
        } else {
            System.out.println("User already registered for this challenge");
        }
    }

    /**
     * gets the users id from the database.
     * @param currentUser
     * @return the user id.
     */

    public int getUId(User currentUser) {
        int uid;
        String uidSql = "SELECT id FROM user WHERE username = ? AND name = ?";
        try (Connection conn = databaseManager.connect()) {
            try (PreparedStatement uidPs = conn.prepareStatement(uidSql)) {
                uidPs.setString(1, currentUser.getEncryptedUserName());
                uidPs.setString(2, currentUser.getName());
                System.out.println(currentUser.getName());
                uid = uidPs.executeQuery().getInt(1);
                System.out.println(uid);
                return uid;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
//        return uid;
    }

    /**
     * gets the wines for the challenge, and returns them as array list of wines.
     * @param cname
     * @return ArrayList of wines for the challenge
     */

    public ArrayList<Wine> getWinesForChallenge(String cname) {
        ArrayList<Wine> wineList;
        String sql =    "SELECT id, wine_name, description, points, price, tag.name as tag_name, tag.type as tag_type\n" +
                "FROM (SELECT id, name as wine_name, description, points, price\n" +
                "      FROM wine\n" +
                "      JOIN challenge_wine ON wine.id = challenge_wine.wineID\n" +
                "WHERE challenge_wine.cname = ?)\n" +
                "JOIN owned_by ON id = owned_by.wid\n" +
                "JOIN tag ON owned_by.tname = tag.name\n" +
                "ORDER BY id;";

        try (
                Connection conn = databaseManager.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql);
        ) {
            pstmt.setString(1, cname);
            try (ResultSet rs = pstmt.executeQuery()) {
                wineList = processResultSetIntoWines(rs);
            }
            return wineList;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * processes the result set from the database into an array list of wines
     * @param resultSet
     * @return array list of wines
     * @throws SQLException
     */

    private ArrayList<Wine> processResultSetIntoWines(ResultSet resultSet) throws SQLException
    {
//        System.out.println("Start processing");

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
                    log.error("Tag type {} is not supported!", resultSet.getString("tag_type"));
            }
        }
        if (currentWineBuilder != null) {
            wineList.add(currentWineBuilder.build());
        }

        return wineList;
    }





//    public static void main(String[] args) {
//        ChallengeDAO cd = new ChallengeDAO();
//        cd.databaseManager.initialiseDB();
//        cd.wineInChallenge();
////        cd.userToChallenge(1, "Variety Challenge");
////        cd.wineInChallenge();
//    }



}
