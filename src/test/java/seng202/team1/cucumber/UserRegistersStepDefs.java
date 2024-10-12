package seng202.team1.cucumber;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import seng202.team1.exceptions.InstanceAlreadyExistsException;
import seng202.team1.services.UserLoginService;
import seng202.team1.repository.DatabaseManager;

import static org.junit.jupiter.api.Assertions.*;


public class UserRegistersStepDefs {
    private UserLoginService userLoginService;
    private String name;
    private String username;
    private String password;

    public void initialise() throws InstanceAlreadyExistsException {
        DatabaseManager.REMOVE_INSTANCE();
        DatabaseManager.initialiseInstanceWithUrl("jdbc:sqlite:./src/test/resources/test_database.db");
        DatabaseManager.getInstance().forceReset();
        userLoginService = new UserLoginService();
    }

    @Given("The user with name {string} doesn't currently have an account associated to the username {string} and password {string}")
    public void iDoNotHaveAnAccount(String name, String username, String password) throws InstanceAlreadyExistsException {
        initialise();
        this.name = name;
        this.username = username;
        this.password = password;
        assertFalse(userLoginService.checkLogin(username, password));
    }

    @Given("The user with name {string} does already have an account associated to the username {string} and password {string}")
    public void iDoHaveAccount(String name, String username, String password) throws InstanceAlreadyExistsException {
        initialise();
        userLoginService.storeLogin(name, username, password);
        assertTrue(userLoginService.checkLogin(username, password));
    }

    @When("The user registers with the given credentials")
    public void iRegisterWithUsernameAndPassword() {
        userLoginService.storeLogin(name, username, password);
    }

    @When("The user tries to re register with the same username")
    public void iReregistersWithUsernameAndPassword() {
        userLoginService.storeLogin(name, username, password);

    }

    @Then("The user is registered successfully and details are stored in the database")
    public void iAmRegisteredSuccessfully() {
        assertTrue(userLoginService.checkLogin(username, password));
    }

    @Then("The user will not be re-registered")
    public void iNotReRegistered() {
        assertEquals(0, userLoginService.storeLogin(name, username, password));

    }
}
