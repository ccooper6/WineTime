package seng202.team1.unittests.services;

import org.junit.jupiter.api.*;
import seng202.team1.exceptions.InstanceAlreadyExistsException;
import seng202.team1.services.UserLoginService;
import seng202.team1.repository.DatabaseManager;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserLoginServiceTest {
    private static UserLoginService userLoginService;

    /**
     * This creates a new database file for the test databases
     * @throws InstanceAlreadyExistsException this is in case there already is an instance
     */
    @BeforeEach
    void setUp() throws InstanceAlreadyExistsException{
        DatabaseManager.REMOVE_INSTANCE();
        DatabaseManager.initialiseInstanceWithUrl("jdbc:sqlite:./src/test/resources/test_database.db");
        DatabaseManager.getInstance().forceReset();
        userLoginService = new UserLoginService();
    }

    /**
     * This test uses the login service to put a username in the database and confirms it didn't cause an error.
     */
    @Test
    public void testStorePass() {
        String name = "Isaac";
        String username = "IsaacTheBest";
        String password = "check123";

        assertEquals(1, userLoginService.storeLogin(name, username, password));
    }

    @Test
    public void testStorePassCapitalLetters() {
        String name = "Isaac";
        String username = "IsaacTheBest";
        String password = "CHECK123";

        assertEquals(1, userLoginService.storeLogin(name, username, password));
    }

    @Test
    public void testStoreDuplicate() {
        String name = "Isaac";
        String username = "IsaacTheBest";
        String password = "password1";
        String password2 = "password2";
        userLoginService.storeLogin(name, username, password);
        int result = userLoginService.storeLogin(name, username, password2);
        assertEquals(0, result);
    }

    @Test
    public void testStoreNoName() {
        String nameNull = null;
        String nameEmpty = "";
        String username = "username";
        String password = "password";

        assertEquals(2, userLoginService.storeLogin(nameNull, username, password));
        assertEquals(2, userLoginService.storeLogin(nameEmpty, username, password));
    }

    @Test
    public void testStoreNoUsername() {
        String name = "testName";
        String usernameNull = null;
        String usernameEmpty = "";
        String password = "password1";

        assertEquals(2, userLoginService.storeLogin(name, usernameNull, password));
        assertEquals(2, userLoginService.storeLogin(name, usernameEmpty, password));
    }

    @Test
    public void testStoreNoPassword() {
        String name = "testName";
        String username = "testUser";
        String password = null;

        assertEquals(2, userLoginService.storeLogin(name, username, password));
    }

    @Test
    public void testStoreShortPassword() {
        String name = "testName";
        String username = "testUser";
        String password = "abc123";

        assertEquals(2, userLoginService.storeLogin(name, username, password));
    }

    @Test
    public void testStorePasswordNoLetters() {
        String name = "testName";
        String username = "testUser";
        String password = "12345678";

        assertEquals(2, userLoginService.storeLogin(name, username, password));
    }

    @Test
    public void testStorePasswordNoNumbers() {
        String name = "testName";
        String username = "testUser";
        String password = "abcdefgh";

        assertEquals(2, userLoginService.storeLogin(name, username, password));
    }

    @Test
    public void testTryLoginGood() {
        String name = "Isaac";
        String username = "IsaacTheBest";
        String password = "password1";

        assertEquals(1, userLoginService.storeLogin(name, username, password));
        boolean wasInDB = userLoginService.checkLogin(username, password);
        Assertions.assertTrue(wasInDB);

    }

    @Test
    public void testTryLoginBad1() {
        String name = "Isaac";
        String username = "IsaacTheBest";
        String password = "password1";
        String notPassword = "notPassword";
        userLoginService.storeLogin(name, username, password);
        boolean wasInDB = userLoginService.checkLogin(username, notPassword);
        Assertions.assertFalse(wasInDB);

    }

    @Test
    public void testTryLoginBad2() {
        String name = "Isaac";
        String username = "IsaacTheBest";
        String password = "password1";
        String notUsername = "IsaacTheCoolest";
        userLoginService.storeLogin(name, username, password);
        boolean wasInDB = userLoginService.checkLogin(notUsername, password);
        Assertions.assertFalse(wasInDB);
    }

    @Test
    void testGetName() {
        String name = "Isaac";
        String username = "IsaacTheBest";
        String password = "password1";
        userLoginService.storeLogin(name, username, password);
        String nameFromDB = userLoginService.getName(username);
        assertEquals(name, nameFromDB);
    }
}
