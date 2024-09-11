package seng202.team1.models;

import seng202.team1.services.UserLoginService;

/**
 * Service class to handle user logins. Calls the UserLoginService model and utilised methods within that to validate or
 * create user accounts
 * @author Isaac Macdonald, Caleb Cooper
 */
public class UserLogin {

    UserLoginService login = new UserLoginService();

    /**
     * Method that calls storeLogin from the UserLoginService model to save a user login.
     * @param username The user's username
     * @param password The associated password
     */
    public int createAccount(String name, String username, String password) {
        return login.storeLogin(name, username, password);
    }

    /**
     * Method that calls check from the UserLoginService model in order to validate that the password matches the saved value
     * associated to the given username.
     * @param username The user's username
     * @param password The associated password
     * @return true if the username and password match, false otherwise
     */
    public boolean validateAccount(String username, String password) {
        return login.checkLogin(username, password);
    }

    public String getName(String username) {
        return login.getName(username);
    }

    /**
     * Returns the encrypted username
     * @param username the raw unencrypted username
     * @return encrypted username
     */
    public String getEncryptedName(String username) {
        return login.getEncryptedUsername(username);
    }
}
