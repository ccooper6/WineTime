package seng202.team1.unittests.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import seng202.team1.exceptions.InstanceAlreadyExistsException;
import seng202.team1.models.Review;
import seng202.team1.repository.DAOs.LogWineDao;
import seng202.team1.repository.DAOs.UserDAO;
import seng202.team1.repository.DatabaseManager;
import seng202.team1.services.WineLoggingPopupService;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for {@link WineLoggingPopupService} which mainly concerns that submit log is calling the appropriate methods
 * from {@link LogWineDao}. Majority of the functionality is based off methods from {@link LogWineDao} and as such, edge
 * cases such as updating reviews and liked tags values are tested in {@link LogWineDAOTest} instead.
 *
 * @author Wen Sheng Thong
 */
public class WineLoggingServiceTest {
    static WineLoggingPopupService wineLoggingPopupService;
    static LogWineDao logWineDao;
    static UserDAO userDAO;

    /**
     * Makes sure the database is set up before each test. Overwrites the prev test database with a clean test_database
     * before each test.
     * @throws InstanceAlreadyExistsException
     */
    @BeforeEach
    public void setUp() throws InstanceAlreadyExistsException {
        DatabaseManager.REMOVE_INSTANCE();
        DatabaseManager.initialiseInstanceWithUrl("jdbc:sqlite:./src/test/resources/test_database.db");
        DatabaseManager.getInstance().forceReset();
        wineLoggingPopupService = new WineLoggingPopupService();
        logWineDao = new LogWineDao();
        userDAO = new UserDAO();
    }

    /**
     * Tests that {@link WineLoggingPopupService#submitLog(int, int, int, ArrayList, boolean, String)} submits the log properly
     * by calling {@link LogWineDao#reviews(int, int, int, String, String, ArrayList, boolean)} and {@link LogWineDao#likes(int, String, int)}
     * properly, as well as making sure all redundant whitespace is removed from the description text before being added
     * to the database
     */
    @Test
    public void testSubmitLog() {
        ArrayList<String> likedTags = new ArrayList<String>(Arrays.asList("seng202 teaching team", "red wine"));
        wineLoggingPopupService.submitLog(5,1,69, likedTags, false, "I      love them" );
        ArrayList<Review> reviews = logWineDao.getUserReview(1,1, true);
        assertEquals(1,reviews.getFirst().getUid());
        assertEquals(69,reviews.getFirst().getWid());
        assertEquals(5,reviews.getFirst().getRating());
        //whitespace removed
        assertEquals("I love them",reviews.getFirst().getReviewDescription());
        assertEquals(1, reviews.size());
    }

    /**
     * Test that {@link WineLoggingPopupService#getCurrentTimeStamp()} gets the current time properly.
     * May fail randomly at times if the seconds just so happen to change
     */
    @Test
    public void testGetCurrentTime() {
        String currentTime = ZonedDateTime.now( ZoneId.systemDefault() ).format(DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm:ss"));
        String serviceCurrentTime = wineLoggingPopupService.getCurrentTimeStamp();
        assertEquals(currentTime, serviceCurrentTime);
    }

    @Test
    public void testGetReviewExists() {
        int testUserId = 1;
        logWineDao.reviews(testUserId, 1, 5, "Great wine!", "12/12/12", new ArrayList<>(List.of("fruity", "smooth")), false);
        Review testReview = wineLoggingPopupService.getReview(1,1);
        assertEquals(testReview.getUid(), 1);
        assertEquals(testReview.getWid(), 1);
        assertEquals(testReview.getRating(), 5);
        assertEquals(testReview.getReviewDate(), "12/12/12");
        assertEquals(testReview.getReviewDescription(), "Great wine!");
        assertEquals(testReview.getTagsSelected(), new ArrayList<>(List.of("fruity", "smooth")));
    }

    @Test
    public void testGetReviewDoesNotExist() {
        assertNull(wineLoggingPopupService.getReview(1,1));
    }

    @Test
    public void testUpdateTagLikes() {
        int uid = 1;
        int initialRating = 5;
        int newRating = 2;
        int definedRating = (initialRating) - 2;

        ArrayList<String> initialTags = new ArrayList<>(List.of("fruity", "smooth"));
        ArrayList<String> tagsToAdd = new ArrayList<>(List.of("clean", "calebiscool", "sparkly"));
        ArrayList<String> tagsToRemove = new ArrayList<>(List.of("fruity", "smooth"));

        for (String tag: initialTags) {
            wineLoggingPopupService.updateTagLikes(uid, tag, definedRating);
        }
        HashMap<String, Integer> result = logWineDao.getLikedTags(1, true);
        assertEquals(2, result.size());
        assertTrue(result.containsKey("fruity") && result.containsKey("smooth"));
        assertEquals(3, result.get("fruity"));
        assertEquals(3, result.get("smooth"));

        wineLoggingPopupService.updateTagLikes(uid, tagsToAdd, tagsToRemove, initialTags, newRating, initialRating);
        HashMap<String, Integer> newResult = logWineDao.getLikedTags(1, true);
        assertTrue(newResult.containsKey("clean") && newResult.containsKey("calebiscool") && newResult.containsKey("sparkly"));
        assertEquals(-1, newResult.get("clean"));
        assertEquals(-1, newResult.get("calebiscool"));
        assertEquals(-1, newResult.get("sparkly"));


        assertTrue(newResult.containsKey("fruity") && newResult.get("fruity") == 0);
        assertTrue(newResult.containsKey("smooth") && newResult.get("smooth") == 0);


    }
}
