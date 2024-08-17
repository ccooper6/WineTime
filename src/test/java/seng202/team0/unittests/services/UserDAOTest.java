package seng202.team0.unittests.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import seng202.team0.exceptions.DuplicateEntryException;
import seng202.team0.exceptions.InstanceAlreadyExistsException;
import seng202.team0.models.User;
import seng202.team0.models.UserLogin;
import seng202.team0.services.UserLoginService;
import seng202.team0.repository.DatabaseManager;
import seng202.team0.repository.UserDAO;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserDAOTest {

    static DatabaseManager databaseManager;
    static UserDAO userDAO;

    private UserLogin userLogin = new UserLogin();

    /**
     * This creates a new database file for the test databases
     * @throws InstanceAlreadyExistsException this is in case there already is an instance
     */
    @BeforeAll
    static void setUp() throws InstanceAlreadyExistsException{
        DatabaseManager.REMOVE_INSTANCE();
        databaseManager = DatabaseManager.initialiseInstanceWithUrl("jdbc:sqlite:./src/test/resources/test_database.db");
        userDAO = new UserDAO();
    }

    @BeforeEach
    void resetDB(){
        databaseManager.resetDB();
    }

    /**
     * This test uses the login service to put a username in the database and confirms it didn't cause an error.
     */
    @Test
    public void testStoreGood() throws DuplicateEntryException {

        String username = "IsaacTheBest";
        String password = "password";
        User user = new User(userLogin.encrypt(username), Objects.hash(password));
        int result = userDAO.add(user);
        assertEquals(1, result);

    }

    @Test
    public void testStoreDuplicate() throws DuplicateEntryException {
        String username = "IsaacTheBest";
        String password = "password";
        String password2 = "password2";
        User user = new User(userLogin.encrypt(username), Objects.hash(password));
        User user1 = new User(userLogin.encrypt(username), Objects.hash(password2));
        userDAO.add(user);
        int result = userDAO.add(user);
        assertEquals(0, result);
    }


}
