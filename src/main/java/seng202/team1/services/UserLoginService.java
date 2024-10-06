package seng202.team1.services;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import seng202.team1.exceptions.DuplicateEntryException;
import seng202.team1.models.User;
import seng202.team1.repository.DAOs.UserDAO;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Objects;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

/**
 * Class to handle user login and register requests. Stores username as an encrypted value using AES encryption
 * and stores password as a hashed value as it is more secure.
 * @author Caleb Cooper
 */
public class UserLoginService {
    private static final byte[] KEY = "1234567891112131".getBytes();
    private static final String ALGORITHM = "AES";
    private static final Logger log = LogManager.getLogger(UserLoginService.class);
    private final UserDAO userDAO = new UserDAO();

    /**
     * Takes a username and password input and stores it as long as the username is unique.
     * @param username username value to be stored
     * @param password password value to be stored
     * @param name name value to be stored
     * @return 1 if the account was successfully created, 0 if the username already exists, 2 if an error occurred
     */
    // TODO whats actually happening here?
    public int storeLogin(String name, String username, String password) {
        try {
            User newUser = new User(name, encrypt(username), Objects.hash(password));
            return userDAO.add(newUser); // 1 = Account successfully created, 0 = User already exist, 2 = ERROR!
        } catch (DuplicateEntryException e) {
            log.error("Error in UserLoginService.storeLogin(): The user " + username + " already exists in the database.");
            return 2;
        }
    }

    /**
     * Method to check if the password given matches the value returned by the getPassword() function.
     * @param username value of username to search for
     * @param password value of password to match
     * @return true if the value returned by getPassword(username) is equal to the hashed value of password
     */
    public boolean checkLogin(String username, String password) {
        User user =  userDAO.tryLogin(encrypt(username), Objects.hash(password));
        if (user != null) {
            User.setCurrenUser(user);
        }

        return user != null;
    }

    /**
     * Returns the name of the user using their username as reference.
     * @param username the username to search for
     * @return the name of the user
     */
    public String getName(String username) {
        return userDAO.getName(encrypt(username));
    }

    /**
     * Returns the encrypted username.
     * @param username the raw unencrypted username
     * @return encrypted username
     */
    // TODO remove since never used
    public String getEncryptedUsername(String username) {
        return encrypt(username);
    }


    /**
     * Method that takes a string as input, for example a username, and returns a string that is no longer readable.
     * Uses a fixed key. Functionality
     * @param text The text that needs to be encrypted
     * @return A string that contains the encrypted text
     */
    public String encrypt(String text) {
        try {
            SecretKeySpec key = new SecretKeySpec(KEY, ALGORITHM);
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] encrypted = cipher.doFinal(text.getBytes());
            return Base64.getEncoder().encodeToString(encrypted);
        } catch (NoSuchPaddingException | NoSuchAlgorithmException | IllegalBlockSizeException | BadPaddingException
                 | InvalidKeyException e) {
            log.error("Error in UserLoginService.encrypt(): " + e.getMessage());
        }
        return null;
    }
}