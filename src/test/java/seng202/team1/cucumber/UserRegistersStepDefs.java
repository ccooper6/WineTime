package seng202.team1.cucumber;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import seng202.team1.exceptions.InstanceAlreadyExistsException;
import seng202.team1.services.UserLoginService;
import seng202.team1.repository.DatabaseManager;

import static org.junit.jupiter.api.Assertions.*;


public class UserRegistersStepDefs {
    UserLoginService userLoginService;
    String name;
    String username;
    String password;
    int errorCodeFromRegister;

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
    public void iDoHaveAnAccount(String name, String username, String password) throws InstanceAlreadyExistsException {
        initialise();
        this.name = name;
        this.username = username;
        this.password = password;
        assertEquals(1, userLoginService.storeLogin(name, username, password));
    }

    @When("The user registers with the given credentials")
    public void iTryRegisterWithCorrectCredentials() {
        userLoginService.storeLogin(name, username, password);
    }

    @When("The user tries to re register with the same username")
    public void iHaveAnAccountAndReRegister()
    {
        errorCodeFromRegister = userLoginService.storeLogin(name, username, password);
    }

    @Then("The user is registered successfully and details are stored in the database")
    public void iAmRegisteredSuccessfully() {
        assertTrue(userLoginService.checkLogin(username, password));
    }

    @Then("There will be an error trying to log in")
    public void iCantReRegister()
    {
        assertEquals(0, errorCodeFromRegister);
    }
}
