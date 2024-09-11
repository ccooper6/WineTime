package seng202.team1.exceptions;

public class InstanceAlreadyExistsException extends Exception {

    /**
     * Simple constructor that passes to parent Exception class
     * @param message error message
     */
    public InstanceAlreadyExistsException(String message) {
        super(message);
    }

}
