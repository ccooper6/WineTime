package seng202.team1.unittests.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import seng202.team1.exceptions.InstanceAlreadyExistsException;
import seng202.team1.models.Review;
import seng202.team1.repository.DatabaseManager;
import seng202.team1.repository.DAOs.LogWineDao;
import seng202.team1.repository.DAOs.UserDAO;
import seng202.team1.services.WineLoggingPopupService;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

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
        Assertions.assertEquals(1,reviews.getFirst().getUid());
        Assertions.assertEquals(69,reviews.getFirst().getWid());
        Assertions.assertEquals(5,reviews.getFirst().getRating());
        //whitespace removed
        Assertions.assertEquals("I love them",reviews.getFirst().getReviewDescription());
        Assertions.assertEquals(1, reviews.size());
    }

    /**
     * Test that {@link WineLoggingPopupService#getCurrentTimeStamp()} gets the current time properly.
     * May fail randomly at times if the seconds just so happen to change
     */
    @Test
    public void testGetCurrentTime() {
        String currentTime = ZonedDateTime.now( ZoneId.systemDefault() ).format(DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm:ss"));
        String serviceCurrentTime = wineLoggingPopupService.getCurrentTimeStamp();
        Assertions.assertEquals(currentTime, serviceCurrentTime);
    }
}
