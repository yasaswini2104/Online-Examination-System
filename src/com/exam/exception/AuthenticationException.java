package com.exam.exception;

/**
 * Exception thrown when authentication fails
 * Used for invalid login credentials or authentication errors
 */
public class AuthenticationException extends Exception {
    
    public AuthenticationException(String message) {
        super(message);
    }
    
    public AuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }
}
