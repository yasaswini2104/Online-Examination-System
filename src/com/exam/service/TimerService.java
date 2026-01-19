package com.exam.service;

/**
 * Service class for exam timer
 * Implements multithreading to enforce time limits
 */
public class TimerService implements Runnable {
    
    private int timeInSeconds;
    private boolean timeUp;
    private boolean stopped;
    private Thread timerThread;
    
    public TimerService(int timeInMinutes) {
        this.timeInSeconds = timeInMinutes * 60;
        this.timeUp = false;
        this.stopped = false;
    }
    
    /**
     * Start the timer in a separate thread
     */
    public void startTimer() {
        timerThread = new Thread(this);
        timerThread.start();
    }
    
    /**
     * Run method for thread execution
     */
    @Override
    public void run() {
        try {
            while (timeInSeconds > 0 && !stopped) {
                Thread.sleep(1000); // Sleep for 1 second
                timeInSeconds--;
                
                // Display countdown every 30 seconds or last 10 seconds
                if (timeInSeconds % 30 == 0 || timeInSeconds <= 10) {
                    displayTimeRemaining();
                }
            }
            
            if (!stopped) {
                timeUp = true;
                System.out.println("\n\n⏰ TIME'S UP! Exam will be auto-submitted.\n");
            }
            
        } catch (InterruptedException e) {
            System.out.println("\nTimer interrupted.");
        }
    }
    
    /**
     * Stop the timer
     */
    public void stopTimer() {
        stopped = true;
        if (timerThread != null) {
            timerThread.interrupt();
        }
    }
    
    /**
     * Check if time is up
     */
    public boolean isTimeUp() {
        return timeUp;
    }
    
    /**
     * Get remaining time in seconds
     */
    public int getRemainingTime() {
        return timeInSeconds;
    }
    
    /**
     * Display time remaining
     */
    private void displayTimeRemaining() {
        int minutes = timeInSeconds / 60;
        int seconds = timeInSeconds % 60;
        System.out.println("\n⏱️  Time Remaining: " + minutes + " min " + seconds + " sec");
    }
    
    /**
     * Format time for display
     */
    public String getFormattedTime() {
        int minutes = timeInSeconds / 60;
        int seconds = timeInSeconds % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }
}
// ```

// ---

// ### **Files Changed:**
// - `src/com/exam/service/TimerService.java` (created)

// ### **Suggested Git Commit Message:**
// ```
// Add TimerService for exam time enforcement with multithreading

// - Implemented Runnable interface for separate thread execution
// - Added countdown timer with periodic time display
// - Implemented auto-submit trigger when time expires
// - Added stopTimer method for manual timer termination
// - Included formatted time display helpers