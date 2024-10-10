package seng202.team1.unittests.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import seng202.team1.exceptions.InstanceAlreadyExistsException;
import seng202.team1.models.Review;
import seng202.team1.repository.DAOs.LogWineDao;
import seng202.team1.repository.DAOs.SearchDAO;
import seng202.team1.repository.DAOs.UserDAO;
import seng202.team1.repository.DatabaseManager;
import seng202.team1.services.ReviewService;


import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for {@link ReviewService} which mainly concerns that submit log is calling the appropriate methods
 * from {@link LogWineDao}. Majority of the functionality is based off methods from {@link LogWineDao} and as such, edge
 * cases such as updating reviews and liked tags values are tested in {@link LogWineDAOTest} instead.
 *
 * @author Wen Sheng Thong
 */
public class WineLoggingServiceTest {
    static ReviewService reviewService;
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
        reviewService = new ReviewService();
        logWineDao = new LogWineDao();
        userDAO = new UserDAO();
    }

    /**
     * Tests that {@link ReviewService#submitLog(int, int, int, ArrayList, ArrayList, boolean, String)} submits the log properly
     * by calling {@link LogWineDao#doReview(int, int, int, String, String, ArrayList, ArrayList, boolean)} and {@link LogWineDao#likes(int, String, int)}
     * properly, as well as making sure all redundant whitespace is removed from the description text before being added
     * to the database
     */
    @Test
    public void testSubmitLog() {
        ArrayList<String> selectedTags = new ArrayList<String>(Arrays.asList("seng202 teaching team", "red wine"));
        ArrayList<String> likedTags = new ArrayList<String>(Arrays.asList("seng202 teaching team", "red wine"));
        reviewService.submitLog(5,1,69, selectedTags, likedTags, false, "I      love them");
        ArrayList<Review> reviews = logWineDao.getUserReview(1,1, true);
        assertEquals(1,reviews.getFirst().getUid());
        assertEquals(69,reviews.getFirst().getWid());
        assertEquals(5,reviews.getFirst().getRating());
        //whitespace removed
        assertEquals("I love them",reviews.getFirst().getReviewDescription());
        assertEquals(1, reviews.size());
    }

    /**
     * Test that {@link ReviewService#getCurrentTimeStamp()} gets the current time properly.
     * May fail randomly at times if the seconds just so happen to change
     */
    @Test
    public void testGetCurrentTime() {
        String currentTime = ZonedDateTime.now( ZoneId.systemDefault() ).format(DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm:ss"));
        String serviceCurrentTime = reviewService.getCurrentTimeStamp();
        assertEquals(currentTime, serviceCurrentTime);
    }

    @Test
    public void testGetReviewExists() {
        int testUserId = 1;
        logWineDao.doReview(testUserId, 1, 5, "Great wine!", "12/12/12", new ArrayList<>(List.of("fruity", "smooth")), new ArrayList<>(List.of("fruity", "smooth")), false);
        Review testReview = reviewService.getReview(1,1);
        assertEquals(testReview.getUid(), 1);
        assertEquals(testReview.getWid(), 1);
        assertEquals(testReview.getRating(), 5);
        assertEquals(testReview.getReviewDate(), "12/12/12");
        assertEquals(testReview.getReviewDescription(), "Great wine!");
        assertEquals(testReview.getTagsSelected(), new ArrayList<>(List.of("fruity", "smooth")));
    }

    @Test
    public void testGetReviewDoesNotExist() {
        assertNull(reviewService.getReview(1,1));
    }

    @Test
    public void testUpdateSelectedTagLikes() {
        int uid = 1;
        int wid = 1;
        int initialRating = 5;
        int newRating = 2;

        ArrayList<String> initialTags = new ArrayList<>(List.of("2013", "White Blend"));
        ArrayList<String> tagsToAdd = new ArrayList<>(List.of("clean", "calebiscool", "sparkly"));
//        ArrayList<String> tagsToRemove = new ArrayList<>(List.of("fruity", "smooth"));

        reviewService.updateTagLikes(uid, wid, initialTags, initialRating);
        reviewService.submitLog(5, uid, wid, initialTags, initialTags, false, "");
        HashMap<String, Integer> result = logWineDao.getLikedTags(1, SearchDAO.UNLIMITED, true);
        assertEquals(2, result.size());
        assertTrue(result.containsKey("2013") && result.containsKey("White Blend"));
        assertEquals(3, result.get("2013"));
        assertEquals(3, result.get("White Blend"));

        reviewService.updateTagLikes(uid, wid, tagsToAdd, newRating);
        HashMap<String, Integer> newResult = logWineDao.getLikedTags(uid, SearchDAO.UNLIMITED, true);
        assertTrue(newResult.containsKey("clean") && newResult.containsKey("calebiscool") && newResult.containsKey("sparkly"));
        assertEquals(-1, newResult.get("clean"));
        assertEquals(-1, newResult.get("calebiscool"));
        assertEquals(-1, newResult.get("sparkly"));


        assertTrue(newResult.containsKey("2013") && newResult.get("2013") == 0);
        assertTrue(newResult.containsKey("White Blend") && newResult.get("White Blend") == 0);
    }

    @Test
    public void testUpdateNoSelectedTagLikes() {
        int uid = 1;
        int wid = 1;
        int initialRating = 5;

        ArrayList<String> potentialTagsToChoose = new ArrayList<>(List.of("fruity", "smooth", "clean", "calebiscool", "sparkly"));

        // Add the value to all the tags as we assume if none are selected, all are liked/disliked
        reviewService.updateTagLikes(uid, wid, potentialTagsToChoose, initialRating);
        HashMap<String, Integer> result = logWineDao.getLikedTags(uid, SearchDAO.UNLIMITED, true);
        assertEquals(5, result.size()); // We can do this as we hava a fresh database
        assertEquals(3, result.get("fruity"));
        assertEquals(3, result.get("smooth"));
        assertEquals(3, result.get("clean"));
        assertEquals(3, result.get("calebiscool"));
        assertEquals(3, result.get("sparkly"));
    }

    @Test
    public void testMoveFromNoneSelectedToSomeSelected() {
        int uid = 1;
        int wid = 1;
        int initialRating = 5;
        int newRating = 2;

        ArrayList<String> potentialTagsToChoose = new ArrayList<>(List.of("2013", "Nicosia", "Italy", "White Blend", "Etna"));
        ArrayList<String> selectedTags = new ArrayList<>();

        // Add the value to all the tags as we assume if none are selected, all are liked/disliked
        reviewService.updateTagLikes(uid, wid, potentialTagsToChoose, initialRating);
        reviewService.submitLog(5, uid, wid, selectedTags, potentialTagsToChoose, true, "");
        HashMap<String, Integer> result = logWineDao.getLikedTags(uid, SearchDAO.UNLIMITED, true);
        assertEquals(5, result.size()); // We can do this as we hava a fresh database
        assertEquals(3, result.get("2013"));
        assertEquals(3, result.get("Nicosia"));
        assertEquals(3, result.get("Italy"));
        assertEquals(3, result.get("White Blend"));
        assertEquals(3, result.get("Etna"));

        // Now we select some tags
        selectedTags = new ArrayList<>(List.of("Nicosia", "Etna"));
        reviewService.updateTagLikes(uid, wid, selectedTags, newRating);
        HashMap<String, Integer> newResult = logWineDao.getLikedTags(uid, SearchDAO.UNLIMITED, true);
        assertEquals(5, newResult.size());
        assertEquals(-1, newResult.get("Nicosia"));
        assertEquals(-1, newResult.get("Etna"));
        assertEquals(0, newResult.get("Italy"));
        assertEquals(0, newResult.get("White Blend"));
        assertEquals(0, newResult.get("2013"));
    }
}
