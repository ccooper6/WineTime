package seng202.team1.unittests.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import seng202.team1.exceptions.DuplicateEntryException;
import seng202.team1.exceptions.InstanceAlreadyExistsException;
import seng202.team1.models.Review;
import seng202.team1.models.User;
import seng202.team1.repository.DatabaseManager;
import seng202.team1.repository.LogWineDao;
import seng202.team1.repository.UserDAO;
import seng202.team1.services.WineLoggingPopupService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
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
    static DatabaseManager databaseManager;
    static WineLoggingPopupService wineLoggingPopupService;
    static LogWineDao logWineDao;
    static UserDAO userDAO;

    /**
     * Makes sure the database is set up before each test. Overwrites the prev test database with a clean test_database
     * before each test.
     * @throws InstanceAlreadyExistsException
     */
    @BeforeAll
    static void setUp() throws InstanceAlreadyExistsException {
        DatabaseManager.REMOVE_INSTANCE();
        databaseManager = DatabaseManager.initialiseInstanceWithUrl("jdbc:sqlite:./src/test/resources/test_database.db");
        wineLoggingPopupService = new WineLoggingPopupService();
        logWineDao = new LogWineDao();
        userDAO = new UserDAO();

        String copyPath = databaseManager.url.substring(12);
        Path copy = Paths.get(copyPath);
        Path ogPath = Paths.get("src/main/resources/sql/og.db");
        try {
            Files.copy(ogPath, copy, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Tests that {@link WineLoggingPopupService#submitLog(int, int, int, ArrayList, String)} submits the log properly
     * by calling {@link LogWineDao#reviews(int, int, int, String, String)} and {@link LogWineDao#likes(int, String, int)}
     * properly, as well as making sure all redundant whitespace is removed from the description text before being added
     * to the database
     */
    @Test
    public void testSubmitLog() {
        ArrayList<String> likedTags = new ArrayList<String>(Arrays.asList("seng202 teaching team", "red wine"));
        wineLoggingPopupService.submitLog(5,1,69, likedTags, "I      love them" );
        HashMap<String, Integer> result = logWineDao.getLikedTags(1, true);
        Assertions.assertTrue(result.containsKey("seng202 teaching team"));
        Assertions.assertEquals(2,result.get("seng202 teaching team"));
        Assertions.assertTrue(result.containsKey("red wine"));
        Assertions.assertEquals(2,result.get("red wine"));
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

    /**
     * Tests that {@link WineLoggingPopupService#getUId(User)} returns the right user id
     */
    @Test
    public void testGetUid() throws DuplicateEntryException {
        User user = new User("name", "username", 69);
        userDAO.add(user);
        int uid = wineLoggingPopupService.getUId(user);
        Assertions.assertEquals(1,uid);
    }
}
