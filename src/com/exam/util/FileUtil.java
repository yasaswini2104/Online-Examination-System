package com.exam.util;

import com.exam.exception.DataAccessException;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for file operations
 * Handles all file read/write operations
 */
public class FileUtil {
    
    // private static final String DATA_DIR = "data/";
    private static final String DATA_DIR =
        System.getProperty("user.dir") + File.separator + "data" + File.separator;

    
    /**
     * Read all lines from a file
     */
    public static List<String> readFile(String filename) throws DataAccessException {
        List<String> lines = new ArrayList<>();
        String filepath = DATA_DIR + filename;
        
        try (BufferedReader reader = new BufferedReader(new FileReader(filepath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    lines.add(line);
                }
            }
        } catch (FileNotFoundException e) {
            throw new DataAccessException("File not found: " + filename, e);
        } catch (IOException e) {
            throw new DataAccessException("Error reading file: " + filename, e);
        }
        
        return lines;
    }
    
    /**
     * Write lines to a file (overwrites existing content)
     */
    public static void writeFile(String filename, List<String> lines) throws DataAccessException {
        String filepath = DATA_DIR + filename;
        
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filepath))) {
            for (String line : lines) {
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            throw new DataAccessException("Error writing to file: " + filename, e);
        }
    }
    
    /**
     * Append a single line to a file
     */
    public static void appendToFile(String filename, String line) throws DataAccessException {
        String filepath = DATA_DIR + filename;
        
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filepath, true))) {
            writer.write(line);
            writer.newLine();
        } catch (IOException e) {
            throw new DataAccessException("Error appending to file: " + filename, e);
        }
    }
    
    /**
     * Check if a file exists
     */
    public static boolean fileExists(String filename) {
        File file = new File(DATA_DIR + filename);
        return file.exists();
    }
}