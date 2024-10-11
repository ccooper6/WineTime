package seng202.team1.cucumber;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import seng202.team1.exceptions.InstanceAlreadyExistsException;
import seng202.team1.models.User;
import seng202.team1.repository.DatabaseManager;
import seng202.team1.services.UserLoginService;

import static org.junit.jupiter.api.Assertions.*;


public class UserLogsInStepDefs {
    UserLoginService userLoginService;
    User user;

    public void initialise() {
        DatabaseManager.REMOVE_INSTANCE();
        try {
            DatabaseManager.initialiseInstanceWithUrl("jdbc:sqlite:./src/test/resources/test_database.db");
        } catch (InstanceAlreadyExistsException e) {
            fail("Could not initialise database");
        }
        DatabaseManager.getInstance().forceReset();

        User.setCurrentUser(null);

        userLoginService = new UserLoginService();
    }

    @Given("The user with name {string} doesn't have an account")
    public void iDontHaveAnAccount(String name) {
        initialise();
    }

    @Given("The user with name {string} has an account with username {string} and password {string}")
    public void alreadyHasAnAccount(String name, String username, String password)
    {
        initialise();

        userLoginService.storeLogin(name, username, password);
    }

    @When("The user logs in with username {string} and password {string}")
    public void theUserLogsInWithUsernameAndPassword(String username, String password) {
        userLoginService.checkLogin(username, password);

        user = User.getCurrentUser();
    }

    @Then("The user isn't logged in")
    public void iAmNotRegistered() {
        assertNull(user);
    }

    @Then("The user is logged in as {string}")
    public void iAmRegisteredSuccessfully(String name) {
        assertNotNull(user);
        assertEquals(name, user.getName());
    }
}
