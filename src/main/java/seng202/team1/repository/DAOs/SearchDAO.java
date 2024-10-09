package seng202.team1.repository.DAOs;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import seng202.team1.models.TagType;
import seng202.team1.models.Wine;
import seng202.team1.models.WineBuilder;
import seng202.team1.repository.DatabaseManager;

import java.sql.*;
import java.text.Normalizer;
import java.util.ArrayList;

/**
 * Data Access Object for the Search Wines functionality.
 * Allows for searching by wine names or tags
 * @author Yuhao Zhang, Isaac Macdonald, Lydia Jackson
 */
public class SearchDAO {
    private static final Logger LOG = LogManager.getLogger(SearchDAO.class);
    private final DatabaseManager databaseManager;
    private static SearchDAO instance;
    /**
     * The upper limit for a search query, used to perform a search when no limit is needed.
     */
    public static final int UNLIMITED = 999999;

    /**
     * Constructor class for SearchDAO.
     *
     * <p>Please use the static {@link SearchDAO#getInstance()} function instead.
     */
    public SearchDAO()
    {
        databaseManager = DatabaseManager.getInstance();
    }

    /**
     * Returns the instance for SearchDAO class.
     *
     * <p>If there is no instance, it will create one and fill the database manager variable
     * @return {@link SearchDAO} the SearchDAO instance
     */
    public static SearchDAO getInstance()
    {
        if (instance == null) {
            instance = new SearchDAO();
        }
        return instance;
    }

    /**
     * Takes a result set of wines with its tags and process them into an ArrayList of wines.
     *
     * @param rs {@link ResultSet} the result set received after a SELECT statement in the
     *                                   database. Each row should contain the wine id, name, description
     *                                   and price and the tag name and type. Rows are seperated by tags.
     *                                   The result set must be ordered by wine id.
     * @return {@link ArrayList} of wines containing all wines in the result set
     * @throws SQLException when a column mentioned in result set is not provided.
     */
    public static ArrayList<Wine> processResultSetIntoWines(ResultSet rs) throws SQLException
    {
        ArrayList<Wine> wineList = new ArrayList<>();

        int currentID = -1;
        WineBuilder currentWineBuilder = null;

        while (rs.next())
        {

            if (rs.getInt("id") != currentID) {
                if (currentWineBuilder != null) {
                    wineList.add(currentWineBuilder.build());
                }

                currentWineBuilder = WineBuilder.genericSetup(rs.getInt("id"),
                        rs.getString("wine_name"),
                        rs.getString("description"),
                        rs.getInt("price"),
                        rs.getInt("points"));

                currentID = rs.getInt("id");
            }

            if (currentWineBuilder == null) {
                throw new NullPointerException("Current Wine Builder is null!");
            }

            TagType tagType = TagType.fromString(rs.getString("tag_type"));
            currentWineBuilder.setTag(tagType, rs.getString("tag_name"));
        }
        if (currentWineBuilder != null) {
            wineList.add(currentWineBuilder.build());
        }

        return wineList;
    }

    /**
     * Dynamically builds the string used for searching for wines by tags given a variable amount of tags
     * @param numTags The number of tags the SQL statement should support
     * @return The SQL query as a string
     */
    private String buildSearchByTagsString(int numTags)
    {
        // Build the SQL query with dynamic placeholders
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("""
                    SELECT id, wine.name as wine_name, description, points, price, tag.name as tag_name, tag.type as tag_type
                    FROM (SELECT id as temp_id
                            FROM (SELECT id, count(wid) as c
                                  FROM wine JOIN owned_by on wine.id = owned_by.wid
                                            JOIN tag on owned_by.tname = tag.name
                                  WHERE tag.normalised_name IN (""");

        // Add placeholders
        for (int i = 0; i < numTags; i++) {
            if (i > 0) {
                sqlBuilder.append(",");
            }
            sqlBuilder.append("?");
        }
        sqlBuilder.append("""
                    )
                                  GROUP BY wid)
                            WHERE c = ? LIMIT ?)
                    JOIN wine on wine.id = temp_id
                    JOIN owned_by on id = owned_by.wid
                    JOIN tag on owned_by.tname = tag.name
                    ORDER BY id;""");

        return sqlBuilder.toString();
    }

