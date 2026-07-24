package com.anode.tool.commands.zip;

/**
 * Exception thrown when an unzip operation fails.
 */
public class UnzipException extends RuntimeException {

    /**
     * Constructs a new UnzipException with the specified message and cause.
     * @param message the error message
     * @param cause the underlying cause
     */
    public UnzipException(String message, Throwable cause) {
        super(message, cause);
    }

}
