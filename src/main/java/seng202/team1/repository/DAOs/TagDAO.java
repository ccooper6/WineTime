package seng202.team1.repository.DAOs;

import com.sun.javafx.scene.shape.ArcHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import seng202.team1.models.User;
import seng202.team1.repository.DatabaseManager;

import javax.swing.text.html.HTML;
import java.lang.reflect.Array;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;

public class TagDAO {

    private static final Logger LOG = LogManager.getLogger(SearchDAO.class);
    private final DatabaseManager databaseManager;
    private static TagDAO instance;

    public TagDAO()
    {
        databaseManager = DatabaseManager.getInstance();
    }

    public static TagDAO getInstance()
    {
        if (instance == null) {
            instance = new TagDAO();
        }
        return instance;
    }

    public ArrayList<String> getVarieties() {
        ArrayList<String> varieties = new ArrayList<>();
        String sql = "SELECT name FROM tag WHERE type = ?";
        try (
                Connection conn = databaseManager.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql);
        ) {
            pstmt.setString(1, "Variety");
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                String variety = rs.getString("name");
                varieties.add(variety);
            }

            Collections.sort(varieties);
            return varieties;


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public ArrayList<String> getCountries() {
        ArrayList<String> countries = new ArrayList<>();
        String sql = "SELECT name FROM tag WHERE type = ?";
        try (
                Connection conn = databaseManager.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql);
        ) {
            pstmt.setString(1, "Country");
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                String variety = rs.getString("name");
                countries.add(variety);
            }

            Collections.sort(countries);
            return countries;


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public ArrayList<String> getWineries() {
        ArrayList<String> wineries = new ArrayList<>();
        String sql = "SELECT name FROM tag WHERE type = ?";
        try (
                Connection conn = databaseManager.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql);
        ) {
            pstmt.setString(1, "Winery");
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                String variety = rs.getString("name");
                wineries.add(variety);
            }

            Collections.sort(wineries);
            return wineries;


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Gets the min vintage in the wines
     * @return the min vintage
     */
    public int getMinVintage() {
        int minVintage = 0;
        String sql = "SELECT min(name) FROM tag WHERE type = ?";
        try (
                Connection conn = databaseManager.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql);
        ) {
            pstmt.setString(1, "Vintage");
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                minVintage = rs.getInt(1);
            }


            return minVintage;


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Gets the max vintage in the wines
     * @return the max vintage
     */
    public int getMaxVintage() {
        int minVintage = 0;
        String sql = "SELECT max(name) FROM tag WHERE type = ?";
        try (
                Connection conn = databaseManager.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql);
        ) {
            pstmt.setString(1, "Vintage");
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                minVintage = rs.getInt(1);
            }


            return minVintage;


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Gets the min point score in the wines
     * @return the min point score
     */
    public int getMinPoints() {
        int minPoints = 0;
        String sql = "SELECT min(points) FROM wine";
        try (
                Connection conn = databaseManager.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql);
        ) {
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                minPoints = rs.getInt(1);
            }


            return minPoints;


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Gets the max point score in the wines
     * @return the max point score
     */
    public int getMaxPoints() {
        int maxPoints = 0;
        String sql = "SELECT max(points) FROM wine";
        try (
                Connection conn = databaseManager.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql);
        ) {
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                maxPoints = rs.getInt(1);
            }


            return maxPoints;


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
