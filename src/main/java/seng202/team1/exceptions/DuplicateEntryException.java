package seng202.team1.exceptions;

/**
 * A simple exception, used when there are duplicate entries in a database.
 */
public class DuplicateEntryException extends Exception {

    /**
     * Simple constructor for DuplicateEntryException that passes to parent class.
     * @param message the error message
     */
    public DuplicateEntryException(String message) {
        super(message);
    }


}
