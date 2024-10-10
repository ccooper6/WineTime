package seng202.team1.unittests.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import seng202.team1.exceptions.InstanceAlreadyExistsException;
import seng202.team1.models.Wine;
import seng202.team1.repository.DAOs.LogWineDao;
import seng202.team1.repository.DAOs.SearchDAO;
import seng202.team1.repository.DatabaseManager;
import seng202.team1.services.RecommendWineService;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * The set of tests that covers {@link RecommendWineService} and
 * {@link seng202.team1.repository.DAOs.SearchDAO#getRecommendedWines(ArrayList, ArrayList, ArrayList, int)}
 *
 * @author Wen Sheng Thong
 */
public class RecommendWineServiceTest {
    static DatabaseManager databaseManager;
    static LogWineDao logWineDao;
    static RecommendWineService recommendWineService;

    /**
     * Sets up {@link DatabaseManager} instance to use the test database
     *
     * @throws InstanceAlreadyExistsException If {@link DatabaseManager#REMOVE_INSTANCE()} does not remove the instance
     */
    @BeforeEach
    public void setUp() throws InstanceAlreadyExistsException
    {
        DatabaseManager.REMOVE_INSTANCE();
        databaseManager = DatabaseManager.initialiseInstanceWithUrl("jdbc:sqlite:./src/test/resources/test_database.db");
        recommendWineService = new RecommendWineService();
        logWineDao = new LogWineDao();
        DatabaseManager.getInstance().forceReset();
    }

    /**
     * Check databaseManager exists
     */
    @Test
    void testDBConnection() {
        assertNotNull(databaseManager);
    }

    /**
     * Gets all the tags belonging to the wine
     * @param wine wine object
     * @return array list of tag belong to the wine
     */
    private ArrayList<String> getWineTags(Wine wine) {
        ArrayList<String> wineTags = new ArrayList<>();
        String psString = """
                SELECT tag.name
                FROM wine JOIN owned_by on wine.id = owned_by.wid JOIN tag on owned_by.tname = tag.name
                WHERE wine.id = ?""";
        try (Connection conn = databaseManager.connect()) {
            try (PreparedStatement ps = conn.prepareStatement(psString)) {
                ps.setInt(1, wine.getWineId());
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    wineTags.add(rs.getString(1));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return wineTags;
    }
    /**
     * Verifies that the wine have at least one liked tag and no disliked tags
     * @param wine wine
     * @param likedTags array of liked tags
     * @param dislikedTags array of disliked tags
     * @return boolean
     */
    private boolean verifyWine(String[] likedTags, Wine wine, String[] dislikedTags) {
        ArrayList<String> wineTags = getWineTags(wine);

        assertTrue(Arrays.stream(dislikedTags).noneMatch(wineTags::contains));

        boolean hasLikedTags = false;
        for (String tag : likedTags) {
            hasLikedTags = hasLikedTags || wineTags.contains(tag);
        }

        return hasLikedTags;
    }

    /**
     * Verifies that all the wines have at least one liked tag and no disliked tags
     * @param wines array of wine
     * @param likedTags array of liked tags
     * @param dislikedTags array of disliked tags
     * @return boolean
     */
    private boolean verifyWines(ArrayList<Wine> wines, String[] likedTags, String[] dislikedTags) {
        for (Wine wine : wines) {
            boolean isValid = verifyWine(likedTags, wine, dislikedTags);
            if (!isValid) {
                return false;
            }
        }
        return true;
    }
    /**
     * Verifies that all the wines have at least one liked tag, no disliked tags and are not wines that should be avoided
     * @param wines array of wine
     * @param likedTags array of liked tags
     * @param dislikedTags array of disliked tags
     * @param wineIdToAvoid array of wine id to avoid
     * @return boolean
     */
    public boolean verifyWines(ArrayList<Wine> wines, String[] likedTags, String[] dislikedTags, Integer[] wineIdToAvoid) {
        for (Wine wine : wines) {
            if (!Arrays.asList(wineIdToAvoid).contains(wine.getWineId())) {
                boolean isValid = verifyWine(likedTags, wine, dislikedTags);
                if (!isValid) {
                    return false;
                }
            } else {
                return false;
            }
        }
        return true;
    }


    /**
     * Checks to see if {@link seng202.team1.services.RecommendWineService#hasEnoughFavouritesTag(int)} returns true
     * if the user has at least 3 liked tags and false if otherwise.
     */
    @Test
    public void testHasEnoughFavTag() {
        String[] tags = {"2012", "2004", "2006", "2005", "2003"};
        for (String tag : tags) {
            logWineDao.likes(1, tag, 5);
            logWineDao.likes(2, tag, -5);
        }
        Assertions.assertTrue(recommendWineService.hasEnoughFavouritesTag(1));
        Assertions.assertFalse(recommendWineService.hasEnoughFavouritesTag(2));
    }

    /**
     * Tests that it will try to recommend wines to the user without including wines with disliked tags and making sure
     * that the wines recommended have at least one tag that is liked
     */
    @Test
    public void testReccWithDislikedTags() {
        logWineDao.likes(1, "2012", 1000);
        logWineDao.likes(1, "2004", 1000);
        logWineDao.likes(1, "2005", 1000);
        logWineDao.likes(1, "2006", -1000);
        logWineDao.likes(1, "2008", -1000);
        ArrayList<Wine> reccWine = recommendWineService.getRecommendedWines(1, SearchDAO.UNLIMITED);
        Assertions.assertFalse(reccWine.isEmpty());
        Assertions.assertTrue(verifyWines(reccWine, new String[]{"2012", "2004", "2005"}, new String[]{"2006", "2008"}));
    }

    @Test
    public void testWineIDAreProperlyAvoided() {
        ArrayList<String> tags = new ArrayList<>();
        tags.add("2012");
        tags.add("US");
        tags.add("Willamette Valley");
        tags.add("Pinot Noir");
        tags.add("Sweet Cheeks");
        for (String tag:tags) {
            logWineDao.likes(1, tag, 5);
        }
        //5 is the wine id belonging to the wine which contains all the tags in the arraylist tags
        logWineDao.doReview(1, 5,5,"i love wine", "2024-10-05 22:27:01", tags, tags, false);
        ArrayList<Wine> reccWine = recommendWineService.getRecommendedWines(1, SearchDAO.UNLIMITED);
        Assertions.assertFalse(reccWine.isEmpty());
        //5 is the wine id belonging to the wine which contains all the tags in the arraylist tags
        Assertions.assertTrue(verifyWines(reccWine, new String[]{"2012", "US", "Willamette Valley", "Pinot Noir", "Sweet Cheeks"}, new String[]{}, new Integer[]{5}));
    }

    @Test
    public void testEmptyEmptyWineList() {
        logWineDao.likes(1, "2012", 1000);
        logWineDao.likes(1, "2004", 1000);
        logWineDao.likes(1, "2005", 1000);
        logWineDao.likes(1, "2006", -1000);
        ArrayList<Wine> reccWine = recommendWineService.getRecommendedWines(1, 0);
        Assertions.assertTrue(reccWine.isEmpty());
    }

}
