package com.lms.model;

public class Assignment {
    private int id;
    private int courseId;
    private String title;
    private String description;
    private int maxMarks;
    private Submission mySubmission;   // for the logged-in student (may be null)

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getCourseId() { return courseId; }
    public void setCourseId(int courseId) { this.courseId = courseId; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public int getMaxMarks() { return maxMarks; }
    public void setMaxMarks(int maxMarks) { this.maxMarks = maxMarks; }
    public Submission getMySubmission() { return mySubmission; }
    public void setMySubmission(Submission mySubmission) { this.mySubmission = mySubmission; }
}
