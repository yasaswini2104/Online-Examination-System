package com.exam.service;

import com.exam.exception.AuthenticationException;
import com.exam.exception.DataAccessException;
import com.exam.model.User;
import com.exam.util.FileUtil;
import com.exam.util.ValidationUtil;

import java.util.List;

/**
 * Service class for authentication operations
 * Handles user registration, login, and role identification
 */
public class AuthService {
    
    private static final String USERS_FILE = "users.txt";
    private User currentUser;
    
    /**
     * Register a new user
     */
    public void register(String username, String password, String role) 
            throws AuthenticationException, DataAccessException {
        
        // Validate inputs
        if (!ValidationUtil.isValidUsername(username)) {
            throw new AuthenticationException(
                "Invalid username! Must be 3-20 alphanumeric characters.");
        }
        
        if (!ValidationUtil.isValidPassword(password)) {
            throw new AuthenticationException(
                "Invalid password! Must be at least 6 characters.");
        }
        
        if (!ValidationUtil.isValidRole(role)) {
            throw new AuthenticationException(
                "Invalid role! Must be ADMIN or STUDENT.");
        }
        
        // Check if username already exists
        if (userExists(username)) {
            throw new AuthenticationException(
                "Username already exists! Please choose a different username.");
        }
        
        // Save user to file
        String userRecord = username + "," + password + "," + role.toUpperCase();
        FileUtil.appendToFile(USERS_FILE, userRecord);
    }
    
    /**
     * Login a user
     */
    public User login(String username, String password) 
            throws AuthenticationException, DataAccessException {
        
        // Validate inputs
        if (!ValidationUtil.isNotEmpty(username) || !ValidationUtil.isNotEmpty(password)) {
            throw new AuthenticationException("Username and password cannot be empty!");
        }
        
        // Read users from file
        List<String> users = FileUtil.readFile(USERS_FILE);
        
        for (String userRecord : users) {
            String[] parts = userRecord.split(",");
            if (parts.length == 3) {
                String storedUsername = parts[0].trim();
                String storedPassword = parts[1].trim();
                String storedRole = parts[2].trim();
                
                if (storedUsername.equals(username) && storedPassword.equals(password)) {
                    currentUser = new User(username, password, storedRole);
                    return currentUser;
                }
            }
        }
        
        throw new AuthenticationException("Invalid username or password!");
    }
    
    /**
     * Get the currently logged-in user
     */
    public User getCurrentUser() {
        return currentUser;
    }
    
    /**
     * Logout the current user
     */
    public void logout() {
        currentUser = null;
    }
    
    /**
     * Check if a user is currently logged in
     */
    public boolean isLoggedIn() {
        return currentUser != null;
    }
    
    /**
     * Check if current user is an admin
     */
    public boolean isAdmin() {
        return currentUser != null && "ADMIN".equals(currentUser.getRole());
    }
    
    /**
     * Check if current user is a student
     */
    public boolean isStudent() {
        return currentUser != null && "STUDENT".equals(currentUser.getRole());
    }
    
    /**
     * Check if a username already exists
     */
    private boolean userExists(String username) throws DataAccessException {
        List<String> users = FileUtil.readFile(USERS_FILE);
        
        for (String userRecord : users) {
            String[] parts = userRecord.split(",");
            if (parts.length >= 1 && parts[0].trim().equals(username)) {
                return true;
            }
        }
        
        return false;
    }
}
```

---

### **Files Changed:**
- `src/com/exam/service/AuthService.java` (created)

### **Suggested Git Commit Message:**
```


- Implemented user registration with validation
- Implemented login functionality with credential verification
- Added role-based access checks (isAdmin, isStudent)
- Added user existence check to prevent duplicates
- Integrated with FileUtil for persistent storage