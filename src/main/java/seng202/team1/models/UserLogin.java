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
<<<<<<<< HEAD:src/main/java/seng202/team1/models/UserLogin.java
     * Method that calls storeLogin from the UserLoginService model to save a user login.
========
     * Method that calls storeLogin from the UserLogin model to save a user login.
     * @param name The user's name
>>>>>>>> refs/remotes/origin/master:src/main/java/seng202/team1/services/UserLoginService.java
     * @param username The user's username
     * @param password The associated password
     * @return 1 if the account was successfully created, 0 if the username already exists, 2 if an error occurred
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
