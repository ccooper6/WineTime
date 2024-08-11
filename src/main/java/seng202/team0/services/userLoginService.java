package seng202.team0.services;

import seng202.team0.models.UserLogin;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class userLoginService {

    UserLogin login = new UserLogin();

    public void createAccount(String username, String password) throws NoSuchPaddingException, IllegalBlockSizeException, IOException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {

        login.storeLogin(username, password);

    }

    public void validateAccount(String username, String password) throws NoSuchPaddingException, IllegalBlockSizeException, IOException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {

        login.checkLogin(username, password);

    }
}
