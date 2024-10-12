package seng202.team1.repository.DAOs;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import seng202.team1.models.TagType;
import seng202.team1.repository.DatabaseManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;


/**
 * Data Access Object for the search/sorting wine tags.
 */
public class TagDAO {

    private static final Logger LOG = LogManager.getLogger(SearchDAO.class);
    private final DatabaseManager databaseManager;
    private static TagDAO instance;

    /**
     * Constructor class for TagDAO.
     */
    public TagDAO()
    {
        databaseManager = DatabaseManager.getInstance();
    }

    /**
     * Returns the instance for TagDAO class.
     * <p>If there is no instance, it will create one and fill the database manager variable
     * @return {@link TagDAO} the TagDA0 instance
     */
    public static TagDAO getInstance()
    {
        if (instance == null) {
            instance = new TagDAO();
        }
        return instance;
    }

    /**
     * gets the names of wine varieties.
     * @return {@link ArrayList<String>} names of wine varieties
     */
    public ArrayList<String> getVarieties() {
        String sql = "SELECT name FROM tag WHERE type = ?";

        return getStringTags(sql, TagType.VARIETY);
    }


    /**
     * gets arraylist of names of countries.
     * @return {@link ArrayList<String>} names of countries
     */
    public ArrayList<String> getCountries() {
        String sql = "SELECT name FROM tag WHERE type = ?";

        return getStringTags(sql, TagType.COUNTRY);
    }

    /**
     * gets arraylist of names of wineries
     * @return {@link ArrayList<String>} of names of wineries.
     */
    public ArrayList<String> getWineries() {
        String sql = "SELECT name FROM tag WHERE type = ?";

        return getStringTags(sql, TagType.WINERY);
    }

    /**
     * Fetches a list of all tag names that matches a tag type stored in the database
     * @param sql The sql query to execute with parameters for tag type
     * @param type The type of tag to search for
     * @return An {@link ArrayList<String>} of all tags with the matching type
     */
    private ArrayList<String> getStringTags(String sql, TagType type)
    {
        ArrayList<String> results = new ArrayList<>();

        try (Connection conn = databaseManager.connect();
             PreparedStatement tagPS = conn.prepareStatement(sql)) {
            tagPS.setString(1, TagType.toString(type));
            ResultSet rs = tagPS.executeQuery();

            while (rs.next()) {
                String tag = rs.getString("name");
                results.add(tag);
            }

            Collections.sort(results);
            return results;
        } catch (SQLException e) {
            LOG.error("Error: Could not get {} tags, {}", type, e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /**
     * Gets the min vintage in the wines
     * @return the lowest value for wine vintage in database.
     */
    public int getMinVintage() {
        String sql = "SELECT min(name) FROM tag WHERE type = ? AND name != 'null'";

        return getIntTag(sql, TagType.VINTAGE);
    }

    /**
     * Gets the max vintage in the wines
     * @return the highest value for wine vintage in database
     */
    public int getMaxVintage() {
        String sql = "SELECT max(name) FROM tag WHERE type = ? AND name != 'null';";

        return getIntTag(sql, TagType.VINTAGE);
    }

    /**
     * Gets the min point score in the wines
     * @return the minimum point score for wine in database
     */
    public int getMinPoints() {
        String sql = "SELECT min(points) FROM wine;";

        return getIntTag(sql, TagType.POINTS);
    }

    /**
     * Gets the max point score in the wines
     * @return the maximum point score for wine in database
     */
    public int getMaxPoints() {
        String sql = "SELECT max(points) FROM wine";

        return getIntTag(sql, TagType.POINTS);
    }

    /**
     * Gets the min price in the wines
     * @return the lowest price of wine stored in the database
     */
    public int getMinPrice() {
        String sql = "SELECT min(price) FROM wine;";

        return getIntTag(sql, TagType.PRICE);
    }

    /**
     * Gets the max price in the wines
     * @return the highest price of wine stored in the database
     */
    public int getMaxPrice() {
        String sql = "SELECT max(price) FROM wine";

        return getIntTag(sql, TagType.PRICE);
    }

    /**
     * Returns a tag with an integer name
     * @param sql the string sql query to fetch the tag
     * @param type the type of tag it is
     * @return an integer tag name
     */
    private int getIntTag(String sql, TagType type)
    {
        try (Connection conn = databaseManager.connect();
             PreparedStatement tagPS = conn.prepareStatement(sql)) {
            if (type == TagType.VINTAGE) {
                tagPS.setString(1, TagType.toString(type));
            }

            ResultSet rs = tagPS.executeQuery();

            if (rs.next()) {
                return rs.getInt(1);
            } else {
                throw new RuntimeException("Tag " + type + " not found");
            }
        } catch (SQLException e) {
            LOG.error("Error: Could not get {} tag, {}", type, e.getMessage());
            throw new RuntimeException(e);
        }
    }


    /**
     * This method takes the url of a wine varieties text file and puts all of them in a set.
     * @param url the filepath of the text file.
     * @param variety the set to put the lines of the text file in.
     */
    public void addWineToSet(InputStream url, HashSet<String> variety) {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(url));
            String varietyName;
            while ((varietyName = br.readLine()) != null) {
                variety.add(varietyName);
            }
        } catch (IOException e ) {
            LOG.error("Error: Could not add wines from text file, {}", e.getMessage());
        }

    }
}
