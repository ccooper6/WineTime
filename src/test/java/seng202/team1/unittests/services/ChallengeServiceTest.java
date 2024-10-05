package seng202.team1.unittests.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import seng202.team1.exceptions.DuplicateEntryException;
import seng202.team1.exceptions.InstanceAlreadyExistsException;
import seng202.team1.gui.FXWrapper;
import seng202.team1.models.User;
import seng202.team1.repository.DAOs.ChallengeDAO;
import seng202.team1.repository.DatabaseManager;
import seng202.team1.repository.DAOs.UserDAO;
import seng202.team1.services.ChallengeService;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * test for challenge service class
 * @auhor Lydia Jackson.
 */
public class ChallengeServiceTest {


    private ChallengeService challengeService;

    private ChallengeDAO challengeDAO;
    private UserDAO userDAO;

    private User user;

    /**
     * Sets up {@link DatabaseManager} instance to use the test database
     * creates a test user with user id 0, sets the current user to be the test user.
     *
     * @throws InstanceAlreadyExistsException If {@link DatabaseManager#REMOVE_INSTANCE()} does not remove the instance
     */

    @BeforeEach
    public void setUp() throws InstanceAlreadyExistsException, DuplicateEntryException {
        DatabaseManager.REMOVE_INSTANCE();
        DatabaseManager.initialiseInstanceWithUrl("jdbc:sqlite:./src/test/resources/test_database.db");
        DatabaseManager.getInstance().forceReset();
        user = new User(0, "test", "test");
        User.setCurrenUser(user);
        challengeDAO = new ChallengeDAO();
        challengeService = new ChallengeService();
        userDAO = new UserDAO();
    }

    /**
     * tests the user id and variety challenge is stored in the active_challenges table in the database when
     * startChallengeVariety() is called.
     * @throws DuplicateEntryException
     */
    @Test
    public void challengeStarts() throws DuplicateEntryException {
        userDAO.add(user);
        challengeService.startChallengeVariety();
        assertEquals("Variety Challenge", challengeDAO.getChallengeForUser(0));
    }

    /**
     * tests that activeChallenge returns false if user has no active challenges.
     */
    @Test
    public void noActiveChallengeTest() throws DuplicateEntryException {
        userDAO.add(user);
        assertEquals(false, challengeService.activeChallenge());
    }

    /**
     * tests that active challenge returns true if user has variety challenge active.
     * @throws DuplicateEntryException
     */
    @Test
    public void activeChallengeTest() throws DuplicateEntryException {
        userDAO.add(user);
        challengeDAO.userToChallenge(0, "Variety Challenge");
        assertEquals(true, challengeService.activeChallenge());
    }

    /**
     * test the challengeWines method returns an array list of the correct wines stored in the database.
     */
    @Test
    public void getsTheChallengeWines() throws DuplicateEntryException {
        userDAO.add(user);
        challengeDAO.initaliseChallenge();
        challengeDAO.wineInChallenge();
        challengeDAO.userToChallenge(0, "Variety Challenge");
        ArrayList<Integer> testWines = new ArrayList<>();
        testWines.add(3);
        testWines.add(57);
        testWines.add(298);
        testWines.add(494);
        testWines.add(500);
        for (int i = 0; i < 5; i++)
            assertEquals(testWines.get(i), challengeService.challengeWines().get(i).getWineId());
    }

    /**
     * tests the usersChallenge returns null when the user has no challenges.
     * @throws DuplicateEntryException
     */
    @Test
    public void usersActiveChallengesNullTests() throws DuplicateEntryException {
        userDAO.add(user);
        assertEquals(null, challengeService.usersChallenge());
    }

    /**
     * tests the usersChallenge returns "Variety Challenge" when the user has started the variety challenge.
     * @throws DuplicateEntryException
     */
    @Test
    public void usersActiveChallengesNotNullTests() throws DuplicateEntryException {
        userDAO.add(user);
        challengeDAO.initaliseChallenge();
        challengeDAO.wineInChallenge();
        challengeDAO.userToChallenge(0, "Variety Challenge");
        assertEquals("Variety Challenge", challengeService.usersChallenge());
    }

}
