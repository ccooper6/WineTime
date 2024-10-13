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

public class RecommendWineServiceTest {
    static DatabaseManager databaseManager;
    static LogWineDao logWineDao;
    static RecommendWineService recommendWineService;

    @BeforeEach
    public void setUp() throws InstanceAlreadyExistsException
    {
        DatabaseManager.REMOVE_INSTANCE();
        databaseManager = DatabaseManager.initialiseInstanceWithUrl("jdbc:sqlite:./src/test/resources/test_database.db");
        recommendWineService = new RecommendWineService();
        logWineDao = new LogWineDao();
        DatabaseManager.getInstance().forceReset();
    }

    @Test
    void testDBConnection() {
        assertNotNull(databaseManager);
    }


    private ArrayList<String> getWineTags(Wine wine) {
        ArrayList<String> wineTags = new ArrayList<>();
        String psString = """
                SELECT tag.name
                FROM wine JOIN owned_by on wine.id = owned_by.wid JOIN tag on owned_by.tname = tag.name
                WHERE wine.id = ?""";
        try (Connection conn = databaseManager.connect()) {
            try (PreparedStatement ps = conn.prepareStatement(psString)) {
                ps.setInt(1, wine.getID());
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

    private boolean verifyWine(String[] likedTags, Wine wine, String[] dislikedTags) {
        ArrayList<String> wineTags = getWineTags(wine);

        assertTrue(Arrays.stream(dislikedTags).noneMatch(wineTags::contains));

        boolean hasLikedTags = false;
        for (String tag : likedTags) {
            hasLikedTags = hasLikedTags || wineTags.contains(tag);
        }

        return hasLikedTags;
    }

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

    @Test
    public void testRecommendedWithDislikedTags() {
        logWineDao.likes(1, "2012", 1000);
        logWineDao.likes(1, "2004", 1000);
        logWineDao.likes(1, "2005", 1000);
        logWineDao.likes(1, "2006", -1000);
        logWineDao.likes(1, "2008", -1000);
        ArrayList<Wine> recommendedWines = recommendWineService.getRecommendedWines(1, SearchDAO.UNLIMITED);
        Assertions.assertFalse(recommendedWines.isEmpty());

        String[] likedTags = new String[]{"2012", "2004", "2005"};
        String[] dislikedTags = new String[]{"2006", "2008"};

        assertTrue(recommendedWines.stream().allMatch(wine -> verifyWine(likedTags, wine, dislikedTags)));
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
        ArrayList<Wine> recommendedWines = recommendWineService.getRecommendedWines(1, SearchDAO.UNLIMITED);
        Assertions.assertFalse(recommendedWines.isEmpty());

        //5 is the wine id belonging to the wine which contains all the tags in the arraylist tags
        String[] likedTags = new String[]{"2012", "US", "Willamette Valley", "Pinot Noir", "Sweet Cheeks"};
        String[] dislikedTags = new String[]{};
        Integer[] wineIdsToAvoid = new Integer[]{5};

        for (Wine wine : recommendedWines) {
            assertTrue(Arrays.stream(wineIdsToAvoid).noneMatch(wineId -> wineId.equals(wine.getID())));
        }

        assertTrue(recommendedWines.stream().allMatch(wine -> verifyWine(likedTags, wine, dislikedTags)));
    }

    @Test
    public void testEmptyEmptyWineList() {
        logWineDao.likes(1, "2012", 1000);
        logWineDao.likes(1, "2004", 1000);
        logWineDao.likes(1, "2005", 1000);
        logWineDao.likes(1, "2006", -1000);
        ArrayList<Wine> recommendedWines = recommendWineService.getRecommendedWines(1, 0);
        Assertions.assertTrue(recommendedWines.isEmpty());
    }

}
