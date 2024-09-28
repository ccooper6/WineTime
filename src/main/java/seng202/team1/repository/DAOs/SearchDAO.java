package seng202.team1.repository.DAOs;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
     * Searches the Database for wines whose name that match a given String.
     *
     * @param filterString {@link String} of the wine name to be filtered by. The SELECT statement
     *                                   will compare the wine name to '%filterString%'
     * @param limit The number of wines to select using {@link SearchDAO#UNLIMITED} for no limit
     * @return {@link ArrayList} of Wine objects for all wines that matched the given string
     */
    public ArrayList<Wine> searchWineByName(String filterString, int limit) {
        if (!Normalizer.isNormalized(filterString, Normalizer.Form.NFD)) {
            LOG.error("{} is not normalised!", filterString);
        }

        filterString = "%" + filterString + "%";

        String stmt = "SELECT id, wine_name, description, points, price, tag.name as tag_name, tag.type as tag_type\n"
                + "FROM (SELECT id, name AS wine_name, description, points, price\n"
                + "    FROM wine\n"
                + "    WHERE wine.normalised_name LIKE ?\n"
                + "    ORDER BY wine.id LIMIT ?)\n"
                + "JOIN owned_by ON id = owned_by.wid\n"
                + "JOIN tag ON owned_by.tname = tag.name\n"
                + "ORDER BY id;";

        ArrayList<Wine> wineList = new ArrayList<>();

        try (Connection conn = databaseManager.connect();
                PreparedStatement ps = conn.prepareStatement(stmt)) {
            ps.setString(1, filterString);
            ps.setInt(2, limit);

            try (ResultSet rs = ps.executeQuery()) {
                wineList = processResultSetIntoWines(rs);
            }
        } catch (SQLException e) {
            LOG.error(e.getMessage());
        }

        return wineList;
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
                LOG.error("{} is not normalised!", tag);
            }
        }
        // Build the SQL query with dynamic placeholders
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("SELECT id, wine.name as wine_name, description, price, tag.name as tag_name, tag.type as tag_type\n")
                .append("FROM (SELECT id as temp_id\n")
                .append("        FROM (SELECT id, count(wid) as c\n")
                .append("              FROM wine JOIN owned_by on wine.id = owned_by.wid\n")
                .append("                        JOIN tag on owned_by.tname = tag.name\n")
                .append("              WHERE tag.normalised_name IN (");

        // Add placeholders
        for (int i = 0; i < tagList.size(); i++) {
            if (i > 0) {
                sqlBuilder.append(",");
            }
            sqlBuilder.append("?");
        }
        sqlBuilder.append(")\n")
                .append("              GROUP BY wid)\n")
                .append("        WHERE c = ? LIMIT ?)\n")
                .append("JOIN wine on wine.id = temp_id\n")
                .append("JOIN owned_by on id = owned_by.wid\n")
                .append("JOIN tag on owned_by.tname = tag.name\n")
                .append("ORDER BY id;");

        ArrayList<Wine> wineList = new ArrayList<>();
        String sql = sqlBuilder.toString();

        // get results
        try (Connection conn = databaseManager.connect();
                PreparedStatement ps = conn.prepareStatement(sql) ) {
            for (int i = 0; i < tagList.size(); i++) {
                ps.setString(i + 1, tagList.get(i));
            }
            ps.setInt(tagList.size() + 1, tagList.size());
            ps.setInt(tagList.size() + 2, limit);
            try (ResultSet rs = ps.executeQuery()) {
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
    public ArrayList<Wine> reccWineByTags(ArrayList<String> tagsLiked, ArrayList<String> tagsToAvoid, ArrayList<Integer> wineIdToAvoid, int limit)
    {
        for (String tag : tagsLiked) {
            if (!Normalizer.isNormalized(tag, Normalizer.Form.NFD)) {
                LOG.error("{} is not normalised!", tag);
            }
        }

        StringBuilder sqlBuilder = new StringBuilder();
        initializeSqlReccString(sqlBuilder, tagsLiked, tagsToAvoid, wineIdToAvoid, limit);

        ArrayList<Wine> wineList = new ArrayList<>();
        String sql = sqlBuilder.toString();

        try (Connection conn = databaseManager.connect();
             PreparedStatement ps = conn.prepareStatement(sql) ) {
            setTagAndWineIDValueToPs(tagsLiked,tagsToAvoid, wineIdToAvoid, ps);
            ps.setInt(tagsToAvoid.size() + 1 + tagsLiked.size(), limit);
            LOG.info(ps);
            try (ResultSet rs = ps.executeQuery()) {
                wineList = processResultSetIntoWines(rs);
            }
        } catch (SQLException e) {
            LOG.error(e.getMessage());
        }
        return wineList;
    }

    /**
     * Sets the respective tag names and wine id to their respective slot in the prepared statement
     * @param tagsLiked an {@link ArrayList<String>} of liked tag names
     * @param tagsToAvoid an {@link ArrayList<String>} of disliked tag names
     * @param wineIdToAvoid an {@link ArrayList<Integer>} of wine id to avoid
     * @param ps the {@link PreparedStatement} to be executed
     * @throws SQLException
     */
    @SuppressWarnings("checkstyle:AbbreviationAsWordInName")
    private static void setTagAndWineIDValueToPs(ArrayList<String> tagsLiked, ArrayList<String> tagsToAvoid, ArrayList<Integer> wineIdToAvoid, PreparedStatement ps) throws SQLException {
        for (int i = 0; i < tagsLiked.size(); i++) {
            ps.setString(i + 1, tagsLiked.get(i));
        }
        if (!tagsToAvoid.isEmpty()) {
            for (int i = 0; i < tagsToAvoid.size(); i++) {
                ps.setString(i + 1 + tagsLiked.size(), tagsToAvoid.get(i));
            }
        }
        if (!wineIdToAvoid.isEmpty()) {
            for (int i = 0; i < wineIdToAvoid.size(); i++) {
                System.out.println(wineIdToAvoid.get(i));
                ps.setInt(tagsToAvoid.size() + tagsLiked.size() + 2 + i, wineIdToAvoid.get(i));
            }
        }
    }

    /**
     * Adds the required ? for the wine id to avoid
     * @param numOfWineToAvoid number of wine id to avoid
     * @param sqlBuilder the {@link StringBuilder} of the prepared statement
     */
    private static void addWineIdToAvoidToPs(int numOfWineToAvoid, StringBuilder sqlBuilder) {
        if (numOfWineToAvoid > 0) {
            sqlBuilder.append(" AND wine.id NOT IN (");
            for (int i = 0; i < numOfWineToAvoid; i++) {
                if (i > 0) {
                    sqlBuilder.append(",");
                }
                sqlBuilder.append("?");
            }
            sqlBuilder.append(")\n");
        }
    }

    /**
     * Adds the ? reserved for the tags to avoid in the search query
     * @param numTagsToAvoid the number of tags to avoid
     * @param sqlBuilder the PS string builder
     */
    private static void addTagsToAvoidToPs(int numTagsToAvoid, StringBuilder sqlBuilder) {
        sqlBuilder.append("      AND id NOT IN (SELECT id\n")
                .append("                       FROM wine JOIN owned_by on wine.id = owned_by.wid\n")
                .append("                                 JOIN tag on owned_by.tname = tag.name\n")
                .append("                       WHERE tag.name IN (");
        if (numTagsToAvoid > 0) {
            for (int i = 0; i < numTagsToAvoid; i++) {
                if (i > 0) {
                    sqlBuilder.append(",");
                }
                sqlBuilder.append("?");
            }
        } else {
            sqlBuilder.append("''");
        }
        sqlBuilder.append("))\n");
    }

    /**
     * Adds enough ? to the PS string builder to fit all the liked tags
     * @param numOfTagsLiked number of liked tags
     * @param sqlBuilder the {@link StringBuilder} for the prepared statement
     */
    private static void addLikedTagsToPs(int numOfTagsLiked, StringBuilder sqlBuilder) {
        sqlBuilder.append("      WHERE tag.name IN (");
        if (numOfTagsLiked == 0) {
            sqlBuilder.append("''");
        }
        for (int i = 0; i < numOfTagsLiked; i++) {
            if (i > 0) {
                sqlBuilder.append(",");
            }
            sqlBuilder.append("?");
        }
        sqlBuilder.append(")\n");
    }

    /**
     * Creates the recommendation sql prepared statement string
     * @param sqlBuilder the {@link StringBuilder} that builds the PS string.
     */
    private static void initializeSqlReccString(StringBuilder sqlBuilder, ArrayList<String> tagsLiked, ArrayList<String> dislikedTags, ArrayList<Integer> winesToAvoid, int limit) {
        sqlBuilder.append("SELECT id, wine.name as wine_name, description, price, tag.name as tag_name, tag.type as tag_type\n")
                .append("FROM (SELECT id as temp_id, count(id) as c\n")
                .append("      FROM wine JOIN owned_by on wine.id = owned_by.wid\n")
                .append("                JOIN tag on owned_by.tname = tag.name\n");
        addLikedTagsToPs(tagsLiked.size(),sqlBuilder);
        addTagsToAvoidToPs(dislikedTags.size(), sqlBuilder);
        sqlBuilder.append("""
                      GROUP BY temp_id
                      ORDER BY c DESC, random()
                      LIMIT ?)
                         JOIN wine ON wine.id = temp_id
                         JOIN owned_by ON wine.id = owned_by.wid""");
        addWineIdToAvoidToPs(winesToAvoid.size(), sqlBuilder);
        sqlBuilder.append("         JOIN tag ON owned_by.tname = tag.name;");
    }

    public static void main(String[] args) {
        SearchDAO searchDAO = new SearchDAO();
        ArrayList<String> tagToAvoid = new ArrayList<>();
        ArrayList<Integer> wineIdToAvoid = new ArrayList<>();
        tagToAvoid.add("2006");
        tagToAvoid.add("Red wine");
        wineIdToAvoid.add(26);
        wineIdToAvoid.add(100);
        wineIdToAvoid.add(79);
        ArrayList<String> likedTags = new ArrayList<>();
        likedTags.add("liked1");
        likedTags.add("liked2");
        likedTags.add("liked3");
        searchDAO.reccWineByTags(likedTags,tagToAvoid,wineIdToAvoid,50);
    }
}
