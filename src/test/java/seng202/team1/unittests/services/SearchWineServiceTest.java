package seng202.team1.unittests.services;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import seng202.team1.exceptions.InstanceAlreadyExistsException;
import seng202.team1.models.TagType;
import seng202.team1.models.User;
import seng202.team1.models.Wine;
import seng202.team1.models.WineBuilder;
import seng202.team1.repository.DAOs.LogWineDAO;
import seng202.team1.repository.DAOs.SearchDAO;
import seng202.team1.repository.DAOs.WishlistDAO;
import seng202.team1.repository.DatabaseManager;
import seng202.team1.services.SearchWineService;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

public class SearchWineServiceTest {

    private static DatabaseManager databaseManager;
    private static LogWineDAO logWineDao;

    @BeforeAll
    static void setUp() throws InstanceAlreadyExistsException
    {
        DatabaseManager.removeInstance();
        databaseManager = DatabaseManager.initialiseInstanceWithUrl("jdbc:sqlite:./src/test/resources/test_database.db");
        logWineDao = new LogWineDAO();
        DatabaseManager.getInstance().forceReset();
    }

    @Test
    void testDBConnection() {
        assertNotNull(databaseManager);
    }

    @Test
    public void searchWinesByTags1Tag()
    {
        String tags = "Oregon";
        SearchWineService.getInstance().searchWinesByTags(tags, SearchDAO.UNLIMITED);
        ArrayList<Wine> fromDB = SearchWineService.getInstance().getWineList();

        assertEquals(4910, fromDB.size());
        assertTrue(fromDB.stream().allMatch(wine -> wine.hasLocation("Oregon")));
    }

    @Test
    public void searchWinesByTagsEmptyString()
    {
        String tags = "";
        SearchWineService.getInstance().searchWinesByTags(tags, SearchDAO.UNLIMITED);
        ArrayList<Wine> fromDB = SearchWineService.getInstance().getWineList();
        assertEquals(0, fromDB.size());
    }

    @Test
    public void searchWinesByTagsTrailingComma()
    {
        String tags = "Oregon,,,";
        SearchWineService.getInstance().searchWinesByTags(tags, SearchDAO.UNLIMITED);
        ArrayList<Wine> fromDB = SearchWineService.getInstance().getWineList();

        assertEquals(4910, fromDB.size());
        assertTrue(fromDB.stream().allMatch(wine -> wine.hasLocation("Oregon")));
    }

    @Test
    public void searchWinesByTagsEmptyTag()
    {
        String tags = "Oregon,,Pinot Noir";
        SearchWineService.getInstance().searchWinesByTags(tags, SearchDAO.UNLIMITED);
        ArrayList<Wine> fromDB = SearchWineService.getInstance().getWineList();
        assertEquals(0, fromDB.size());
    }

    @Test
    public void searchWinesByTag2Tags()
    {
        String tags = "Oregon, Pinot Noir";
        SearchWineService.getInstance().searchWinesByTags(tags, SearchDAO.UNLIMITED);
        ArrayList<Wine> fromDB = SearchWineService.getInstance().getWineList();

        assertEquals(2551, fromDB.size());

        assertTrue(fromDB.stream().allMatch(wine -> wine.hasLocation("Oregon")));
        assertTrue(fromDB.stream().allMatch(wine -> wine.getVariety().equals("Pinot Noir")));
    }

    @Test
    public void searchWinesByTag2TagsWeirdFormat()
    {
        String tags = "  oRÉGon  , pinót Noir  ";
        SearchWineService.getInstance().searchWinesByTags(tags, SearchDAO.UNLIMITED);
        ArrayList<Wine> fromDB = SearchWineService.getInstance().getWineList();

        assertEquals(2551, fromDB.size());
        assertTrue(fromDB.stream().allMatch(wine -> wine.hasLocation("Oregon")));
        assertTrue(fromDB.stream().allMatch(wine -> wine.getVariety().equals("Pinot Noir")));
    }

    @Test
    public void searchWinesByTagTagNotExist()
    {
        String tags = "2030";
        SearchWineService.getInstance().searchWinesByTags(tags, SearchDAO.UNLIMITED);
        ArrayList<Wine> fromDB = SearchWineService.getInstance().getWineList();
        assertEquals(0, fromDB.size());
    }