    /**
     * Searches for wines given a String of tags.
     *
     * @param tagList {@link String} of tag names seperated by commas. Must be normalised and lower case.
     * @param limit The number of wines to select using {@link SearchDAO#UNLIMITED} for no limit
     * @return {@link ArrayList} of Wine objects for all wines that matched the given string
     */
    public ArrayList<Wine> searchWineByTags(ArrayList<String> tagList, int limit)
    {
        for (String tag : tagList) {
            if (!Normalizer.isNormalized(tag, Normalizer.Form.NFD)) {
                LOG.warn("Error: Tag {} is not normalised!", tag);
            }
        }

        ArrayList<Wine> wineList = new ArrayList<>();
        String sql = buildSearchByTagsString(tagList.size());

        // get results
        try (Connection conn = databaseManager.connect();
             PreparedStatement searchPS = conn.prepareStatement(sql)) {
            for (int i = 0; i < tagList.size(); i++) {
                searchPS.setString(i + 1, tagList.get(i));
            }
            searchPS.setInt(tagList.size() + 1, tagList.size());
            searchPS.setInt(tagList.size() + 2, limit);
            try (ResultSet rs = searchPS.executeQuery()) {
                wineList = processResultSetIntoWines(rs);
            }
        } catch (SQLException e) {
            LOG.error("Error: Could not perform search for wines by tags, {}", e.getMessage());
        }
        return wineList;
    }

    /**
     * Builds the SQL query required to search by tags and filter
     *
     * @param numTags the number of tags to accommodate for
     * @return A string of the correct SQL statement
     */
    private String buildSearchByFilterString(int numTags)
    {
        // Build the SQL query with dynamic placeholders
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("""
                    SELECT id, wine.name as wine_name, description, points, price, tag.name as tag_name, tag.type as tag_type
                    FROM (SELECT id as temp_id
                          FROM (SELECT id, count(wid) as c
                              FROM wine JOIN owned_by on wine.id = owned_by.wid
                              JOIN tag on owned_by.tname = tag.name
                          WHERE tag.normalised_name IN (""");

        // Add placeholders
        for (int i = 0; i < numTags; i++) {
            if (i > 0) {
                sqlBuilder.append(",");
            }
            sqlBuilder.append("?");
        }

        sqlBuilder.append("""
                    )
                                  OR CASE WHEN tag.type = 'Vintage' THEN CAST(tag.normalised_name AS UNSIGNED) END BETWEEN ? AND ?
                                  GROUP BY wid)
                            WHERE (c = ?) or (c = ? AND NOT EXISTS (SELECT * FROM owned_by join tag on owned_by.tname = tag.name where owned_by.wid = temp_id and tag.type = 'Vintage')))
                    JOIN wine on wine.id = temp_id
                    JOIN owned_by on id = owned_by.wid
                    JOIN tag on owned_by.tname = tag.name
                    WHERE points >= ? AND points <= ?
                    AND wine_name like ?;""");


        return sqlBuilder.toString();
    }

    /**
     * Searches for wines given a String of tags.
     *
     * @param tagList      {@link String} of tag names seperated by commas. Must be normalised and lower case.
     * @param lowerPoints  the lowest amount of points that a wine can have.
     * @param upperPoints  the highest amounts of points that a wine can have.
     * @param lowerVintage the lowest vintage a wine can have.
     * @param upperVintage the highest vintage a wine can have.
     * @param filterString the string that must match to a wines title.
     * @param orderBy      the column to order the final wine objects by
     * @return {@link ArrayList} of Wine objects for all wines that matched the given string
     */
    public ArrayList<Wine> searchWineByTagsAndFilter(ArrayList<String> tagList, int lowerPoints, int upperPoints, int lowerVintage, int upperVintage, String filterString, String orderBy)
    {
        for (String tag : tagList) {
            if (!Normalizer.isNormalized(tag, Normalizer.Form.NFD)) {
                LOG.error("{} is not normalised!", tag);
            }
        }

        ArrayList<Wine> wineList = new ArrayList<>();
        String sql = buildSearchByFilterString(tagList.size());

        // get results
        try (Connection conn = databaseManager.connect();
             PreparedStatement searchPS = conn.prepareStatement(sql)) {
            for (int i = 0; i < tagList.size(); i++) {
                searchPS.setString(i + 1, tagList.get(i));
            }
            searchPS.setInt(tagList.size() + 1, lowerVintage);
            searchPS.setInt(tagList.size() + 2, upperVintage);
            searchPS.setInt(tagList.size() + 3, tagList.size() + 1);
            searchPS.setInt(tagList.size() + 4, tagList.size());
            searchPS.setInt(tagList.size() + 5, lowerPoints);
            searchPS.setInt(tagList.size() + 6, upperPoints);
            searchPS.setString(tagList.size() + 7, '%' + filterString + '%');

            try (ResultSet rs = searchPS.executeQuery()) {
                wineList = processResultSetIntoWines(rs);
            }
        } catch (SQLException e) {
            LOG.error(e.getMessage());
        }
        return wineList;
    }

