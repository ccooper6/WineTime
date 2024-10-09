package seng202.team1.cucumber;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import seng202.team1.exceptions.InstanceAlreadyExistsException;
import seng202.team1.models.User;
import seng202.team1.repository.DAOs.ChallengeDAO;
import seng202.team1.repository.DAOs.UserDAO;
import seng202.team1.repository.DatabaseManager;
import seng202.team1.services.ChallengeService;

import static org.junit.jupiter.api.Assertions.*;

public class ChallengeStepDefs {

    private ChallengeService challengeService;
    private ChallengeDAO challengeDAO;
    private UserDAO userDAO;
    private User user;


    public void initialise() throws InstanceAlreadyExistsException {
        DatabaseManager.REMOVE_INSTANCE();
        DatabaseManager.initialiseInstanceWithUrl("jdbc:sqlite:./src/test/resources/test_database.db");
        DatabaseManager.getInstance().forceReset();
        challengeService = new ChallengeService();
        challengeDAO = new ChallengeDAO();
        user = new User(0, "test", "test");
        User.setCurrenUser(user);
        userDAO = new UserDAO();
        userDAO.add(user);
    }

    @Given("the user has no active challenges")
    public void iHasNoActiveChallenge() throws InstanceAlreadyExistsException {
        initialise();
        assertFalse(challengeService.activeChallenge()); }

    @Given("user has an active challenge")
    public void iHasActiveChallenge() throws InstanceAlreadyExistsException {
        initialise();
        challengeService.startChallengeVariety();
        assertTrue(challengeService.activeChallenge());
    }

    @When("the user starts the variety challenge")
    public void iStartVarietyChallenge() { challengeService.startChallengeVariety(); }

    @When("the user starts time the travellers challenge")
    public void iStartYearsChallenge() { challengeService.startChallengeYears(); }

    @When("the user starts the red challenge")
    public void iStartRedChallenge() { challengeService.startChallengeReds(); }

    @When("the user starts the white challenge")
    public void iStartWhiteChallenge() { challengeService.startChallengeWhites(); }

    @When("the user starts the rose challenge")
    public void iStartRoseChallenge() { challengeService.startChallengeRose(); }

    @When("the user completes challenge")
    public void iCompletesChallenge() { challengeService.challengeCompleted("Variety Challenge"); }

    @Then("5 wines of different variety are displayed on the profile")
    public void varietyWineDisplayed() {
        assertEquals(5, challengeService.challengeWines().size());
        assertEquals("Variety Challenge", challengeDAO.getChallengeForUser(user.getId()));
    }

    @Then("5 wines of different vintage are displayed on the profile")
    public void yearWineDisplayed() {
        assertEquals(5, challengeService.challengeWines().size());
        assertEquals("Time Travelling Challenge", challengeDAO.getChallengeForUser(user.getId()));
    }

    @Then("5 different red are displayed on the profile")
    public void redWineDisplayed() {
        assertEquals(5, challengeService.challengeWines().size());
        assertEquals("Red Roulette Challenge", challengeDAO.getChallengeForUser(user.getId()));
    }

    @Then("5 different white are displayed on the profile")
    public void whiteWineDisplayed() {
        assertEquals(5, challengeService.challengeWines().size());
        assertEquals("Great White Challenge", challengeDAO.getChallengeForUser(user.getId()));
    }

    @Then("5 different rose are displayed on the profile")
    public void roseWineDisplayed() {
        assertEquals(5, challengeService.challengeWines().size());
        assertEquals("Rosè challenge", challengeDAO.getChallengeForUser(user.getId()));
    }

    @Then("challenge is removed from the users active challenges")
    public void challengeRemoved() { assertEquals(null, challengeDAO.getChallengeForUser(user.getId())); }


}
