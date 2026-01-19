package com.exam.util;

import java.util.Scanner;

/**
 * Utility class for safe input handling
 * Wrapper around Scanner with validation
 */
public class InputUtil {
    
    private static Scanner scanner = new Scanner(System.in);
    
    /**
     * Read a string input
     */
    public static String readString(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }
    
    /**
     * Read an integer input with validation
     */
    public static int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                String input = scanner.nextLine().trim();
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Please enter a valid number.");
            }
        }
    }
    
    /**
     * Read an integer within a range
     */
    public static int readIntInRange(String prompt, int min, int max) {
        while (true) {
            int value = readInt(prompt);
            if (value >= min && value <= max) {
                return value;
            }
            System.out.println("Please enter a number between " + min + " and " + max);
        }
    }
    
    /**
     * Read a single character input
     */
    public static char readChar(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            if (input.length() == 1) {
                return input.charAt(0);
            }
            System.out.println("Invalid input! Please enter a single character.");
        }
    }
    
    /**
     * Read yes/no confirmation
     */
    public static boolean readConfirmation(String prompt) {
        while (true) {
            System.out.print(prompt + " (y/n): ");
            String input = scanner.nextLine().trim().toLowerCase();
            if (input.equals("y") || input.equals("yes")) {
                return true;
            } else if (input.equals("n") || input.equals("no")) {
                return false;
            }
            System.out.println("Please enter 'y' or 'n'");
        }
    }
    
    /**
     * Close the scanner
     */
    public static void closeScanner() {
        scanner.close();
    }
}