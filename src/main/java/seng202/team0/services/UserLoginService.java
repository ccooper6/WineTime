package seng202.team0.services;

import seng202.team0.models.UserLogin;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;


/**
 * Service class to handle user logins. Calls the UserLogin model and utilised methods within that to validate or
 * create user accounts
 * @author Issac Macdonald, Caleb Cooper
 */
public class UserLoginService {

    UserLogin login = new UserLogin();

    /**
     * Method that calls storeLogin from the UserLogin model to save a user login.
     * @param username The user's username
     * @param password The associated password
     */
    public void createAccount(String username, String password) throws NoSuchPaddingException,
            IllegalBlockSizeException, IOException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        login.storeLogin(username, password);
    }

    /**
     * Method that calls check from the UserLogin model in order to validate that the password matches the saved value
     * associated to the given username.
     * @param username The user's username
     * @param password The associated password
     * @return true if the username and password match, false otherwise
     */
    public boolean validateAccount(String username, String password) throws NoSuchPaddingException,
            IllegalBlockSizeException, IOException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        return login.checkLogin(username, password);
    }
}
