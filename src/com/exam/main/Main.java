package com.exam.main;

import com.exam.exception.AuthenticationException;
import com.exam.exception.AuthorizationException;
import com.exam.exception.DataAccessException;
import com.exam.model.Question;
import com.exam.model.Result;
import com.exam.model.User;
import com.exam.service.*;
import com.exam.util.InputUtil;

import java.util.List;

/**
 * Main entry point for Online Examination System
 * Handles menu navigation and user flow
 */
public class Main {
    
    private static AuthService authService;
    private static QuestionService questionService;
    private static EvaluationService evaluationService;
    private static ExamService examService;
    
    public static void main(String[] args) {
        // Initialize services
        authService = new AuthService();
        questionService = new QuestionService(authService);
        evaluationService = new EvaluationService();
        examService = new ExamService(questionService, evaluationService, authService);
        
        System.out.println("\n" + "=".repeat(60));
        System.out.println("        WELCOME TO ONLINE EXAMINATION SYSTEM");
        System.out.println("=".repeat(60));
        
        // Main application loop
        boolean running = true;
        while (running) {
            try {
                if (!authService.isLoggedIn()) {
                    running = showMainMenu();
                } else {
                    if (authService.isAdmin()) {
                        showAdminMenu();
                    } else {
                        showStudentMenu();
                    }
                }
            } catch (Exception e) {
                System.out.println("\n❌ Error: " + e.getMessage());
            }
        }
        
        System.out.println("\n👋 Thank you for using Online Examination System!");
        InputUtil.closeScanner();
    }
    
    /**
     * Display main menu (before login)
     */
    private static boolean showMainMenu() {
        System.out.println("\n=== MAIN MENU ===");
        System.out.println("1. Login");
        System.out.println("2. Register");
        System.out.println("3. Exit");
        
        int choice = InputUtil.readIntInRange("Enter your choice: ", 1, 3);
        
        switch (choice) {
            case 1:
                handleLogin();
                break;
            case 2:
                handleRegistration();
                break;
            case 3:
                return false;
        }
        
        return true;
    }
    
    /**
     * Handle user login
     */
    private static void handleLogin() {
        System.out.println("\n=== LOGIN ===");
        String username = InputUtil.readString("Username: ");
        String password = InputUtil.readString("Password: ");
        
        try {
            User user = authService.login(username, password);
            System.out.println("\n✅ Login successful! Welcome, " + user.getUsername());
        } catch (AuthenticationException | DataAccessException e) {
            System.out.println("\n❌ " + e.getMessage());
        }
    }
    
    /**
     * Handle user registration
     */
    private static void handleRegistration() {
        System.out.println("\n=== REGISTRATION ===");
        String username = InputUtil.readString("Username (3-20 alphanumeric): ");
        String password = InputUtil.readString("Password (min 6 characters): ");
        
        System.out.println("\nSelect Role:");
        System.out.println("1. Student");
        System.out.println("2. Admin");
        int roleChoice = InputUtil.readIntInRange("Enter choice: ", 1, 2);
        String role = (roleChoice == 1) ? "STUDENT" : "ADMIN";
        
        try {
            authService.register(username, password, role);
            System.out.println("\n✅ Registration successful! You can now login.");
        } catch (AuthenticationException | DataAccessException e) {
            System.out.println("\n❌ " + e.getMessage());
        }
    }
    
    /**
     * Display admin menu
     */
    private static void showAdminMenu() {
        System.out.println("\n=== ADMIN MENU ===");
        System.out.println("1. Manage Questions");
        System.out.println("2. Configure Exam");
        System.out.println("3. View All Results");
        System.out.println("4. Logout");
        
        int choice = InputUtil.readIntInRange("Enter your choice: ", 1, 4);
        
        switch (choice) {
            case 1:
                showQuestionManagementMenu();
                break;
            case 2:
                showExamConfigMenu();
                break;
            case 3:
                viewAllResults();
                break;
            case 4:
                authService.logout();
                System.out.println("\n✅ Logged out successfully!");
                break;
        }
    }
    
