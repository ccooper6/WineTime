package seng202.team1.unittests.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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

public class ReviewServiceTest {
    static ReviewService reviewService;
    static LogWineDao logWineDao;
    static UserDAO userDAO;

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
        logWineDao.doReview(testUserId, 1, 5, "Great wine!", "12/12/12", new ArrayList<>(List.of("fruity", "smooth")), new ArrayList<>(List.of("fruity", "smooth")), false);
        logWineDao.doReview(testUserId, 2, 4, "Good wine!", "12/12/12", new ArrayList<>(List.of("dry", "bold")), new ArrayList<>(List.of("dry", "bold")), false);

        ArrayList<Review> userReviews = ReviewService.getUserReviews(testUserId);

        assertNotNull(userReviews);
        assertEquals(2, userReviews.size());
        assertEquals(userReviews.getFirst().getUid(), testUserId);
        assertEquals(userReviews.getFirst().getWid(), 1);
        assertEquals(userReviews.getFirst().getRating(), 5);
        assertEquals(userReviews.getFirst().getDescription(), "Great wine!");
        assertEquals(userReviews.getFirst().getDate(), "12/12/12");
        assertEquals(userReviews.getFirst().getTagsSelected(), new ArrayList<>(List.of("fruity", "smooth")));
    }

    @Test
    public void testBadGetUserReviews() {
        int testUserId = 1;
        logWineDao.doReview(testUserId, 1, 5, "Great wine!", "12/12/12", new ArrayList<>(List.of("fruity", "smooth")), new ArrayList<>(List.of("fruity", "smooth")), false);
        logWineDao.doReview(testUserId, 2, 4, "Good wine!", "12/12/12", new ArrayList<>(List.of("dry", "bold")), new ArrayList<>(List.of("dry", "bold")), false);

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
        logWineDao.doReview(testUserId, 1, 5, "Great wine!", "12/12/12", new ArrayList<>(List.of("fruity", "smooth")), new ArrayList<>(List.of("fruity", "smooth")), false);
        assertTrue(reviewService.reviewExists(1,1));
    }

    @Test
    public void testReviewDoesNotExist() {
        int testUserId = 1;
        logWineDao.doReview(testUserId, 1, 5, "Great wine!", "12/12/12", new ArrayList<>(List.of("fruity", "smooth")), new ArrayList<>(List.of("fruity", "smooth")), false);
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
        ArrayList<String> selectedTags = new ArrayList<>(List.of("fruity", "smooth"));
        ArrayList<String> likedTags = new ArrayList<>(List.of("fruity", "smooth"));

        Review testReview = new Review(uid, wid, rating, desc, date, selectedTags, likedTags);
        logWineDao.doReview(uid, wid, rating, desc, date, selectedTags, likedTags, false);

        assertTrue(reviewService.reviewExists(1,3));
        reviewService.deleteReview(testReview);
        assertFalse(reviewService.reviewExists(1,3));
    }

    @Test
    public void testGoodGetCurrentWine() {
        int uid = 1;
        int wid = 3;
        int rating = 5;
        String desc = "Great wine!";
        String date = "12/12/12";
        ArrayList<String> selectedTags = new ArrayList<>(List.of("fruity", "smooth"));
        ArrayList<String> likedTags = new ArrayList<>(List.of("fruity", "smooth"));

        Review testReview = new Review(uid, wid, rating, desc, date, selectedTags, likedTags);

        reviewService.setCurrentReview(testReview);
        Wine wine =  ReviewService.getCurrentWine();
        assertEquals(wine.getID(), 3);
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
        ArrayList<String> selectedTags = new ArrayList<>(List.of("fruity", "smooth"));
        ArrayList<String> likedTags = new ArrayList<>(List.of("fruity", "smooth"));

        Review testReview = new Review(uid, wid, rating, desc, date, selectedTags, likedTags);
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
