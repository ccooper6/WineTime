package seng202.team1.unittests.services;

import org.junit.jupiter.api.*;
import seng202.team1.exceptions.InstanceAlreadyExistsException;
import seng202.team1.services.UserLoginService;
import seng202.team1.repository.DatabaseManager;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserLoginServiceTest {
    private static UserLoginService userLoginService;

    @BeforeAll
    static void setUp() throws InstanceAlreadyExistsException{
        DatabaseManager.REMOVE_INSTANCE();
        DatabaseManager.initialiseInstanceWithUrl("jdbc:sqlite:./src/test/resources/test_database.db");
        DatabaseManager.getInstance().forceReset();
        userLoginService = new UserLoginService();
    }

    @Test
    public void testTryRegisterUserCorrectScenario() {
        String name = "Isaac";
        String username = "IsaacTheBest";
        String password = "password";
        int result = userLoginService.storeLogin(name, username, password);
        assertEquals(1, result);
    }

    @Test
    public void testTryRegisterDuplicateUsername() {
        String name = "Isaac";
        String username = "IsaacTheBest";
        String password = "password";
        String password2 = "password2";
        userLoginService.storeLogin(name, username, password);
        int result = userLoginService.storeLogin(name, username, password2); // returns a 0 code if the username already exists in the db
        assertEquals(0, result);
    }

    @Test
    public void testTryLoginCorrectCredentials() {
        String name = "Isaac";
        String username = "IsaacTheBest";
        String password = "password";
        userLoginService.storeLogin(name, username, password);
        boolean wasInDB = userLoginService.checkLogin(username, password);
        Assertions.assertTrue(wasInDB);

    }

    @Test
    public void testTryLoginWithIncorrectPassword() {
        String name = "Isaac";
        String username = "IsaacTheBest";
        String password = "password";
        String notPassword = "notPassword";
        userLoginService.storeLogin(name, username, password);
        boolean wasInDB = userLoginService.checkLogin(username, notPassword);
        Assertions.assertFalse(wasInDB);

    }

    @Test
    public void testTryLoginWithWrongUsername() {
        String name = "Isaac";
        String username = "IsaacTheBest";
        String password = "password";
        String notUsername = "IsaacTheCoolest";
        userLoginService.storeLogin(name, username, password);
        boolean wasInDB = userLoginService.checkLogin(notUsername, password);
        Assertions.assertFalse(wasInDB);
    }

    @Test
    void testGetName() {
        String name = "Isaac";
        String username = "IsaacTheBest";
        String password = "password";
        userLoginService.storeLogin(name, username, password);
        String nameFromDB = userLoginService.getName(username);
        assertEquals(name, nameFromDB);
    }
}
