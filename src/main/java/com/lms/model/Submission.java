package com.lms.model;

public class Submission {
    private int id;
    private int assignmentId;
    private int studentId;
    private String studentName;
    private String assignmentTitle;
    private String answer;
    private Integer marks;   // null = not graded yet

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getAssignmentId() { return assignmentId; }
    public void setAssignmentId(int assignmentId) { this.assignmentId = assignmentId; }
    public int getStudentId() { return studentId; }
    public void setStudentId(int studentId) { this.studentId = studentId; }
    public String getStudentName() { return studentName; }
    public void setStudentName(String studentName) { this.studentName = studentName; }
    public String getAssignmentTitle() { return assignmentTitle; }
    public void setAssignmentTitle(String assignmentTitle) { this.assignmentTitle = assignmentTitle; }
    public String getAnswer() { return answer; }
    public void setAnswer(String answer) { this.answer = answer; }
    public Integer getMarks() { return marks; }
    public void setMarks(Integer marks) { this.marks = marks; }
    public boolean isGraded() { return marks != null; }
}
