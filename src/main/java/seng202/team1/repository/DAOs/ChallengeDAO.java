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
 * Data Access Object for the challenge tracker functionality,
 * updates the database with information on user and challenge, and gets information
 * from the database.
 * @author Lydia Jackson, Wen Sheng Thong
 */
// TODO LOG after update
public class ChallengeDAO {
    private static final Logger LOG = LogManager.getLogger(ChallengeDAO.class);
    private final DatabaseManager databaseManager = DatabaseManager.getInstance();

    /**
     * Inserts challenge wine into the database.
     * @param wineID wine id
     * @param cname challenge name
     */
    public void insertChallenge(int wineID, int uid, String cname) {
        String sql = "INSERT INTO challenge_wine (wineID, cname, uid) VALUES (?, ?, ?)";
        try (Connection conn = databaseManager.connect()) {
            try (PreparedStatement challengePS = conn.prepareStatement(sql)) {
                challengePS.setInt(1, wineID);
                challengePS.setString(2, cname);
                challengePS.setInt(3, uid);
                challengePS.executeUpdate();
            }
        } catch (SQLException e) {
            LOG.error("Error: Could not insert challenge, {}", e.getMessage());
        }
    }

    /**
     * Inserts the user and challenge into active challenges in database.
     * @param uid user id
     * @param cname challenge name
     */
    public void startChallenge(int uid, String cname) {
        String sql = "INSERT INTO active_challenge (userID, cname) VALUES (?, ?)";
        try (Connection conn = databaseManager.connect()) {
            try (PreparedStatement challengePS = conn.prepareStatement(sql)) {
                challengePS.setInt(1, uid);
                challengePS.setString(2, cname);
                challengePS.executeUpdate();
            }
        } catch (SQLException e) {
            LOG.error("Error: Could not start challenge for user, {}", e.getMessage());
        }
    }

    /**
     * Checks to see if there are wines for the challenge in database.
     * @return boolean if wine in challenge_wine.
     */
    private Boolean challengeHasWines(String cname, int uid) {
        String sql = "SELECT * FROM challenge_wine WHERE cname = ? AND uid = ?";
        try (Connection conn = databaseManager.connect()) {
            try (PreparedStatement challengePS = conn.prepareStatement(sql)) {
                challengePS.setString(1, cname);
                challengePS.setInt(2, uid);
                ResultSet rs = challengePS.executeQuery();
                return rs.next();
            }
        } catch (SQLException e) {
            LOG.error("Error: Could not get challenge wines for user, {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /**
     * Checks if the user has a challenge assigned to them.
     * @param uid user id
     * @param cname challenge name
     * @return boolean if the user has started a challenge.
     */
    private Boolean userHasChallenge(int uid, String cname) {
        String sql = "SELECT * FROM active_challenge WHERE userID = ? AND cname = ?";
        try (Connection conn = databaseManager.connect()) {
            try (PreparedStatement challengePS = conn.prepareStatement(sql)) {
                challengePS.setInt(1, uid);
                challengePS.setString(2, cname);
                ResultSet rs = challengePS.executeQuery();
                return rs.next();
            }
        } catch (SQLException e) {
            LOG.error("Could not check if user is participating in challenge, {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /**
     * Gets the name of the active challenge for the user.
     * @param uid user id
     * @return challenge name
     */
    // TODO only one challenge?
    public String getChallengeForUser(int uid) {
        String sql = "SELECT * FROM active_challenge WHERE userID = ?";
        try (Connection conn = databaseManager.connect()) {
            try (PreparedStatement challengePS = conn.prepareStatement(sql)) {
                challengePS.setInt(1, uid);
                ResultSet rs = challengePS.executeQuery();
                return rs.getString("cname");
            }
        } catch (SQLException e) {
            LOG.error("Error: Could not get challenges for user, {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /**
     * Inserts the wine into the challenge.
     * @param wineIds an array list of integer wine id
     * @param challengeName the name of the challenge
     * @param uid current user uid
     */
    public void addWineToChallenge(ArrayList<Integer> wineIds, int uid, String challengeName) {
        if (!challengeHasWines(challengeName, uid)) {
            for (Integer wineId : wineIds) {
                insertChallenge(wineId, uid, challengeName);
            }
        }
    }

    /**
     * Checks the user already has the challenge active, if not calls a method to update the database.
     * @param uid user id
     * @param cname challenge name
     */
    public void userActivatesChallenge(int uid, String cname, ArrayList<Integer> wineIds) {
        // add challenge if user does not already have this challenge already
        if (!userHasChallenge(uid, cname)) {
            startChallenge(uid, cname);
            addWineToChallenge(wineIds, uid, cname);
        } else {
            LOG.error("Error: Could not activate challenge for user since user has already started that challenge");
        }
    }

    /**
     * Gets the wines for the challenge, and returns them as array list of wines.
     * @param cname challenge name
     * @param uid the user id
     * @return ArrayList of wines for the challenge
     */
    public ArrayList<Wine> getWinesInChallenge(String cname, int uid) {
        ArrayList<Wine> wineList;
        String sql = """
                   SELECT id, wine_name, description, points, price, tag.name as tag_name, tag.type as tag_type
                   FROM (SELECT id, name as wine_name, description, points, price
                         FROM wine
                         JOIN challenge_wine ON wine.id = challenge_wine.wineID
                   WHERE challenge_wine.cname = ?
                   AND challenge_wine.uid = ?)
                   JOIN owned_by ON id = owned_by.wid
                   JOIN tag ON owned_by.tname = tag.name
                   ORDER BY id;""";

        try (Connection conn = databaseManager.connect();
             PreparedStatement challengePS = conn.prepareStatement(sql)) {
            challengePS.setString(1, cname);
            challengePS.setInt(2, uid);
            try (ResultSet rs = challengePS.executeQuery()) {
                wineList = SearchDAO.processResultSetIntoWines(rs);
            }
            return wineList;
        } catch (SQLException e) {
            LOG.error("Error: Could not get wines for challenge by user, {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /**
     * removes the challenge from the users active challenges.
     * @param uid users id
     * @param cname the name of the challenge
     */
    public void challengeCompleted(int uid, String cname)
    {
        String activeChallengeSQL = "DELETE FROM active_challenge WHERE userID = ? AND cname = ?;";
        try (Connection conn = databaseManager.connect()) {
            try (PreparedStatement challengePS = conn.prepareStatement(activeChallengeSQL)) {
                challengePS.setInt(1, uid);
                challengePS.setString(2, cname);
                challengePS.executeUpdate();
            }
        } catch (SQLException e) {
            LOG.error("Error: Could not remove challenge from user, {}", e.getMessage());
            throw new RuntimeException(e);
        }
        String challengeWineSQL = "DELETE FROM challenge_wine WHERE cname = ? AND uid = ?;";
        try (Connection conn = databaseManager.connect()) {
            try (PreparedStatement challengePS = conn.prepareStatement(challengeWineSQL)) {
                challengePS.setString(1, cname);
                challengePS.setInt(2, uid);
                challengePS.executeUpdate();
            }
        } catch (SQLException e) {
            LOG.error("Error: Could not remove wines from challenge for user, {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
