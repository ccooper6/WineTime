package seng202.team1.repository.DAOs;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import seng202.team1.models.Wine;
import seng202.team1.models.WineBuilder;
import seng202.team1.repository.DatabaseManager;
import seng202.team1.services.SearchWineService;

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
        //boolean sortDirection = SearchWineService.getInstance().getSortDirection();
        filterString = "%" + filterString + "%";

        String stmt = "SELECT id, wine_name, description, points, price, tag.name as tag_name, tag.type as tag_type\n"
                + "FROM (SELECT id, name AS wine_name, description, points, price\n"
                + "    FROM wine\n"
                + "    WHERE wine.normalised_name LIKE ?\n"
                + "    ORDER BY wine.id LIMIT ?)\n"
                + "JOIN owned_by ON id = owned_by.wid\n"
                + "JOIN tag ON owned_by.tname = tag.name\n"
                + "ORDER BY id ;";
        //"+ (sortDirection ? "ASC" : "DESC") + "

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
    public ArrayList<Wine> searchByNameAndFilter(ArrayList<String> varietyLocationWinery, int lowerPoints, int upperPoints, int lowerVintage, int upperVintage, String filterString, int limit){
        {
            for (String tag : varietyLocationWinery) {
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
                    .append("              WHERE 1=1\n"); //in case no filters

            // Name
            if(filterString != null) {
                sqlBuilder.append(" AND wine.normalised_name LIKE ?\n");
            }

            // Variety
            if(!varietyLocationWinery.isEmpty()) {
                sqlBuilder.append(" AND tag.normalised_name IN (");
                for (int i = 0; i < varietyLocationWinery.size(); i++) {
                    if (i > 0) {
                        sqlBuilder.append(",");
                    }
                    sqlBuilder.append("?");
                }
                sqlBuilder.append(")\n");
            }

            //Points range
            sqlBuilder.append(" AND wine.points BETWEEN ? AND ? \n");

            //Vintage range
            sqlBuilder.append(" AND tag.type = 'Vintage' AND tag.normalised_name BETWEEN ? AND ? \n");

            sqlBuilder.append("              GROUP BY wid)\n")
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
                int z = 1;
                if(filterString != null) {
                    ps.setString(z, filterString);
                    z ++;
                }
                for (int i = 0; i < varietyLocationWinery.size(); i++) {
                    ps.setString(z, varietyLocationWinery.get(i));
                    z++;
                }
                ps.setInt(z, lowerPoints); z++;
                ps.setInt(z, upperPoints); z++;
                ps.setString(z, String.valueOf(lowerVintage)); z++;
                ps.setString(z, String.valueOf(upperVintage)); z++;
                ps.setInt(z, varietyLocationWinery.size()); z++;
                ps.setInt(z, limit);
                try (ResultSet rs = ps.executeQuery()) {
                    wineList = processResultSetIntoWines(rs);
                }
            } catch (SQLException e) {
                LOG.error(e.getMessage());
            }
            return wineList;
        }
    }
}
