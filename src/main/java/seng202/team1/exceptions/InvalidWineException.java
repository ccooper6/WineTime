package seng202.team1.exceptions;

public class InvalidWineException extends Exception {
    /**
     * Simple constructor that passes to parent Exception class.
     * @param message error message
     */
    public InvalidWineException(String message) {
        super(message);
    }
}
