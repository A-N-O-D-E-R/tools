package com.anode.tool.print;

/**
 * Exception thrown when a print service operation fails.
 */
public class PrintServicesException extends Exception {

    /**
     * Serial version UID for serialization.
     */
    private static final long serialVersionUID = 7158068091315510185L;

    /**
     * Constructs a new PrintServicesException with no message or cause.
     */
    public PrintServicesException() {
        super();
    }

    /**
     * Constructs a new PrintServicesException with the specified message.
     * @param message the error message
     */
    public PrintServicesException(String message) {
        super(message);
    }

    /**
     * Constructs a new PrintServicesException with the specified cause.
     * @param cause the underlying cause
     */
    public PrintServicesException(Throwable cause) {
        super(cause);
    }

    /**
     * Constructs a new PrintServicesException with the specified message and cause.
     * @param message the error message
     * @param cause the underlying cause
     */
    public PrintServicesException(String message, Throwable cause) {
        super(message, cause);
    }

}
