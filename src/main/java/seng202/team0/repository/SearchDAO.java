package seng202.team0.repository;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import seng202.team0.models.Wine;
import seng202.team0.models.WineBuilder;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.Normalizer;
import java.util.ArrayList;

public class SearchDAO {

    private static final Logger log = LogManager.getLogger(SearchDAO.class);

    private DatabaseManager databaseManager;

    private static SearchDAO instance;

    /**
     * Constructor class for SearchDAO.
     * <p>
     * Please use the static {@link SearchDAO#getInstance()} function instead.
     */
    public SearchDAO()
    {
        databaseManager = DatabaseManager.getInstance();
    }

    /**
     * Returns the instance for SearchDAO class.
     * <p>
     * If there is no instance, it will create one and fill the database manager variable
     *
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

                currentWineBuilder = WineBuilder.generaicSetup(resultSet.getInt("id"),
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
     * Searches the Database for wines whose name that match a given String
     *
     * @param filterString {@link String} of the wine name to be filtered by. The SELECT statement
     *                                   will compare the wine name to '%filterString%'
     * @return {@link ArrayList} of Wine objects for all wines that matched the given string
     */
    public ArrayList<Wine> searchWineByName (String filterString)
    {
        if (!Normalizer.isNormalized(filterString, Normalizer.Form.NFD)) {
            log.error("{} is not normalised!", filterString);
        }

        filterString = "%" + filterString + "%";

        String stmt = "SELECT id, wine_name, description, points, price, tag.name as tag_name, tag.type as tag_type\n" +
                "FROM (SELECT id, name AS wine_name, description, points, price\n" +
                "    FROM wine\n" +
                "    WHERE wine.normalised_name LIKE ?\n" +
                "    ORDER BY wine.id)\n" +
                "JOIN owned_by ON id = owned_by.wid\n" +
                "JOIN tag ON owned_by.tname = tag.name\n" +
                "ORDER BY id;";

        ArrayList<Wine> wineList = new ArrayList<>();

        try (Connection conn = databaseManager.connect();
             PreparedStatement ps = conn.prepareStatement(stmt) ) {
            ps.setString(1, filterString);

            try (ResultSet rs = ps.executeQuery()) {
                wineList = processResultSetIntoWines(rs);
            }
        } catch (SQLException e) {
            log.error(e.getMessage());
        }

        return wineList;
    }

    public ArrayList<Wine> searchWineByTags (ArrayList<String> tagList)
    {
//        select id, name from (select wine.id, wine.name, count(owned_by.wid) as c from wine
//            join owned_by on wine.id = owned_by.wid
//            join tag on owned_by.tname = tag.name
//            where tag.normalised_name = 'oregon' collate NOCASE
//            or tag.normalised_name = 'pinot noir' collate NOCASE
//            group by wine.id)
//        where c = 2;

        return null;
    }
}
