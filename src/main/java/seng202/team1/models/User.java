package seng202.team1.models;

import java.util.ArrayList;

/**
 * The User class that contains the attributes of a user.
 * @author Caleb Cooper, Wen Sheng Thong, Isaac Macdonald
 */
public class User {
    private int id;
    private String name;
    private String encryptedUserName;
    private int hashedPassword;
    private ArrayList<Wine> favouritedWines;
    private ArrayList<Wine> dislikedWines;
    private ArrayList<String> favouritedTags;

    private static User currenUser;

    /**
     * Constructor for User.
     * @param name The name of the user
     * @param userName The encrypted username of the user
     * @param id The id of the user
     */
    public User(int id, String name, String userName) {
        this.id = id;
        this.name = name;
        this.encryptedUserName = userName;
    }

    /**
     * Sets the current user of the application to the user provided
     * @param user the current user
     */
    public static void setCurrenUser(User user)
    {
        currenUser = user;
    }

    /**
     * Returns the current user stored
     * @return the current user
     */
    public static User getCurrentUser()
    {
        return currenUser;
    }

    /**
     * The getter method for users id
     * @return the users id
     */
    public int getId()
    {
        return id;
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
