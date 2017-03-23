package ru.spbau.mit.sd.command;

/**
 * Exception class, which uses by commands to throw exception.
 */
public class CommandException extends RuntimeException {
    /**
     * default constructor
     */
    public CommandException() {
        super();
    }

    /**
     * constructor with message
     * @param message - message
     */
    public CommandException(String message) {
        super(message);
    }

    /**
     * constructor with message and another exception
     * @param message - message
     * @param cause - exception
     */
    public CommandException(String message, Throwable cause) {
        super(message, cause);
    }

}
