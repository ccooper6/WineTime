package seng202.team0.services;

import seng202.team0.models.UserLogin;

/**
 * Service class to handle user logins. Calls the UserLogin model and utilised methods within that to validate or
 * create user accounts
 * @author Isaac Macdonald, Caleb Cooper
 */
public class UserLoginService {

    UserLogin login = new UserLogin();

    /**
     * Method that calls storeLogin from the UserLogin model to save a user login.
     * @param name The user's name
     * @param username The user's username
     * @param password The associated password
     * @return 1 if the account was successfully created, 0 if the username already exists, 2 if an error occurred
     */
    public int createAccount(String name, String username, String password) {
        return login.storeLogin(name, username, password);
    }

    /**
     * Method that calls check from the UserLogin model in order to validate that the password matches the saved value
     * associated to the given username.
     * @param username The user's username
     * @param password The associated password
     * @return true if the username and password match, false otherwise
     */
    public boolean validateAccount(String username, String password) {
        return login.checkLogin(username, password);
    }

    /**
     * Method that calls getName from the UserLogin model to return the name of the user associated with the given
     * username.
     * @param username The user's username
     * @return The name of the user
     */
    public String getName(String username) {
        return login.getName(username);
    }

    /**
     * Returns the encrypted username.
     * @param username the raw unencrypted username
     * @return encrypted username
     */
    public String getEncryptedName(String username) {
        return login.getEncryptedUsername(username);
    }
}
