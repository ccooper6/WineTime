package seng202.team1.cucumber;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import seng202.team1.exceptions.InstanceAlreadyExistsException;
import seng202.team1.models.UserLogin;
import seng202.team1.repository.DatabaseManager;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class UserRegistersStepDefs {
    UserLogin userLogin;
    String name;
    String username;
    String password;

    public void initialise() throws InstanceAlreadyExistsException {
        DatabaseManager.REMOVE_INSTANCE();
        DatabaseManager.initialiseInstanceWithUrl("jdbc:sqlite:./src/test/resources/test_database.db");
        DatabaseManager.getInstance().forceReset();
        userLogin = new UserLogin();
    }

    @Given("The user with name {string} doesn't currently have an account associated to the username {string} and password {string}")
    public void iDoNotHaveAnAccount(String name, String username, String password) throws InstanceAlreadyExistsException {
        initialise();
        this.name = name;
        this.username = username;
        this.password = password;
        assertFalse(userLogin.validateAccount(username, password));
    }

    @When("The user registers with the given credentials")
    public void iRegisterWithUsernameAndPassword() {
        userLogin.createAccount(name, username, password);
    }

    @Then("The user is registered successfully and details are stored in the database")
    public void iAmRegisteredSuccessfully() {
        assertTrue(userLogin.validateAccount(username, password));
    }
}
