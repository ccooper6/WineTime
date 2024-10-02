package seng202.team1.models;

/**
 * The User class that contains the attributes of a user.
 * @author Caleb Cooper, Wen Sheng Thong, Isaac Macdonald
 */
public class User {
    private int id;
    private String name;
    private String encryptedUserName;
    private int hashedPassword;

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
     * Sets the current user of the application to the user provided
     * @param user the current user
     */
    public static void setCurrentUser(User user)
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
     * Returns the hashed password of the user.
     * @return The hashed password of the user
     */
    public int getHashedPassword() {
        return hashedPassword;
    }

}
