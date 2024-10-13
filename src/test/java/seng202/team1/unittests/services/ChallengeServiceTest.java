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
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

public class ChallengeServiceTest {


    private ChallengeService challengeService;
    private ChallengeDAO challengeDAO;
    private UserDAO userDAO;

    private User user;

    @BeforeEach
    public void setUp() throws InstanceAlreadyExistsException, DuplicateEntryException {
        DatabaseManager.removeInstance();
        DatabaseManager.initialiseInstanceWithUrl("jdbc:sqlite:./src/test/resources/test_database.db");
        DatabaseManager.getInstance().forceReset();
        user = new User(0, "test", Objects.hash("test"));

        User.setCurrentUser(user);
        challengeDAO = new ChallengeDAO();
        challengeService = new ChallengeService();
        userDAO = new UserDAO();
    }

    @Test
    public void varietyChallengeStarts() {
        userDAO.add(user);
        challengeService.startChallengeVariety();
        assertEquals("Variety Challenge", challengeDAO.getChallengeForUser(user.getId()));
    }

    @Test
    public void decadesChallengeStarts() {
        userDAO.add(user);
        challengeService.startChallengeYears();
        assertEquals("Time Travelling Challenge", challengeDAO.getChallengeForUser(user.getId()));
    }

    @Test
    public void redsChallengeStarts() {
        userDAO.add(user);
        challengeService.startChallengeReds();
        assertEquals("Red Roulette Challenge", challengeDAO.getChallengeForUser(user.getId()));
    }

    @Test
    public void whitesChallengeStarts() {
        userDAO.add(user);
        challengeService.startChallengeWhites();
        assertEquals("Great White Challenge", challengeDAO.getChallengeForUser(user.getId()));
    }

    @Test
    public void roseChallengeStarts() {
        userDAO.add(user);
        challengeService.startChallengeRose();
        assertEquals("Rosè challenge", challengeDAO.getChallengeForUser(user.getId()));
    }

    @Test
    public void noActiveChallengeTest() {
        userDAO.add(user);
        assertFalse(challengeService.activeChallenge());
    }

    @Test
    public void activeChallengeTest() {
        userDAO.add(user);
        challengeDAO.startChallenge(0, "Variety Challenge");
        assertTrue(challengeService.activeChallenge());
    }

    @Test
    public void usersActiveChallengesNullTests() {
        userDAO.add(user);
        assertNull(challengeService.usersChallenge());
    }

    @Test
    public void challengeCompleteTest() {
        userDAO.add(user);
        challengeService.startChallengeVariety();
        challengeService.challengeCompleted("Variety Challenge");
        assertNull(challengeDAO.getChallengeForUser(user.getId()));

    }

    @Test
    public void challengeWinesTest() {
        userDAO.add(user);
        ArrayList<Integer> wineIDs = new ArrayList<>(Arrays.asList(1, 2, 3, 4));

        challengeService.userActivatesChallenge(user.getId(), "test", wineIDs);

        List<Wine> wines = challengeService.challengeWines();

        assertNotNull(wines);
        assertEquals(4, wines.size());
        for (int i= 0; i < wines.size(); i++) {
            assertEquals(i + 1, wines.get(i).getID());
        }
    }
}
