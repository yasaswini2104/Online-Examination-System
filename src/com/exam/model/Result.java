package com.exam.model;

/**
 * Result entity for storing exam results
 * Pure POJO - no business logic
 */
public class Result {
    private String username;
    private int score;
    private int totalMarks;
    private String timestamp;
    
    public Result() {
    }
    
    public Result(String username, int score, int totalMarks, String timestamp) {
        this.username = username;
        this.score = score;
        this.totalMarks = totalMarks;
        this.timestamp = timestamp;
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public int getScore() {
        return score;
    }
    
    public void setScore(int score) {
        this.score = score;
    }
    
    public int getTotalMarks() {
        return totalMarks;
    }
    
    public void setTotalMarks(int totalMarks) {
        this.totalMarks = totalMarks;
    }
    
    public String getTimestamp() {
        return timestamp;
    }
    
    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
    
    @Override
    public String toString() {
        return "Result{username='" + username + "', score=" + score + 
               "/" + totalMarks + ", timestamp='" + timestamp + "'}";
    }
}
