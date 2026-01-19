package com.exam.service;

import com.exam.exception.DataAccessException;
import com.exam.model.Question;
import com.exam.model.Result;
import com.exam.util.InputUtil;
import com.exam.util.ValidationUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Service class for exam operations
 * Coordinates exam flow, timer, and evaluation
 */
public class ExamService {
    
    private QuestionService questionService;
    private EvaluationService evaluationService;
    private AuthService authService;
    private int examTimeInMinutes;
    private int numberOfQuestions;
    
    public ExamService(QuestionService questionService, 
                      EvaluationService evaluationService,
                      AuthService authService) {
        this.questionService = questionService;
        this.evaluationService = evaluationService;
        this.authService = authService;
        this.examTimeInMinutes = 10; // Default: 10 minutes
        this.numberOfQuestions = 5;  // Default: 5 questions
    }
    
    /**
     * Start the exam for the current user
     */
    public void startExam() throws DataAccessException {
        String username = authService.getCurrentUser().getUsername();
        
        System.out.println("\n" + "=".repeat(60));
        System.out.println("                    EXAM STARTED");
        System.out.println("=".repeat(60));
        System.out.println("Student: " + username);
        System.out.println("Time Limit: " + examTimeInMinutes + " minutes");
        System.out.println("Total Questions: " + numberOfQuestions);
        System.out.println("=".repeat(60));
        
        // Load questions
        List<Question> examQuestions = questionService.getQuestionsForExam(numberOfQuestions);
        
        if (examQuestions.isEmpty()) {
            System.out.println("\nNo questions available for the exam!");
            return;
        }
        
        // Adjust if fewer questions available
        if (examQuestions.size() < numberOfQuestions) {
            System.out.println("\nNote: Only " + examQuestions.size() + " questions available.");
            numberOfQuestions = examQuestions.size();
        }
        
        // Start timer
        TimerService timer = new TimerService(examTimeInMinutes);
        timer.startTimer();
        
        // Store user answers
        Map<Integer, String> userAnswers = new HashMap<>();
        
        // Display questions and capture answers
        boolean examCompleted = conductExam(examQuestions, userAnswers, timer);
        
        // Stop timer
        timer.stopTimer();
        
        if (!examCompleted && timer.isTimeUp()) {
            System.out.println("\n⚠️  Exam auto-submitted due to time limit.");
        }
        
        // Evaluate and save result
        Result result = evaluationService.evaluateExam(username, examQuestions, userAnswers);
        evaluationService.saveResult(result);
        
        // Display result
        displayResult(result);
    }
    
    /**
     * Conduct the exam - display questions and capture answers
     */
    private boolean conductExam(List<Question> questions, 
                                Map<Integer, String> userAnswers, 
                                TimerService timer) {
        
        for (int i = 0; i < questions.size(); i++) {
            // Check if time is up
            if (timer.isTimeUp()) {
                return false;
            }
            
            Question question = questions.get(i);
            
            System.out.println("\n" + "-".repeat(60));
            System.out.println("Question " + (i + 1) + " of " + questions.size());
            System.out.println("-".repeat(60));
            System.out.println(question.getQuestionText());
            System.out.println("\nA. " + question.getOptionA());
            System.out.println("B. " + question.getOptionB());
            System.out.println("C. " + question.getOptionC());
            System.out.println("D. " + question.getOptionD());
            
            // Get user answer
            String answer = getValidAnswer(timer);
            
            if (answer == null) {
                // Time expired while waiting for answer
                return false;
            }
            
            userAnswers.put(question.getId(), answer);
        }
        
        return true;
    }
    
    /**
     * Get valid answer from user with timer check
     */
    private String getValidAnswer(TimerService timer) {
        while (true) {
            // Check time before accepting input
            if (timer.isTimeUp()) {
                return null;
            }
            
            System.out.print("\nYour answer (A/B/C/D) or 'S' to skip: ");
            String input = InputUtil.readString("").toUpperCase();
            
            // Check time again after input
            if (timer.isTimeUp()) {
                return null;
            }
            
            if (input.equals("S")) {
                return "";  // Skipped question
            }
            
            if (ValidationUtil.isValidOption(input)) {
                return input;
            }
            
            System.out.println("Invalid input! Please enter A, B, C, D, or S to skip.");
        }
    }
    
    /**
     * Display exam result
     */
    private void displayResult(Result result) {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("                    EXAM COMPLETED");
        System.out.println("=".repeat(60));
        System.out.println("Student: " + result.getUsername());
        System.out.println("Score: " + result.getScore() + "/" + result.getTotalMarks());
        
        double percentage = evaluationService.calculatePercentage(result);
        String grade = evaluationService.getGrade(percentage);
        
        System.out.println("Percentage: " + String.format("%.2f", percentage) + "%");
        System.out.println("Grade: " + grade);
        System.out.println("Date: " + result.getTimestamp());
        System.out.println("=".repeat(60));
        
        if (percentage >= 50) {
            System.out.println("\n🎉 Congratulations! You passed the exam.");
        } else {
            System.out.println("\n📚 Keep studying! Better luck next time.");
        }
    }
    
    /**
     * Set exam time limit (Admin only)
     */
    public void setExamTime(int minutes) {
        if (minutes > 0) {
            this.examTimeInMinutes = minutes;
            System.out.println("Exam time set to " + minutes + " minutes.");
        } else {
            System.out.println("Invalid time! Must be greater than 0.");
        }
    }
    
    /**
     * Set number of questions (Admin only)
     */
    public void setNumberOfQuestions(int count) throws DataAccessException {
        int totalAvailable = questionService.getTotalQuestions();
        
        if (count <= 0) {
            System.out.println("Invalid count! Must be greater than 0.");
        } else if (count > totalAvailable) {
            System.out.println("Only " + totalAvailable + " questions available.");
            this.numberOfQuestions = totalAvailable;
        } else {
            this.numberOfQuestions = count;
            System.out.println("Number of questions set to " + count + ".");
        }
    }
    
    /**
     * Get current exam configuration
     */
    public void displayExamConfig() throws DataAccessException {
        System.out.println("\n=== Current Exam Configuration ===");
        System.out.println("Time Limit: " + examTimeInMinutes + " minutes");
        System.out.println("Number of Questions: " + numberOfQuestions);
        System.out.println("Total Questions Available: " + questionService.getTotalQuestions());
        System.out.println("==================================");
    }
    
    public int getExamTimeInMinutes() {
        return examTimeInMinutes;
    }
    
    public int getNumberOfQuestions() {
        return numberOfQuestions;
    }
}
// ```

// ---

// ### **Files Changed:**
// - `src/com/exam/service/ExamService.java` (created)

// ### **Suggested Git Commit Message:**
// ```
// Add ExamService to coordinate exam flow and orchestrate services

// - Implemented startExam with timer integration
// - Added conductExam to display questions and capture answers
// - Implemented auto-submit when timer expires
// - Added exam configuration methods (time limit, question count)
// - Integrated with TimerService, QuestionService, and EvaluationService
// - Added result display with grade calculation