    /**
     * Searches for wines given a String of tags. It will aim to avoid wines with tags that the user has disliked and the wines
     * that have already been reviewed, to avoid recommending wines that the user has already tried. It will sort the wines by
     * number of tags matched
     *
     * @param tagsLiked An {@link ArrayList<String>} of the liked tag names
     * @param tagsToAvoid An {@link ArrayList<String>} of tag names to avoid.
     * @param wineIdToAvoid An {@link ArrayList<Integer>} of the wine ids that have already been added to the recommended list.
     * @param limit The number of wines to select using {@link SearchDAO#UNLIMITED} for no limit
     * @return {@link ArrayList} of Wine objects for all wines that matched the given condition
     */
    public ArrayList<Wine> getRecommendedWines(ArrayList<String> tagsLiked, ArrayList<String> tagsToAvoid, ArrayList<Integer> wineIdToAvoid, int limit)
    {
        for (String tag : tagsLiked) {
            if (!Normalizer.isNormalized(tag, Normalizer.Form.NFD)) {
                LOG.warn("Error: Tag {} is not normalised!", tag);
            }
        }

        StringBuilder sqlBuilder = new StringBuilder();
        initializeSqlRecommendedString(sqlBuilder, tagsLiked, tagsToAvoid, wineIdToAvoid);

        ArrayList<Wine> wineList = new ArrayList<>();
        String sql = sqlBuilder.toString();
        try (Connection conn = databaseManager.connect();
             PreparedStatement searchPS = conn.prepareStatement(sql)) {
            setTagAndWineIDValueToPs(tagsLiked,tagsToAvoid, wineIdToAvoid, searchPS);
            searchPS.setInt(tagsToAvoid.size() + 1 + tagsLiked.size() + wineIdToAvoid.size(), limit);

            try (ResultSet rs = searchPS.executeQuery()) {
                wineList = processResultSetIntoWines(rs);
            }
        } catch (SQLException e) {
            LOG.error("Could not perform search for recommended wines, {}", e.getMessage());
        }
        return wineList;
    }

    /**
     * Sets the respective tag names and wine id to their respective slot in the prepared statement
     * @param tagsLiked an {@link ArrayList<String>} of liked tag names
     * @param tagsToAvoid an {@link ArrayList<String>} of disliked tag names
     * @param wineIdToAvoid an {@link ArrayList<Integer>} of wine id to avoid
     * @param searchPS the {@link PreparedStatement} to be executed
     * @throws SQLException when values cannot be set in the prepared statement
     */
    @SuppressWarnings("checkstyle:AbbreviationAsWordInName")
    private static void setTagAndWineIDValueToPs(ArrayList<String> tagsLiked, ArrayList<String> tagsToAvoid, ArrayList<Integer> wineIdToAvoid, PreparedStatement searchPS) throws SQLException {
        for (int i = 0; i < tagsLiked.size(); i++) {
            searchPS.setString(i + 1, tagsLiked.get(i));
        }
        if (!tagsToAvoid.isEmpty()) {
            for (int i = 0; i < tagsToAvoid.size(); i++) {
                searchPS.setString(i + 1 + tagsLiked.size(), tagsToAvoid.get(i));
            }
        }
        if (!wineIdToAvoid.isEmpty()) {
            for (int i = 0; i < wineIdToAvoid.size(); i++) {
                searchPS.setInt(tagsToAvoid.size() + tagsLiked.size() + 1 + i, wineIdToAvoid.get(i));
            }
        }
    }

    /**
     * Adds the required ? for the wine id to avoid
     * @param numOfWineToAvoid number of wine id to avoid
     * @param sqlBuilder the {@link StringBuilder} of the prepared statement
     */
    private static void addWineIdToAvoidToPs(int numOfWineToAvoid, StringBuilder sqlBuilder) {
        sqlBuilder.append(" AND id NOT IN (");
        if (numOfWineToAvoid > 0) {
            for (int i = 0; i < numOfWineToAvoid; i++) {
                if (i > 0) {
                    sqlBuilder.append(",");
                }
                sqlBuilder.append("?");
            }
        }
        sqlBuilder.append(")\n");
    }

