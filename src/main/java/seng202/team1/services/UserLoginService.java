package seng202.team1.services;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import seng202.team1.models.User;
import seng202.team1.repository.DAOs.UserDAO;

import java.util.Objects;
import java.util.regex.*;

/**
 * Class to handle user login and register requests. Stores username and password as a hashed value as it
 * is more secure. Stores name as plain text.
 */
public class UserLoginService {
    private static final Logger LOG = LogManager.getLogger(UserLoginService.class);
    private final UserDAO userDAO = new UserDAO();

    /**
     * Takes a username and password input and stores it as long as the username is unique.
     * @param username username value to be stored
     * @param password password value to be stored
     * @param name name value to be stored
     * @return 1 if the account was successfully created, 0 if the username already exists, 2 if an error occurred
     */
    public int storeLogin(String name, String username, String password) {
        // check password requirements (8 chars min, letters and digits required)
        String regex = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d\\W]{8,}$";
        Pattern p = Pattern.compile(regex);

        if (name == null || name.isEmpty() ||
            username == null || username.isEmpty() ||
            password == null || !p.matcher(password).matches()) {
            System.out.println("oooh u made a mistake some where");
            return 2;
        }

        User newUser = new User(name, Objects.hash(username), Objects.hash(password));
        return userDAO.add(newUser);
    }

    /**
     * Method to check if the password given matches the value returned by the getPassword() function.
     * @param username value of username to search for
     * @param password value of password to match
     * @return true if the value returned by getPassword(username) is equal to the hashed value of password
     */
    public boolean checkLogin(String username, String password) {
        User user = userDAO.tryLogin(Objects.hash(username), Objects.hash(password));
        if (user != null) {
            User.setCurrentUser(user);
        }

        return user != null;
    }

    /**
     * Returns the name of the user using their username as reference.
     * @param username the username to search for
     * @return the name of the user
     */
    public String getName(String username) {
        return userDAO.getName(Objects.hash(username));
    }
}