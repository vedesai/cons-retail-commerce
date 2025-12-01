package com.deloitte.retail.exception;

/**
 * Exception thrown when attempting to create a resource that already exists
 * 
 * @author Deloitte
 * @version 1.0.0
 */
public class DuplicateResourceException extends RuntimeException {

    public DuplicateResourceException(String message) {
        super(message);
    }

    public DuplicateResourceException(String message, Throwable cause) {
        super(message, cause);
    }
}