    /**
     * Adds the ? reserved for the tags to avoid in the search query
     * @param numTagsToAvoid the number of tags to avoid
     * @param sqlBuilder the PS string builder
     */
    private static void addTagsToAvoidToPs(int numTagsToAvoid, StringBuilder sqlBuilder) {
        sqlBuilder.append("""
                      AND id NOT IN (SELECT id
                                     FROM wine JOIN owned_by on wine.id = owned_by.wid
                                               JOIN tag on owned_by.tname = tag.name
                                     WHERE tag.name IN (""");

        addTag(sqlBuilder, numTagsToAvoid);

        sqlBuilder.append("))");
    }

    /**
     * Adds enough ? to the PS string builder to fit all the liked tags
     * @param numOfTagsLiked number of liked tags
     * @param sqlBuilder the {@link StringBuilder} for the prepared statement
     */
    private static void addLikedTagsToPs(int numOfTagsLiked, StringBuilder sqlBuilder) {
        sqlBuilder.append("      WHERE tag.name IN (");

        addTag(sqlBuilder, numOfTagsLiked);

        sqlBuilder.append(")\n");
    }

    /**
     * Adds correct number of parameters to a sql query.
     *
     * @param sqlBuilder The StringBuilder to build onto
     * @param numTags The number of parameters to add
     */
    private static void addTag(StringBuilder sqlBuilder, int numTags)
    {
        for (int i = 0; i < numTags; i++) {
            if (i > 0) {
                sqlBuilder.append(",");
            }
            sqlBuilder.append("?");
        }
    }

    /**
     * Creates the recommendation sql prepared statement string
     * @param sqlBuilder the {@link StringBuilder} that builds the PS string.
     */
    private static void initializeSqlRecommendedString(StringBuilder sqlBuilder, ArrayList<String> tagsLiked, ArrayList<String> dislikedTags, ArrayList<Integer> winesToAvoid) {
        sqlBuilder.append("""
                    SELECT id, wine.name as wine_name, description, points, price, tag.name as tag_name, tag.type as tag_type
                    FROM (SELECT id as temp_id, count(id) as c
                          FROM wine JOIN owned_by on wine.id = owned_by.wid
                                    JOIN tag on owned_by.tname = tag.name
                    """);
        addLikedTagsToPs(tagsLiked.size(),sqlBuilder);
        addTagsToAvoidToPs(dislikedTags.size(), sqlBuilder);
        addWineIdToAvoidToPs(winesToAvoid.size(), sqlBuilder);
        sqlBuilder.append("""
                    GROUP BY temp_id
                    ORDER BY c DESC, random()
                    LIMIT ?)
                     JOIN wine ON wine.id = temp_id
                     JOIN owned_by ON wine.id = owned_by.wid
                    JOIN tag ON owned_by.tname = tag.name;""");
    }

    /**
     * Checks the existence of a wine in the database.
     * @param wineID the id of the wine in question
     * @return true if present in wine table
     */
    public boolean checkWineExists(int wineID) {
        String sql = "SELECT id FROM wine WHERE wine.id = ?";
        try (Connection conn = DatabaseManager.getInstance().connect();
             PreparedStatement wishlistPS = conn.prepareStatement(sql)) {
            wishlistPS.setInt(1, wineID);
            try (ResultSet rs = wishlistPS.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            LOG.error("Error: Could not check if wine exists, {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /**
     * Returns a specific wine object using the specified wine id.
     * @param wid the wine id
     * @return a {@link Wine} object
     */
    public Wine getWine(int wid) {
        String sql = "SELECT id, wine.name as wine_name, description, points, price, tag.type as tag_type, tag.name as tag_name FROM wine "
                + "JOIN owned_by ON id = owned_by.wid "
                + "JOIN tag ON owned_by.tname = tag.name WHERE wine.id = ?;";
        WineBuilder wineBuilder = null;
        try (Connection conn = databaseManager.connect();
             PreparedStatement loggingPS = conn.prepareStatement(sql)) {
            loggingPS.setInt(1, wid);
            try (ResultSet rs = loggingPS.executeQuery()) {
                while (rs.next()) {
                    if (wineBuilder == null) {
                        wineBuilder = WineBuilder.genericSetup(rs.getInt("id"),
                                rs.getString("wine_name"),
                                rs.getString("description"),
                                rs.getInt("price"),
                                rs.getInt("points"));
                    }

                    TagType tagType = TagType.fromString(rs.getString("tag_type"));
                    wineBuilder.setTag(tagType, rs.getString("tag_name"));
                }

                if (wineBuilder == null) {
                    throw new NullPointerException("Fetched wine does not exist");
                }

                return wineBuilder.build();
            }
        } catch (SQLException e) {
            LOG.error("Error: Could not get wine, {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
