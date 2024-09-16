package seng202.team1.unittests.services;

import org.junit.jupiter.api.*;
import seng202.team1.exceptions.InstanceAlreadyExistsException;
import seng202.team1.services.UserLoginService;
import seng202.team1.repository.DatabaseManager;
import seng202.team1.repository.UserDAO;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserLoginServiceTest {

    private static UserDAO userDAO;
    private static UserLoginService userLoginService;

    /**
     * This creates a new database file for the test databases
     * @throws InstanceAlreadyExistsException this is in case there already is an instance
     */
    @BeforeAll
    static void setUp() throws InstanceAlreadyExistsException{
        DatabaseManager.REMOVE_INSTANCE();
        DatabaseManager.initialiseInstanceWithUrl("jdbc:sqlite:./src/test/resources/test_database.db");
        DatabaseManager.getInstance().forceReset();
        userDAO = new UserDAO();
        userLoginService = new UserLoginService();
    }

    @AfterEach
    void clearUser() {
        userDAO.delete(1);
    }

    /**
     * This test uses the login service to put a username in the database and confirms it didn't cause an error.
     */
    @Test
    public void testStoreGood() {
        String name = "Isaac";
        String username = "IsaacTheBest";
        String password = "password";
        int result = userLoginService.storeLogin(name, username, password);
        assertEquals(1, result);
    }

    @Test
    public void testStoreDuplicate() {
        String name = "Isaac";
        String username = "IsaacTheBest";
        String password = "password";
        String password2 = "password2";
        userLoginService.storeLogin(name, username, password);
        int result = userLoginService.storeLogin(name, username, password2);
        assertEquals(0, result);
    }

    @Test
    public void testTryLoginGood() {
        String name = "Isaac";
        String username = "IsaacTheBest";
        String password = "password";
        userLoginService.storeLogin(name, username, password);
        boolean wasInDB = userLoginService.checkLogin(username, password);
        Assertions.assertTrue(wasInDB);

    }

    @Test
    public void testTryLoginBad1() {
        String name = "Isaac";
        String username = "IsaacTheBest";
        String password = "password";
        String notPassword = "notPassword";
        userLoginService.storeLogin(name, username, password);
        boolean wasInDB = userLoginService.checkLogin(username, notPassword);
        Assertions.assertFalse(wasInDB);

    }

    @Test
    public void testTryLoginBad2() {
        String name = "Isaac";
        String username = "IsaacTheBest";
        String password = "password";
        String notUsername = "IsaacTheCoolest";
        userLoginService.storeLogin(name, username, password);
        boolean wasInDB = userLoginService.checkLogin(notUsername, password);
        Assertions.assertFalse(wasInDB);
    }

}
