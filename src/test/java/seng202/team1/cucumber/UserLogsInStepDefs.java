package seng202.team1.cucumber;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import seng202.team1.exceptions.InstanceAlreadyExistsException;
import seng202.team1.repository.DatabaseManager;
import seng202.team1.services.UserLoginService;

import javax.xml.crypto.Data;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;


public class UserLogsInStepDefs {
    UserLoginService userLoginService;
    String name;
    String username;
    String password;
    boolean loginSuccess;

    int errorCodeFromRegister;

    public void initialise() throws InstanceAlreadyExistsException {
        DatabaseManager.REMOVE_INSTANCE();
        DatabaseManager.initialiseInstanceWithUrl("jdbc:sqlite:./src/test/resources/test_database.db");
        DatabaseManager.getInstance().forceReset();
        userLoginService = new UserLoginService();
    }

    @Given("The user with name {string} does already have an account associated to the username {string} and password {string}")
    public void iDoHaveAnAccount(String name, String username, String password) throws InstanceAlreadyExistsException {
        initialise();
        this.name = name;
        this.username = username;
        this.password = password;
        assertEquals(1, userLoginService.storeLogin(name, username, password));
    }

    @Given("The user with name {string} doesn't have an account associated to the username {string} and password {string}")
    public void iDontHaveAnAccount(String name, String username, String password) throws InstanceAlreadyExistsException {
        initialise();
        this.name = name;
        this.username = username;
        this.password = password;
    }

    @When("The user logs in with the given credentials")
    public void iTryLoginWithCorrectCredientials() {
        loginSuccess = userLoginService.checkLogin(username, password);
    }

    @When("The user tries to re register with the same username")
    public void iHaveAnAccountAndReRegister()
    {
         errorCodeFromRegister = userLoginService.storeLogin(name, username, password);
    }

    @When("The user logs in with the wrong password")
    public void iTryLoginWithWrongPassword() {
        loginSuccess = userLoginService.checkLogin(username, password+"IsaacIsCool");
    }

    @When("The user logs in with the wrong username")
    public void iTryLoginWithWrongUsername() {
        loginSuccess = userLoginService.checkLogin(username +"IsaacIsCool", password);
    }

    @When("The user logs in with the assumed credentials")
    public void iTryLoginWithNoAccount() {
        loginSuccess = userLoginService.checkLogin(username, password);
    }

    @Then("The user isn't logged in successfully")
    public void iAmNotRegistered() {
        assertFalse(loginSuccess);
    }

    @Then("The user is logged in successfully")
    public void iAmRegisteredSuccessfully() {
        assertTrue(loginSuccess);
    }

    @Then("There will be an error trying to log in")
    public void iCantReRegister()
    {
        assertEquals(0, errorCodeFromRegister);
    }

}
