package seng202.team1.models;

/**
 * The User class that contains the attributes of a user.
 */
public class User {
    private int id;
    private String name;
    private int hashedUsername;
    private int hashedPassword;

    private static User currentUser;

    /**
     * Constructor for User.
     * @param name The name of the user
     * @param userName The hashed username of the user
     * @param id The id of the user
     */
    public User(int id, String name, int userName) {
        this.id = id;
        this.name = name;
        this.hashedUsername = userName;
    }

    /**
     * Sets the current user of the application to the user provided
     * @param user the current user
     */
    public static void setCurrentUser(User user)
    {
        currentUser = user;
    }

    /**
     * Returns the current user stored
     * @return the current user
     */
    public static User getCurrentUser()
    {
        return currentUser;
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
     * @param hashedUsername The hashed username of the user
     * @param hashedPassword The hashed password of the user
     */
    public User(String name, int hashedUsername, int hashedPassword) {
        this.name = name;
        this.hashedUsername = hashedUsername;
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
     * Returns the hashed username of the user.
     * @return The hashed username of the user
     */
    public int getHashedUsername() {
        return hashedUsername;
    }

    /**
     * Returns the hashed password of the user.
     * @return The hashed password of the user
     */
    public int getHashedPassword() {
        return hashedPassword;
    }
}
