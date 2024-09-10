package seng202.team0.models;

import java.util.ArrayList;

public class User {
    private String name;
    private String encryptedUserName;
    private int hashedPassword;
    private ArrayList<Wine> favouritedWines;
    private ArrayList<Wine> dislikedWines;
    private ArrayList<String> favouritedTags;

    /**
     * Constructor for User.
     * @param name The name of the user
     * @param userName The encrypted username of the user
     */
    public User(String name, String userName) {
        this.name = name;
        this.encryptedUserName = userName;
    }

    /**
     * Constructor for User.
     * @param name The name of the user
     * @param userName The encrypted username of the user
     * @param hashedPassword The hashed password of the user
     */
    public User(String name, String userName, int hashedPassword) {
        this.name = name;
        this.encryptedUserName = userName;
        this.hashedPassword = hashedPassword;
    }

    /**
     * Returns the name of the user.
     * @return The name of the user
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the user.
     * @param name The name of the user
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the encrypted username of the user.
     * @return The encrypted username of the user
     */
    public String getEncryptedUserName() {
        return encryptedUserName;
    }

    /**
     * Sets the encrypted username of the user.
     * @param userName The encrypted username of the user
     */
    public void setEncryptedUserName(String userName) {
        this.encryptedUserName = userName;
    }

    /**
     * Returns the hashed password of the user.
     * @return The hashed password of the user
     */
    public int getHashedPassword() {
        return hashedPassword;
    }

    /**
     * Sets the hashed password of the user.
     * @param hashedPassword The hashed password of the user
     */
    public void setHashedPassword(int hashedPassword) {
        this.hashedPassword = hashedPassword;
    }

    /**
     * Gets the list of favourited wines.
     * @return The list of favourited wines
     */
    public ArrayList<Wine> getFavouritedWines() {
        return favouritedWines;
    }

    /**
     * Sets the list of favourited wines.
     * @param favouritedWines The list of favourited wines
     */
    public void setFavouritedWines(ArrayList<Wine> favouritedWines) {
        this.favouritedWines = favouritedWines;
    }

    /**
     * Gets the list of disliked wines.
     * @return The list of disliked wines
     */
    public ArrayList<Wine> getDislikedWines() {
        return dislikedWines;
    }

    /**
     * Sets the list of disliked wines.
     * @param dislikedWines The list of disliked wines
     */
    public void setDislikedWines(ArrayList<Wine> dislikedWines) {
        this.dislikedWines = dislikedWines;
    }

    /**
     * Gets the list of favourited tags.
     * @return The list of favourited tags
     */
    public ArrayList<String> getFavouritedTags() {
        return favouritedTags;
    }

    /**
     * Sets the list of favourited tags.
     * @param favouritedTags The list of favourited tags
     */
    public void setFavouritedTags(ArrayList<String> favouritedTags) {
        this.favouritedTags = favouritedTags;
    }
}
