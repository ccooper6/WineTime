package seng202.team0.models;

import java.io.*;
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
 * @author Caleb Cooper - https://www.youtube.com/watch?v=IG6mkDgKSTg
 */
public class UserLogin {
    private static final byte[] KEY = "1234567891112131".getBytes();
    private static final String ALGORITHM = "AES";
    private static final String FILENAME = "src/main/resources/logins/login.txt";

    /**
     * Takes a username and password input and stores it as long as the username is unique.
     * @param username username value to be stored
     * @param password password value to be stored
     */
    public void storeLogin(String username, String password) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(FILENAME, true));
            BufferedReader reader = new BufferedReader(new FileReader(FILENAME));
            String lookahead;
            while ((lookahead = reader.readLine()) != null) {
                String[] login = lookahead.split(",");
                if (login[0].equals(encrypt(username))) {
                    System.out.println("Username already exists, please try a different Username.");
                    return;
                }
            }
            writer.write(encrypt(username) + "," + Objects.hash(password) + "\n");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method to check if the password given matches the value returned by the getPassword() function.
     * @param username value of username to search for
     * @param password value of password to match
     * @return true if the value returned by getPassword(username) is equal to the hashed value of password
     */
    public boolean checkLogin(String username, String password) {
        return Objects.equals(getPassword(encrypt(username)), Objects.hash(password));
    }

    /**
     * Method that looks through the text file where the username and password pair is stored and returns the hashed
     * value of the password.
     * @param encryptedUsername the username to search for
     * @return null if no username could be found, otherwise returns the hashed password value
     */
    public Integer getPassword(String encryptedUsername) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(FILENAME));
            String lookahead;
            while ((lookahead = reader.readLine()) != null) {
                String[] login = lookahead.split(",");
                if (login[0].equals(encrypt(encryptedUsername))) {
                    return Integer.valueOf(login[1]);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Method that takes a string as input, for example a username, and returns a string that is no longer readable.
     * Uses a fixed key.
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
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Method that takes a string as input, for example a username, and returns a string that is then user readable.
     * Uses a fixed key.
     * @param text The text that needs to be decrypted
     * @return A string that contains the decrypted text
     */
    public String decrypt(String text) {
        try {
            SecretKeySpec key = new SecretKeySpec(KEY, ALGORITHM);
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] decrypted = cipher.doFinal(Base64.getDecoder().decode(text));
            return new String(decrypted);
        } catch (NoSuchPaddingException | NoSuchAlgorithmException | IllegalBlockSizeException | BadPaddingException
                 | InvalidKeyException e) {
            e.printStackTrace();
        }
        return null;
    }
}