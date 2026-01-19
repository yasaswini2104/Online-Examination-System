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

### **Suggested Git Commit Message:**
```
Add custom exception classes for error handling

- Created AuthenticationException for login failures
- Created AuthorizationException for unauthorized access
- Created DataAccessException for file I/O errors