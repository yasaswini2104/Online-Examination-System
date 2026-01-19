package com.exam.util;

/**
 * Utility class for input validation
 * Validates usernames, passwords, menu choices, etc.
 */
public class ValidationUtil {
    
    /**
     * Validate username format
     * Username must be 3-20 characters, alphanumeric only
     */
    public static boolean isValidUsername(String username) {
        if (username == null || username.isEmpty()) {
            return false;
        }
        return username.matches("^[a-zA-Z0-9]{3,20}$");
    }
    
    /**
     * Validate password format
     * Password must be at least 6 characters
     */
    public static boolean isValidPassword(String password) {
        if (password == null || password.isEmpty()) {
            return false;
        }
        return password.length() >= 6;
    }
    
    /**
     * Validate menu choice
     */
    public static boolean isValidMenuChoice(int choice, int min, int max) {
        return choice >= min && choice <= max;
    }
    
    /**
     * Validate MCQ option (A, B, C, or D)
     */
    public static boolean isValidOption(String option) {
        if (option == null || option.isEmpty()) {
            return false;
        }
        String upperOption = option.trim().toUpperCase();
        return upperOption.equals("A") || upperOption.equals("B") || 
               upperOption.equals("C") || upperOption.equals("D");
    }
    
    /**
     * Validate role (ADMIN or STUDENT)
     */
    public static boolean isValidRole(String role) {
        if (role == null || role.isEmpty()) {
            return false;
        }
        String upperRole = role.trim().toUpperCase();
        return upperRole.equals("ADMIN") || upperRole.equals("STUDENT");
    }
    
    /**
     * Validate that a string is not empty
     */
    public static boolean isNotEmpty(String str) {
        return str != null && !str.trim().isEmpty();
    }
}
