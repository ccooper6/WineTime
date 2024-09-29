package seng202.team1.unittests.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.internal.matchers.Null;
import seng202.team1.exceptions.InstanceAlreadyExistsException;
import seng202.team1.models.Review;
import seng202.team1.models.Wine;
import seng202.team1.repository.DAOs.LogWineDao;
import seng202.team1.repository.DAOs.UserDAO;
import seng202.team1.repository.DatabaseManager;
import seng202.team1.services.ReviewService;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for {@link ReviewService} which mainly concerns that submit log is calling the appropriate methods
 * from {@link LogWineDao}. Majority of the functionality is based off methods from {@link LogWineDao} and as such, edge
 * cases such as updating reviews and liked tags values are tested in {@link LogWineDAOTest} instead.
 * @author Caleb Cooper
 */
public class ReviewServiceTest {
    static ReviewService reviewService;
    static LogWineDao logWineDao;
    static UserDAO userDAO;

    /**
     * Makes sure the database is set up before each test. Overwrites the prev test database with a clean test_database
     * before each test.
     * @throws InstanceAlreadyExistsException if the database instance already exists
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

    @Test
    public void testGoodGetUserReviews() {
        int testUserId = 1;
        logWineDao.reviews(testUserId, 1, 5, "Great wine!", "12/12/12", new ArrayList<>(List.of("fruity", "smooth")), false);
        logWineDao.reviews(testUserId, 2, 4, "Good wine!", "12/12/12", new ArrayList<>(List.of("dry", "bold")), false);

        ArrayList<Review> userReviews = ReviewService.getUserReviews(testUserId);

        assertNotNull(userReviews);
        assertEquals(2, userReviews.size());
        assertEquals(userReviews.get(0).getUid(), testUserId);
        assertEquals(userReviews.get(0).getWid(), 1);
        assertEquals(userReviews.get(0).getRating(), 5);
        assertEquals(userReviews.get(0).getReviewDescription(), "Great wine!");
        assertEquals(userReviews.get(0).getReviewDate(), "12/12/12");
        assertEquals(userReviews.get(0).getTagsSelected(), new ArrayList<>(List.of("fruity", "smooth")));
    }

    @Test
    public void testBadGetUserReviews() {
        int testUserId = 1;
        logWineDao.reviews(testUserId, 1, 5, "Great wine!", "12/12/12", new ArrayList<>(List.of("fruity", "smooth")), false);
        logWineDao.reviews(testUserId, 2, 4, "Good wine!", "12/12/12", new ArrayList<>(List.of("dry", "bold")), false);

        ArrayList<Review> userReviews = ReviewService.getUserReviews(testUserId);

        assertNotNull(userReviews);
        assertNotEquals(5, userReviews.size());
        assertThrows(IndexOutOfBoundsException.class,
                ()-> userReviews.get(4).getUid());
        assertThrows(IndexOutOfBoundsException.class,
                ()-> userReviews.get(4).getWid());
    }

    @Test
    public void testReviewDoesExist() {
        int testUserId = 1;
        logWineDao.reviews(testUserId, 1, 5, "Great wine!", "12/12/12", new ArrayList<>(List.of("fruity", "smooth")), false);
        assertTrue(reviewService.reviewExists(1,1));
    }

    @Test
    public void testReviewDoesNotExist() {
        int testUserId = 1;
        logWineDao.reviews(testUserId, 1, 5, "Great wine!", "12/12/12", new ArrayList<>(List.of("fruity", "smooth")), false);
        assertFalse(reviewService.reviewExists(1,2));
        assertFalse(reviewService.reviewExists(2,1));
    }

    @Test
    public void testDeleteReview() {
        int uid = 1;
        int wid = 3;
        int rating = 5;
        String desc = "Great wine!";
        String date = "12/12/12";
        ArrayList<String> tags = new ArrayList<>(List.of("fruity", "smooth"));

        Review testReview = new Review(uid, wid, rating, desc, date, tags);
        logWineDao.reviews(uid, wid, rating, desc, date, tags, false);

        assertTrue(reviewService.reviewExists(1,3));
        ReviewService.deleteReview(rating, testReview);
        assertFalse(reviewService.reviewExists(1,3));
    }

    @Test
    public void testGoodGetCurrentWine() {
        int uid = 1;
        int wid = 3;
        int rating = 5;
        String desc = "Great wine!";
        String date = "12/12/12";
        ArrayList<String> tags = new ArrayList<>(List.of("fruity", "smooth"));

        Review testReview = new Review(uid, wid, rating, desc, date, tags);
        reviewService.setCurrentReview(testReview);
        Wine wine =  ReviewService.getCurrentWine();
        assertEquals(wine.getWineId(), 3);
        assertEquals(wine.getName(), "Rainstorm 2013 Pinot Gris (Willamette Valley)");
        assertEquals(wine.getVintage(), 2013);
        assertEquals(wine.getCountry(), "US");
    }

    @Test
    public void testOutOfBoundsWineIDGetCurrentWine() {
        int uid = 1;
        int wid = 1000000;
        int rating = 5;
        String desc = "Great wine!";
        String date = "12/12/12";
        ArrayList<String> tags = new ArrayList<>(List.of("fruity", "smooth"));

        Review testReview = new Review(uid, wid, rating, desc, date, tags);
        reviewService.setCurrentReview(testReview);
        assertThrows(NullPointerException.class,
                ReviewService::getCurrentWine);
    }

    @Test
    public void testNoCurrentReviewGetCurrentWine() {
        assertNull(ReviewService.getCurrentReview());
        assertThrows(NullPointerException.class,
                ReviewService::getCurrentWine);
    }
}
