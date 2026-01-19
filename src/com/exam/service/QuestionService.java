package com.exam.service;

import com.exam.exception.AuthorizationException;
import com.exam.exception.DataAccessException;
import com.exam.model.Question;
import com.exam.util.FileUtil;
import com.exam.util.ValidationUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Service class for question management
 * Handles loading, adding, updating, and deleting questions
 */
public class QuestionService {
    
    private static final String QUESTIONS_FILE = "questions.txt";
    private List<Question> questionBank;
    private AuthService authService;
    
    public QuestionService(AuthService authService) {
        this.authService = authService;
        this.questionBank = new ArrayList<>();
    }
    
    /**
     * Load all questions from file
     */
    public void loadQuestions() throws DataAccessException {
        questionBank.clear();
        List<String> lines = FileUtil.readFile(QUESTIONS_FILE);
        
        int id = 1;
        for (String line : lines) {
            String[] parts = line.split("\\|");
            if (parts.length == 6) {
                Question question = new Question(
                    id++,
                    parts[0].trim(),
                    parts[1].trim(),
                    parts[2].trim(),
                    parts[3].trim(),
                    parts[4].trim(),
                    parts[5].trim().toUpperCase()
                );
                questionBank.add(question);
            }
        }
    }
    
    /**
     * Get all questions
     */
    public List<Question> getAllQuestions() throws DataAccessException {
        if (questionBank.isEmpty()) {
            loadQuestions();
        }
        return new ArrayList<>(questionBank);
    }
    
    /**
     * Get a specific number of questions for exam
     */
    public List<Question> getQuestionsForExam(int count) throws DataAccessException {
        if (questionBank.isEmpty()) {
            loadQuestions();
        }
        
        int actualCount = Math.min(count, questionBank.size());
        return new ArrayList<>(questionBank.subList(0, actualCount));
    }
    
    /**
     * Add a new question (Admin only)
     */
    public void addQuestion(String questionText, String optionA, String optionB, 
                           String optionC, String optionD, String correctOption) 
            throws AuthorizationException, DataAccessException {
        
        // Check authorization
        if (!authService.isAdmin()) {
            throw new AuthorizationException("Only admins can add questions!");
        }
        
        // Validate inputs
        if (!ValidationUtil.isNotEmpty(questionText) || 
            !ValidationUtil.isNotEmpty(optionA) ||
            !ValidationUtil.isNotEmpty(optionB) ||
            !ValidationUtil.isNotEmpty(optionC) ||
            !ValidationUtil.isNotEmpty(optionD)) {
            throw new IllegalArgumentException("All fields must be filled!");
        }
        
        if (!ValidationUtil.isValidOption(correctOption)) {
            throw new IllegalArgumentException("Correct option must be A, B, C, or D!");
        }
        
        // Create question record
        String questionRecord = questionText + "|" + optionA + "|" + optionB + "|" + 
                               optionC + "|" + optionD + "|" + correctOption.toUpperCase();
        
        // Append to file
        FileUtil.appendToFile(QUESTIONS_FILE, questionRecord);
        
        // Reload questions
        loadQuestions();
    }
    
    /**
     * Update a question (Admin only)
     */
    public void updateQuestion(int questionId, String questionText, String optionA, 
                              String optionB, String optionC, String optionD, 
                              String correctOption) 
            throws AuthorizationException, DataAccessException {
        
        // Check authorization
        if (!authService.isAdmin()) {
            throw new AuthorizationException("Only admins can update questions!");
        }
        
        // Validate inputs
        if (!ValidationUtil.isNotEmpty(questionText) || 
            !ValidationUtil.isNotEmpty(optionA) ||
            !ValidationUtil.isNotEmpty(optionB) ||
            !ValidationUtil.isNotEmpty(optionC) ||
            !ValidationUtil.isNotEmpty(optionD)) {
            throw new IllegalArgumentException("All fields must be filled!");
        }
        
        if (!ValidationUtil.isValidOption(correctOption)) {
            throw new IllegalArgumentException("Correct option must be A, B, C, or D!");
        }
        
        if (questionBank.isEmpty()) {
            loadQuestions();
        }
        
        // Find and update question
        boolean found = false;
        for (int i = 0; i < questionBank.size(); i++) {
            if (questionBank.get(i).getId() == questionId) {
                Question updated = new Question(
                    questionId,
                    questionText,
                    optionA,
                    optionB,
                    optionC,
                    optionD,
                    correctOption.toUpperCase()
                );
                questionBank.set(i, updated);
                found = true;
                break;
            }
        }
        
        if (!found) {
            throw new IllegalArgumentException("Question not found with ID: " + questionId);
        }
        
        // Save all questions back to file
        saveAllQuestions();
    }
    
    /**
     * Delete a question (Admin only)
     */
    public void deleteQuestion(int questionId) 
            throws AuthorizationException, DataAccessException {
        
        // Check authorization
        if (!authService.isAdmin()) {
            throw new AuthorizationException("Only admins can delete questions!");
        }
        
        if (questionBank.isEmpty()) {
            loadQuestions();
        }
        
        // Remove question
        boolean removed = questionBank.removeIf(q -> q.getId() == questionId);
        
        if (!removed) {
            throw new IllegalArgumentException("Question not found with ID: " + questionId);
        }
        
        // Re-assign IDs
        for (int i = 0; i < questionBank.size(); i++) {
            questionBank.get(i).setId(i + 1);
        }
        
        // Save all questions back to file
        saveAllQuestions();
    }
    
    /**
     * Get total number of questions
     */
    public int getTotalQuestions() throws DataAccessException {
        if (questionBank.isEmpty()) {
            loadQuestions();
        }
        return questionBank.size();
    }
    
    /**
     * Save all questions to file
     */
    private void saveAllQuestions() throws DataAccessException {
        List<String> lines = new ArrayList<>();
        
        for (Question q : questionBank) {
            String line = q.getQuestionText() + "|" + 
                         q.getOptionA() + "|" + 
                         q.getOptionB() + "|" + 
                         q.getOptionC() + "|" + 
                         q.getOptionD() + "|" + 
                         q.getCorrectOption();
            lines.add(line);
        }
        
        FileUtil.writeFile(QUESTIONS_FILE, lines);
    }
}
