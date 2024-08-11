package seng202.team0.models;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

public class UserLogin {

    public static void storePassword(String username, String password) throws IOException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        BufferedWriter writer = new BufferedWriter(new FileWriter("src/main/resources/logins/login.txt",true));

        writer.write(username + "," + encryptPassword(password) + "\n");
        writer.close();
    }

    public static String getPassword(String username) throws IOException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        BufferedReader reader = new BufferedReader(new FileReader("src/main/resources/logins/login.txt"));
        String lookahead;
        while((lookahead = reader.readLine()) != null) {
            String[] login = lookahead.split(",");
            if (login[0].equals(username)) {
                return decryptPassword(login[1]);
            }
        }
        return null;
    }

    public static String encryptPassword(String password) throws NoSuchPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
        byte[] keyBytes = "1234567891112131".getBytes();
        SecretKeySpec key = new SecretKeySpec(keyBytes, "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] encrypted = cipher.doFinal(password.getBytes());
        return Base64.getEncoder().encodeToString(encrypted);
    }

    public static String decryptPassword(String password) throws NoSuchPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
        byte[] keyBytes = "1234567891112131".getBytes();
        SecretKeySpec key = new SecretKeySpec(keyBytes, "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] decrypted = cipher.doFinal(Base64.getDecoder().decode(password));
        return new String(decrypted);
    }
}