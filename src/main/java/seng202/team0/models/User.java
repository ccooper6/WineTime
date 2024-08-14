package seng202.team0.models;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class User {
    private String encryptedUserName;
    private String hashedPassword;
    private ArrayList<Wine> favouritedWines;
    private ArrayList<Wine> dislikedWines;
    private ArrayList<String> favouritedTags;

    public User(String userName, String hashedPassword) {
        this.encryptedUserName = userName;
        this.hashedPassword = hashedPassword;
    }
    public String getEncryptedUserName() {
        return encryptedUserName;
    }

    public void setEncryptedUserName(String userName) {
        this.encryptedUserName = userName;
    }
    public String getHashedPassword() {
        return hashedPassword;
    }

    public void setHashedPassword(String hashedPassword) {
        this.hashedPassword = hashedPassword;
    }

    public ArrayList<Wine> getFavouritedWines() {
        return favouritedWines;
    }

    public void setFavouritedWines(ArrayList<Wine> favouritedWines) {
        this.favouritedWines = favouritedWines;
    }

    public ArrayList<Wine> getDislikedWines() {
        return dislikedWines;
    }

    public void setDislikedWines(ArrayList<Wine> dislikedWines) {
        this.dislikedWines = dislikedWines;
    }

    public ArrayList<String> getFavouritedTags() {
        return favouritedTags;
    }

    public void setFavouritedTags(ArrayList<String> favouritedTags) {
        this.favouritedTags = favouritedTags;
    }
}
