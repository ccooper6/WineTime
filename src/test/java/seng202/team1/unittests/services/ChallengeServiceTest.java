package seng202.team1.unittests.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import seng202.team1.exceptions.DuplicateEntryException;
import seng202.team1.exceptions.InstanceAlreadyExistsException;
import seng202.team1.models.User;
import seng202.team1.models.Wine;
import seng202.team1.repository.DAOs.ChallengeDAO;
import seng202.team1.repository.DatabaseManager;
import seng202.team1.repository.DAOs.UserDAO;
import seng202.team1.services.ChallengeService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * test for challenge service class
 * @author Lydia Jackson.
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
    public void varietyChallengeStarts() throws DuplicateEntryException {
        userDAO.add(user);
        challengeService.startChallengeVariety();
        assertEquals("Variety Challenge", challengeDAO.getChallengeForUser(user.getId()));
    }

    /**
     * tests the user id and variety challenge is stored in the active_challenges table in the database when
     * startChallengeVariety() is called.
     * @throws DuplicateEntryException
     */
    @Test
    public void decadesChallengeStarts() throws DuplicateEntryException {
        userDAO.add(user);
        challengeService.startChallengeYears();
        assertEquals("Time Travelling Challenge", challengeDAO.getChallengeForUser(user.getId()));
    }

    /**
     * tests the user id and variety challenge is stored in the active_challenges table in the database when
     * startChallengeVariety() is called.
     * @throws DuplicateEntryException
     */
    @Test
    public void redsChallengeStarts() throws DuplicateEntryException {
        userDAO.add(user);
        challengeService.startChallengeReds();
        assertEquals("Red Roulette Challenge", challengeDAO.getChallengeForUser(user.getId()));
    }

    /**
     * tests the user id and variety challenge is stored in the active_challenges table in the database when
     * startChallengeVariety() is called.
     * @throws DuplicateEntryException
     */
    @Test
    public void whitesChallengeStarts() throws DuplicateEntryException {
        userDAO.add(user);
        challengeService.startChallengeWhites();
        assertEquals("Great White Challenge", challengeDAO.getChallengeForUser(user.getId()));
    }

    /**
     * tests the user id and variety challenge is stored in the active_challenges table in the database when
     * startChallengeVariety() is called.
     * @throws DuplicateEntryException
     */
    @Test
    public void roseChallengeStarts() throws DuplicateEntryException {
        userDAO.add(user);
        challengeService.startChallengeRose();
        assertEquals("Rose challenge", challengeDAO.getChallengeForUser(user.getId()));
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
        challengeDAO.startChallenge(0, "Variety Challenge");
        assertEquals(true, challengeService.activeChallenge());
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
     * test that challenge completed method clears the challenge from the users active challenge in the database
     * @throws DuplicateEntryException
     */
    @Test
    public void challengeCompleteTest() throws DuplicateEntryException {
        userDAO.add(user);
        challengeService.startChallengeVariety();
        challengeService.challengeCompleted("Variety Challenge");
        assertEquals(null, challengeDAO.getChallengeForUser(user.getId()));

    }


    /**
     * tests  the method to add wines to a challenge and activate the challenge for the user
     * @throws DuplicateEntryException
     */
    @Test
    public void challengeWinesTest() throws DuplicateEntryException {
        userDAO.add(user);
        ArrayList<Integer> wineids = new ArrayList<>(Arrays.asList(1, 2, 3, 4));

        challengeDAO.userActivatesChallenge(user.getId(), "test", wineids);

        List<Wine> wines = challengeService.challengeWines();

        assertNotNull(wines);
        assertEquals(4, wines.size());
        for (int i= 0; i < wines.size(); i++) {
            assertEquals(i + 1, wines.get(i).getWineId());
        }


    }


}
