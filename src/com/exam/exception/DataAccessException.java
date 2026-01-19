package com.exam.exception;

/**
 * Exception thrown when file read/write operations fail
 * Used for any data persistence errors
 */
public class DataAccessException extends Exception {
    
    public DataAccessException(String message) {
        super(message);
    }
    
    public DataAccessException(String message, Throwable cause) {
        super(message, cause);
    }
}
