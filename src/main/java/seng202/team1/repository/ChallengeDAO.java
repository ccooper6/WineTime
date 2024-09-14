package seng202.team1.repository;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import seng202.team1.models.User;
import seng202.team1.services.ChallengeService;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;



public class ChallengeDAO {

    private static final Logger log = LogManager.getLogger(LogWineDao.class);

    private final DatabaseManager databaseManager = DatabaseManager.getInstance();

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

    public void insertWineChal(int wineID, String cname) {
        String sql = "INSERT INTO challenge_wine (wineID, cname) VALUES (?, ?)";
        try (Connection conn = databaseManager.connect()) {
            try (PreparedStatement chalps = conn.prepareStatement(sql)) {
                chalps.setInt(1, wineID);
                chalps.setString(2, cname);
                chalps.executeUpdate();
            }
        } catch (SQLException e) {
            log.error(e.getMessage());
        }

    }

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

    public void initaliseChallenge() {
        if (challengeExsists() == false) {
            insertChallenge("Variety Challenge", "Expand your palate with the variety challenge, " +
                    "this challenge encourages you to explore wines of different varieties, with wines of a range of " +
                    "colours, from different places and different grapes.");
        }
    }

    public void wineInChallenge() {
        if (challengeHasWines() == false) {
            insertWineChal(3, "Variety Challenge");
            insertWineChal(57, "Variety Challenge");
            insertWineChal(298, "Variety Challenge");
            insertWineChal(494, "Variety Challenge");
            insertWineChal(500, "Variety Challenge");
        }
    }

    public void userActivatesChallenge(int userID, String cname) {
        if (!userHasChallenge(userID, cname)) {
            userToChallenge(userID, cname);
        } else {
            System.out.println("User alreay registered for this challenge");
        }
    }


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





    public static void main(String[] args) {
        ChallengeDAO cd = new ChallengeDAO();
        cd.databaseManager.initialiseDB();
//        cd.userToChallenge(1, "Variety Challenge");
//        cd.wineInChallenge();
    }



}