    /**
     * Display question management menu
     */
    private static void showQuestionManagementMenu() {
        System.out.println("\n=== QUESTION MANAGEMENT ===");
        System.out.println("1. View All Questions");
        System.out.println("2. Add Question");
        System.out.println("3. Update Question");
        System.out.println("4. Delete Question");
        System.out.println("5. Back");
        
        int choice = InputUtil.readIntInRange("Enter your choice: ", 1, 5);
        
        try {
            switch (choice) {
                case 1:
                    viewAllQuestions();
                    break;
                case 2:
                    addQuestion();
                    break;
                case 3:
                    updateQuestion();
                    break;
                case 4:
                    deleteQuestion();
                    break;
                case 5:
                    return;
            }
        } catch (Exception e) {
            System.out.println("\n❌ " + e.getMessage());
        }
    }
    
    /**
     * View all questions
     */
    private static void viewAllQuestions() throws DataAccessException {
        List<Question> questions = questionService.getAllQuestions();
        
        if (questions.isEmpty()) {
            System.out.println("\nNo questions available.");
            return;
        }
        
        System.out.println("\n=== ALL QUESTIONS ===");
        for (Question q : questions) {
            System.out.println("\nID: " + q.getId());
            System.out.println("Question: " + q.getQuestionText());
            System.out.println("A. " + q.getOptionA());
            System.out.println("B. " + q.getOptionB());
            System.out.println("C. " + q.getOptionC());
            System.out.println("D. " + q.getOptionD());
            System.out.println("Correct Answer: " + q.getCorrectOption());
            System.out.println("-".repeat(50));
        }
    }
    
    /**
     * Add a new question
     */
    private static void addQuestion() throws AuthorizationException, DataAccessException {
        System.out.println("\n=== ADD QUESTION ===");
        String questionText = InputUtil.readString("Question: ");
        String optionA = InputUtil.readString("Option A: ");
        String optionB = InputUtil.readString("Option B: ");
        String optionC = InputUtil.readString("Option C: ");
        String optionD = InputUtil.readString("Option D: ");
        String correctOption = InputUtil.readString("Correct Option (A/B/C/D): ").toUpperCase();
        
        questionService.addQuestion(questionText, optionA, optionB, optionC, optionD, correctOption);
        System.out.println("\n✅ Question added successfully!");
    }
    
    /**
     * Update a question
     */
    private static void updateQuestion() throws AuthorizationException, DataAccessException {
        viewAllQuestions();
        
        int id = InputUtil.readInt("\nEnter Question ID to update: ");
        
        System.out.println("\n=== UPDATE QUESTION ===");
        String questionText = InputUtil.readString("New Question: ");
        String optionA = InputUtil.readString("New Option A: ");
        String optionB = InputUtil.readString("New Option B: ");
        String optionC = InputUtil.readString("New Option C: ");
        String optionD = InputUtil.readString("New Option D: ");
        String correctOption = InputUtil.readString("New Correct Option (A/B/C/D): ").toUpperCase();
        
        questionService.updateQuestion(id, questionText, optionA, optionB, optionC, optionD, correctOption);
        System.out.println("\n✅ Question updated successfully!");
    }
    
    /**
     * Delete a question
     */
    private static void deleteQuestion() throws AuthorizationException, DataAccessException {
        viewAllQuestions();
        
        int id = InputUtil.readInt("\nEnter Question ID to delete: ");
        
        boolean confirm = InputUtil.readConfirmation("Are you sure you want to delete this question?");
        
        if (confirm) {
            questionService.deleteQuestion(id);
            System.out.println("\n✅ Question deleted successfully!");
        } else {
            System.out.println("\n❌ Deletion cancelled.");
        }
    }
    
