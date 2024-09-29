package seng202.team1.exceptions;

/**
 * A simple exception, used when there are  instances of a database that already exist.
 */
public class InstanceAlreadyExistsException extends Exception {

    /**
     * Simple constructor that passes to parent Exception class
     * @param message error message
     */
    public InstanceAlreadyExistsException(String message) {
        super(message);
    }

}
