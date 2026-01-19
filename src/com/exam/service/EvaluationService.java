package com.exam.service;

import com.exam.exception.DataAccessException;
import com.exam.model.Question;
import com.exam.model.Result;
import com.exam.util.FileUtil;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Service class for exam evaluation
 * Handles answer comparison, score calculation, and result storage
 */
public class EvaluationService {
    
    private static final String RESULTS_FILE = "results.txt";
    
    /**
     * Evaluate exam answers and calculate score
     */
    public Result evaluateExam(String username, List<Question> questions, 
                               Map<Integer, String> userAnswers) {
        
        int score = 0;
        int totalMarks = questions.size();
        
        // Compare each answer with correct option
        for (Question question : questions) {
            String userAnswer = userAnswers.get(question.getId());
            if (userAnswer != null && 
                userAnswer.trim().equalsIgnoreCase(question.getCorrectOption())) {
                score++;
            }
        }
        
        // Generate timestamp
        String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        
        // Create result object
        Result result = new Result(username, score, totalMarks, timestamp);
        
        return result;
    }
    
    /**
     * Save result to file
     */
    public void saveResult(Result result) throws DataAccessException {
        String resultRecord = result.getUsername() + "," + 
                             result.getScore() + "," + 
                             result.getTotalMarks() + "," + 
                             result.getTimestamp();
        
        FileUtil.appendToFile(RESULTS_FILE, resultRecord);
    }
    
    /**
     * Get all results
     */
    public List<Result> getAllResults() throws DataAccessException {
        List<String> lines = FileUtil.readFile(RESULTS_FILE);
        List<Result> results = new java.util.ArrayList<>();
        
        for (String line : lines) {
            String[] parts = line.split(",");
            if (parts.length == 4) {
                Result result = new Result(
                    parts[0].trim(),
                    Integer.parseInt(parts[1].trim()),
                    Integer.parseInt(parts[2].trim()),
                    parts[3].trim()
                );
                results.add(result);
            }
        }
        
        return results;
    }
    
    /**
     * Get results for a specific user
     */
    public List<Result> getResultsByUsername(String username) throws DataAccessException {
        List<Result> allResults = getAllResults();
        List<Result> userResults = new java.util.ArrayList<>();
        
        for (Result result : allResults) {
            if (result.getUsername().equals(username)) {
                userResults.add(result);
            }
        }
        
        return userResults;
    }
    
    /**
     * Get the latest result for a specific user
     */
    public Result getLatestResult(String username) throws DataAccessException {
        List<Result> userResults = getResultsByUsername(username);
        
        if (userResults.isEmpty()) {
            return null;
        }
        
        // Return the last result (most recent)
        return userResults.get(userResults.size() - 1);
    }
    
    /**
     * Calculate percentage score
     */
    public double calculatePercentage(Result result) {
        if (result.getTotalMarks() == 0) {
            return 0.0;
        }
        return (result.getScore() * 100.0) / result.getTotalMarks();
    }
    
    /**
     * Get grade based on percentage
     */
    public String getGrade(double percentage) {
        if (percentage >= 90) {
            return "A+";
        } else if (percentage >= 80) {
            return "A";
        } else if (percentage >= 70) {
            return "B";
        } else if (percentage >= 60) {
            return "C";
        } else if (percentage >= 50) {
            return "D";
        } else {
            return "F";
        }
    }
}
// ```

// ---

// ### **Files Changed:**
// - `src/com/exam/service/EvaluationService.java` (created)

// ### **Suggested Git Commit Message:**
// ```
// Add EvaluationService for exam evaluation and result management

// - Implemented evaluateExam to compare answers and calculate score
// - Added saveResult to persist exam results to file
// - Implemented getAllResults and getResultsByUsername for result retrieval
// - Added calculatePercentage and getGrade helper methods
// - Integrated timestamp generation for result tracking