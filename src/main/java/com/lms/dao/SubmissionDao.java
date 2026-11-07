package com.lms.dao;

import com.lms.exception.LmsException;
import com.lms.model.Submission;
import com.lms.util.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SubmissionDao {
    private static final String BASE =
        "SELECT s.*, u.name AS student_name, a.title AS assignment_title FROM submissions s "
      + "JOIN users u ON u.id = s.student_id JOIN assignments a ON a.id = s.assignment_id ";

    private Submission map(ResultSet rs) throws SQLException {
        Submission s = new Submission();
        s.setId(rs.getInt("id"));
        s.setAssignmentId(rs.getInt("assignment_id"));
        s.setStudentId(rs.getInt("student_id"));
        s.setStudentName(rs.getString("student_name"));
        s.setAssignmentTitle(rs.getString("assignment_title"));
        s.setAnswer(rs.getString("answer"));
        int m = rs.getInt("marks");
        s.setMarks(rs.wasNull() ? null : m);
        return s;
    }

    /** Insert, or update the answer if the student re-submits (clears old marks). */
    public void submit(int assignmentId, int studentId, String answer) {
        String sql = "INSERT INTO submissions(assignment_id, student_id, answer) VALUES (?,?,?) "
                   + "ON DUPLICATE KEY UPDATE answer = VALUES(answer), marks = NULL";
        try (Connection c = DBConnection.get(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, assignmentId);
            ps.setInt(2, studentId);
            ps.setString(3, answer);
            ps.executeUpdate();
        } catch (SQLException e) { throw new LmsException("Could not submit assignment", e); }
    }

    public void grade(int submissionId, int marks) {
        try (Connection c = DBConnection.get(); PreparedStatement ps = c.prepareStatement("UPDATE submissions SET marks = ? WHERE id = ?")) {
            ps.setInt(1, marks);
            ps.setInt(2, submissionId);
            ps.executeUpdate();
        } catch (SQLException e) { throw new LmsException("Could not save grade", e); }
    }

    public Submission find(int assignmentId, int studentId) {
        try (Connection c = DBConnection.get(); PreparedStatement ps = c.prepareStatement(BASE + "WHERE s.assignment_id = ? AND s.student_id = ?")) {
            ps.setInt(1, assignmentId);
            ps.setInt(2, studentId);
            try (ResultSet rs = ps.executeQuery()) { return rs.next() ? map(rs) : null; }
        } catch (SQLException e) { throw new LmsException("Database error", e); }
    }

    public List<Submission> findByAssignment(int assignmentId) {
        List<Submission> list = new ArrayList<>();
        try (Connection c = DBConnection.get(); PreparedStatement ps = c.prepareStatement(BASE + "WHERE s.assignment_id = ? ORDER BY u.name")) {
            ps.setInt(1, assignmentId);
            try (ResultSet rs = ps.executeQuery()) { while (rs.next()) list.add(map(rs)); }
        } catch (SQLException e) { throw new LmsException("Database error", e); }
        return list;
    }

    public List<Submission> findByStudent(int studentId) {
        List<Submission> list = new ArrayList<>();
        try (Connection c = DBConnection.get(); PreparedStatement ps = c.prepareStatement(BASE + "WHERE s.student_id = ? ORDER BY s.id DESC")) {
            ps.setInt(1, studentId);
            try (ResultSet rs = ps.executeQuery()) { while (rs.next()) list.add(map(rs)); }
        } catch (SQLException e) { throw new LmsException("Database error", e); }
        return list;
    }
}