    @Test
    public void searchWinesByTagGeneralConflictingTags()
    {
        String tags = "2001,2002";
        SearchWineService.getInstance().searchWinesByTags(tags, SearchDAO.UNLIMITED);
        ArrayList<Wine> fromDB = SearchWineService.getInstance().getWineList();
        assertEquals(0, fromDB.size());
    }

    @Test
    public void searchWinesByName1Word() {
        String name = "Chardonnay";
        SearchWineService.getInstance().searchWinesByName(name);
        ArrayList<Wine> fromDB = SearchWineService.getInstance().getWineList();

        assertEquals(8886, fromDB.size());
        assertTrue(fromDB.stream().allMatch(wine -> wine.getName().toLowerCase().contains("chardonnay")));
    }

    @Test
    public void searchWinesByNameWeirdFormat() {
        String name = "ChardóNnAy";
        SearchWineService.getInstance().searchWinesByName(name);
        ArrayList<Wine> fromDB = SearchWineService.getInstance().getWineList();

        assertEquals(8886, fromDB.size());
        assertTrue(fromDB.stream().allMatch(wine -> wine.getName().contains("Chardonnay")));
    }

    @Test
    public void searchWinesByName2Words()
    {
        String name = "New Zealand";
        SearchWineService.getInstance().searchWinesByName(name);
        ArrayList<Wine> fromDB = SearchWineService.getInstance().getWineList();

        assertEquals(9, fromDB.size());
        assertTrue(fromDB.stream().allMatch(wine -> wine.getName().contains("New Zealand")));
    }

    @Test
    public void searchWinesByNameTrailingSpaces() {
        String name = "     chardonnay    ";
        SearchWineService.getInstance().searchWinesByName(name);
        ArrayList<Wine> fromDB = SearchWineService.getInstance().getWineList();

        assertEquals(8886, fromDB.size());
        assertTrue(fromDB.stream().allMatch(wine -> wine.getName().toLowerCase().contains("chardonnay")));
    }

    @Test
    public void searchWinesByNameEmptyString()
    {
        String name = "";
        SearchWineService.getInstance().searchWinesByName(name);
        ArrayList<Wine> fromDB = SearchWineService.getInstance().getWineList();

        assertEquals(118837, fromDB.size());
    }

    @Test
    public void searchWinesByNameNoMatch()
    {
        String name = "this wine should not exist";

        SearchWineService.getInstance().searchWinesByName(name);
        ArrayList<Wine> fromDB = SearchWineService.getInstance().getWineList();

        assertEquals(0, fromDB.size());
    }

