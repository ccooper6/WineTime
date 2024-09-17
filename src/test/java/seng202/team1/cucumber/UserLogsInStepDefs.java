package seng202.team1.cucumber;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import seng202.team1.exceptions.InstanceAlreadyExistsException;
import seng202.team1.models.UserLogin;
import seng202.team1.repository.DatabaseManager;

import javax.xml.crypto.Data;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class UserLogsInStepDefs {
    UserLogin userLogin;
    String name;
    String username;
    String password;
    boolean loginSuccess;

    public void initialise() throws InstanceAlreadyExistsException {
        DatabaseManager.REMOVE_INSTANCE();
        DatabaseManager.initialiseInstanceWithUrl("jdbc:sqlite:./src/test/resources/test_database.db");
        DatabaseManager.getInstance().forceReset();
        userLogin = new UserLogin();
    }

    @Given("The user with name {string} does already have an account associated to the username {string} and password {string}")
    public void iDoHaveAnAccount(String name, String username, String password) throws InstanceAlreadyExistsException {
        initialise();
        this.name = name;
        this.username = username;
        this.password = password;
        assertEquals(1, userLogin.createAccount(name, username, password));
    }

    @When("The user logs in with the given credentials")
    public void iTryLoginWith() {
        loginSuccess = userLogin.validateAccount(username, password);
    }

    @Then("The user is logged in successfully")
    public void iAmRegisteredSuccessfully() {
        assertTrue(loginSuccess);
    }
}
