package com.exam.exception;

/**
 * Exception thrown when a user attempts unauthorized actions
 * Used when students try to access admin features
 */
public class AuthorizationException extends Exception {
    
    public AuthorizationException(String message) {
        super(message);
    }
    
    public AuthorizationException(String message, Throwable cause) {
        super(message, cause);
    }
}