    private ArrayList<String> getWineTags(Wine wine) {
        ArrayList<String> wineTags = new ArrayList<>();
        String psString = """
                SELECT tag.name
                FROM wine JOIN owned_by on wine.id = owned_by.wid JOIN tag on owned_by.tname = tag.name
                WHERE wine.id = ?;""";
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

    @Test
    public void testRecommendationSearch() {
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
        logWineDao.doReview(1, 5,5,"i love wine", "2024-10-05 22:27:01",tags, tags,false);
        User.setCurrentUser(new User(1, "user1", Objects.hash("User1")));
        SearchWineService.getInstance().searchWinesByRecommend(User.getCurrentUser().getId(), 10);
        ArrayList<Wine> recommendedWines = SearchWineService.getInstance().getWineList();
        assertFalse(recommendedWines.isEmpty());
        //5 is the wine id belonging to the wine which contains all the tags in the arraylist tags
        String[] likedTags = new String[]{"2012", "US", "Willamette Valley", "Pinot Noir", "Sweet Cheeks"};
        String[] dislikedTags = new String[]{};
        Integer[] wineIdsToAvoid = new Integer[]{5};

        assertTrue(recommendedWines.stream().noneMatch(wine -> Arrays.asList(wineIdsToAvoid).contains(wine.getID())));

        for (Wine wine : recommendedWines) {
            ArrayList<String> wineTags = getWineTags(wine);

            assertFalse(Arrays.stream(likedTags).noneMatch(wineTags::contains));
            assertTrue(Arrays.stream(dislikedTags).noneMatch(wineTags::contains));
        }
    }

    @Test
    public void testSetCurrentSearch() {
        SearchWineService.getInstance().setCurrentSearch("Tags");
        assertEquals("Tags", SearchWineService.getInstance().getCurrentSearch());
    }

    @Test
    public void testSetCurrentMethod() {
        SearchWineService.getInstance().setCurrentMethod("Wishlist");
        assertEquals("Wishlist", SearchWineService.getInstance().getCurrentMethod());
    }

    @Test
    public void testSetSortDirection() {
        SearchWineService.getInstance().setSortDirection(true);
        assertTrue(SearchWineService.getInstance().getSortDirection());
    }
    @Test
    public void testSearchWineByWishlist() {
        WishlistDAO.getInstance().addWine(1,1);
        SearchWineService.getInstance().searchWinesByWishlist(1);
        assertEquals(1,SearchWineService.getInstance().getWineList().getFirst().getID());
    }
    @Test
    public void testSetCurrentWine() {
        WineBuilder wineBuilder = WineBuilder.genericSetup(1, "TestName", "TestDescription", 5, 97);
        wineBuilder.setTag(TagType.VARIETY, "Früburgunder");
        Wine wine = wineBuilder.build();
        SearchWineService.getInstance().setCurrentWine(wine);
        assertEquals(wine, SearchWineService.getInstance().getCurrentWine());
    }

    @Test
    public void testResetFilters() {
        SearchWineService.getInstance().setCurrentMinPoints(90);
        SearchWineService.getInstance().setCurrentMaxPoints(91);
        SearchWineService.getInstance().setCurrentMinYear(1900);
        SearchWineService.getInstance().setCurrentMaxYear(1921);
        SearchWineService.getInstance().setCurrentMinPrice(6);
        SearchWineService.getInstance().setCurrentMaxPrice(7);
        SearchWineService.getInstance().setCurrentWineryFilter("Isaac is cool");
        SearchWineService.getInstance().setCurrentVarietyFilter("Isaac is really cool");
        SearchWineService.getInstance().setCurrentCountryFilter("Isaac is super cool");

        SearchWineService.getInstance().resetFilters();

        assertEquals(80, SearchWineService.getInstance().getCurrentMinPoints());
        assertEquals(100, SearchWineService.getInstance().getCurrentMaxPoints());
        assertEquals(1821, SearchWineService.getInstance().getCurrentMinYear());
        assertEquals(2017, SearchWineService.getInstance().getCurrentMaxYear());
        assertEquals(4, SearchWineService.getInstance().getCurrentMinPrice());
        assertEquals(3300, SearchWineService.getInstance().getCurrentMaxPrice());
        assertNull(SearchWineService.getInstance().getCurrentWineryFilter());
        assertNull(SearchWineService.getInstance().getCurrentVarietyFilter());
        assertNull(SearchWineService.getInstance().getCurrentCountryFilter());
    }

    @Test
    public void testSetMaxPrice() {
        SearchWineService.getInstance().resetFilters();
        SearchWineService.getInstance().setCurrentMaxPrice(199);
        assertEquals(199, SearchWineService.getInstance().getCurrentMaxPrice());
        SearchWineService.getInstance().setCurrentMaxPrice(200);
        // 200 is max for most wines so if set to 200 check all
        assertEquals(3300, SearchWineService.getInstance().getCurrentMaxPrice());
    }

    @Test
    public void testTagsFromStringGeneral() {
        String tags = "New Zealand, 2017, Central Otago";
        String[] expected = new String[] {"new zealand", "2017", "central otago"};

        ArrayList<String> received = SearchWineService.getInstance().getTagsFromString(tags);
        String[] actual = received.toArray(new String[0]);

        assertArrayEquals(expected, actual);
    }

    @Test
    public void testTagsFromStringTrailingSpaces() {
        String tags = "   New Zealand, 2017  ,  Central Otago   ";
        String[] expected = new String[] {"new zealand", "2017", "central otago"};

        ArrayList<String> received = SearchWineService.getInstance().getTagsFromString(tags);
        String[] actual = received.toArray(new String[0]);

        assertArrayEquals(expected, actual);
    }

    @Test
    public void testTagsFromStringNull() {
        String tags = null;
        String[] expected = new String[] {};

        ArrayList<String> received = SearchWineService.getInstance().getTagsFromString(tags);
        String[] actual = received.toArray(new String[0]);

        assertArrayEquals(expected, actual);
    }

    @Test
    public void testTagsFromStringEmpty() {
        String tags = "";
        String[] expected = new String[] {};

        ArrayList<String> received = SearchWineService.getInstance().getTagsFromString(tags);
        String[] actual = received.toArray(new String[0]);

        assertArrayEquals(expected, actual);
    }


}
