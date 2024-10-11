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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ChallengeServiceTest {


    private ChallengeService challengeService;
    private ChallengeDAO challengeDAO;
    private UserDAO userDAO;

    private User user;

    @BeforeEach
    public void setUp() throws InstanceAlreadyExistsException, DuplicateEntryException {
        DatabaseManager.REMOVE_INSTANCE();
        DatabaseManager.initialiseInstanceWithUrl("jdbc:sqlite:./src/test/resources/test_database.db");
        DatabaseManager.getInstance().forceReset();
        user = new User(0, "test", Objects.hash("test"));

        User.setCurrentUser(user);
        challengeDAO = new ChallengeDAO();
        challengeService = new ChallengeService();
        userDAO = new UserDAO();
    }

    @Test
    public void varietyChallengeStarts() throws DuplicateEntryException {
        userDAO.add(user);
        challengeService.startChallengeVariety();
        assertEquals("Variety Challenge", challengeDAO.getChallengeForUser(user.getId()));
    }

    @Test
    public void decadesChallengeStarts() throws DuplicateEntryException {
        userDAO.add(user);
        challengeService.startChallengeYears();
        assertEquals("Time Travelling Challenge", challengeDAO.getChallengeForUser(user.getId()));
    }

    @Test
    public void redsChallengeStarts() throws DuplicateEntryException {
        userDAO.add(user);
        challengeService.startChallengeReds();
        assertEquals("Red Roulette Challenge", challengeDAO.getChallengeForUser(user.getId()));
    }

    @Test
    public void whitesChallengeStarts() throws DuplicateEntryException {
        userDAO.add(user);
        challengeService.startChallengeWhites();
        assertEquals("Great White Challenge", challengeDAO.getChallengeForUser(user.getId()));
    }

    @Test
    public void roseChallengeStarts() throws DuplicateEntryException {
        userDAO.add(user);
        challengeService.startChallengeRose();
        assertEquals("Rosè challenge", challengeDAO.getChallengeForUser(user.getId()));
    }

    @Test
    public void noActiveChallengeTest() throws DuplicateEntryException {
        userDAO.add(user);
        assertEquals(false, challengeService.activeChallenge());
    }

    @Test
    public void activeChallengeTest() throws DuplicateEntryException {
        userDAO.add(user);
        challengeDAO.startChallenge(0, "Variety Challenge");
        assertEquals(true, challengeService.activeChallenge());
    }

    @Test
    public void usersActiveChallengesNullTests() throws DuplicateEntryException {
        userDAO.add(user);
        assertEquals(null, challengeService.usersChallenge());
    }

    @Test
    public void challengeCompleteTest() throws DuplicateEntryException {
        userDAO.add(user);
        challengeService.startChallengeVariety();
        challengeService.challengeCompleted("Variety Challenge");
        assertEquals(null, challengeDAO.getChallengeForUser(user.getId()));

    }

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
