package seng202.team0.unittests.services;

import org.junit.jupiter.api.Test;
import seng202.team0.services.UserLoginService;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserDAOTest {

    UserLoginService userLoginService = new UserLoginService();

    /**
     * This test uses the login service to put a username in the database and confirms it didn't cause an error.
     */
    @Test
    public void testStoreGood(){

        String username = "abcd";
        String password = "1234";
        int result = userLoginService.createAccount(username, password);
        assertEquals(1, result);

    }

    /**
     * This test tries to put the same user in the database twice, and checks the method causes a duplicate error.
     */
    @Test
    public void testStoreDuplicate(){

        String username = "abcd";
        String password = "1234";
        userLoginService.createAccount(username, password);
        int result = userLoginService.createAccount(username, password);
        assertEquals(0, result);

    }

    /**
     * This test puts a user in the database and then checks that the user is in the database
     */
    @Test
    public void testValidateLoginTrue(){

    }


}
