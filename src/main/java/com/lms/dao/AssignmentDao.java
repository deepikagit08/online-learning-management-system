package com.lms.dao;

import com.lms.exception.LmsException;
import com.lms.model.Assignment;
import com.lms.util.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AssignmentDao {
    private final SubmissionDao submissionDao = new SubmissionDao();

    private Assignment map(ResultSet rs) throws SQLException {
        Assignment a = new Assignment();
        a.setId(rs.getInt("id"));
        a.setCourseId(rs.getInt("course_id"));
        a.setTitle(rs.getString("title"));
        a.setDescription(rs.getString("description"));
        a.setMaxMarks(rs.getInt("max_marks"));
        return a;
    }

    public void create(int courseId, String title, String description, int maxMarks) {
        String sql = "INSERT INTO assignments(course_id,title,description,max_marks) VALUES (?,?,?,?)";
        try (Connection c = DBConnection.get(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, courseId);
            ps.setString(2, title);
            ps.setString(3, description);
            ps.setInt(4, maxMarks);
            ps.executeUpdate();
        } catch (SQLException e) { throw new LmsException("Could not create assignment", e); }
    }

    public Assignment findById(int id) {
        try (Connection c = DBConnection.get(); PreparedStatement ps = c.prepareStatement("SELECT * FROM assignments WHERE id = ?")) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) { return rs.next() ? map(rs) : null; }
        } catch (SQLException e) { throw new LmsException("Database error", e); }
    }

    /** Assignments of a course; if studentId > 0, attaches that student's submission. */
    public List<Assignment> findByCourse(int courseId, int studentId) {
        List<Assignment> list = new ArrayList<>();
        try (Connection c = DBConnection.get(); PreparedStatement ps = c.prepareStatement("SELECT * FROM assignments WHERE course_id = ? ORDER BY id")) {
            ps.setInt(1, courseId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Assignment a = map(rs);
                    if (studentId > 0) a.setMySubmission(submissionDao.find(a.getId(), studentId));
                    list.add(a);
                }
            }
        } catch (SQLException e) { throw new LmsException("Database error", e); }
        return list;
    }
}