    /**
     * Show exam configuration menu
     */
    private static void showExamConfigMenu() {
        try {
            System.out.println("\n=== EXAM CONFIGURATION ===");
            examService.displayExamConfig();
            
            System.out.println("\n1. Set Time Limit");
            System.out.println("2. Set Number of Questions");
            System.out.println("3. Back");
            
            int choice = InputUtil.readIntInRange("Enter your choice: ", 1, 3);
            
            switch (choice) {
                case 1:
                    int time = InputUtil.readInt("Enter time limit in minutes: ");
                    examService.setExamTime(time);
                    break;
                case 2:
                    int count = InputUtil.readInt("Enter number of questions: ");
                    examService.setNumberOfQuestions(count);
                    break;
                case 3:
                    return;
            }
        } catch (Exception e) {
            System.out.println("\n❌ " + e.getMessage());
        }
    }
    
    /**
     * View all results
     */
    private static void viewAllResults() {
        try {
            List<Result> results = evaluationService.getAllResults();
            
            if (results.isEmpty()) {
                System.out.println("\nNo results available.");
                return;
            }
            
            System.out.println("\n=== ALL EXAM RESULTS ===");
            System.out.println(String.format("%-15s %-10s %-15s %-20s", 
                "Username", "Score", "Percentage", "Date"));
            System.out.println("-".repeat(65));
            
            for (Result r : results) {
                double percentage = evaluationService.calculatePercentage(r);
                System.out.println(String.format("%-15s %-10s %-15s %-20s",
                    r.getUsername(),
                    r.getScore() + "/" + r.getTotalMarks(),
                    String.format("%.2f%%", percentage),
                    r.getTimestamp()));
            }
        } catch (DataAccessException e) {
            System.out.println("\n❌ " + e.getMessage());
        }
    }
    
    /**
     * Display student menu
     */
    private static void showStudentMenu() {
        System.out.println("\n=== STUDENT MENU ===");
        System.out.println("1. Start Exam");
        System.out.println("2. View My Results");
        System.out.println("3. Logout");
        
        int choice = InputUtil.readIntInRange("Enter your choice: ", 1, 3);
        
        try {
            switch (choice) {
                case 1:
                    startExam();
                    break;
                case 2:
                    viewMyResults();
                    break;
                case 3:
                    authService.logout();
                    System.out.println("\n✅ Logged out successfully!");
                    break;
            }
        } catch (Exception e) {
            System.out.println("\n❌ " + e.getMessage());
        }
    }
    
    /**
     * Start exam for student
     */
    private static void startExam() throws DataAccessException {
        examService.displayExamConfig();
        
        boolean ready = InputUtil.readConfirmation("\nAre you ready to start the exam?");
        
        if (ready) {
            examService.startExam();
        } else {
            System.out.println("\n❌ Exam cancelled.");
        }
    }
    
    /**
     * View student's own results
     */
    private static void viewMyResults() {
        try {
            String username = authService.getCurrentUser().getUsername();
            List<Result> results = evaluationService.getResultsByUsername(username);
            
            if (results.isEmpty()) {
                System.out.println("\nYou haven't taken any exams yet.");
                return;
            }
            
            System.out.println("\n=== MY EXAM RESULTS ===");
            System.out.println(String.format("%-10s %-15s %-20s", 
                "Score", "Percentage", "Date"));
            System.out.println("-".repeat(50));
            
            for (Result r : results) {
                double percentage = evaluationService.calculatePercentage(r);
                String grade = evaluationService.getGrade(percentage);
                System.out.println(String.format("%-10s %-15s %-20s",
                    r.getScore() + "/" + r.getTotalMarks(),
                    String.format("%.2f%% (%s)", percentage, grade),
                    r.getTimestamp()));
            }
        } catch (DataAccessException e) {
            System.out.println("\n❌ " + e.getMessage());
        }
    }
}
// ```

// ---

// ### **Files Changed:**
// - `src/com/exam/main/Main.java` (created)

// ### **Suggested Git Commit Message:**
// ```
// Add Main class with complete menu system and application flow

// - Implemented main menu for login/registration/exit
// - Added admin menu with question management and exam configuration
// - Added student menu with exam start and result viewing
// - Integrated all service classes for complete functionality
// - Added proper error handling and user feedback
// - Menu-driven navigation with role-based access control