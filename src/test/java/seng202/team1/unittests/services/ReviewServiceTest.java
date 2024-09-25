package seng202.team1.unittests.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import seng202.team1.exceptions.InstanceAlreadyExistsException;
import seng202.team1.models.Review;
import seng202.team1.repository.DAOs.LogWineDao;
import seng202.team1.repository.DAOs.UserDAO;
import seng202.team1.repository.DatabaseManager;
import seng202.team1.services.ReviewService;
import seng202.team1.services.WineLoggingPopupService;

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
}
