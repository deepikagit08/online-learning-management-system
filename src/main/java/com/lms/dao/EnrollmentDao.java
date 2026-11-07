package com.lms.dao;

import com.lms.exception.LmsException;
import com.lms.model.Course;
import com.lms.util.DBConnection;
import java.sql.*;
import java.util.*;

public class EnrollmentDao {

    public void enroll(int studentId, int courseId) {
        try (Connection c = DBConnection.get();
             PreparedStatement ps = c.prepareStatement("INSERT IGNORE INTO enrollments(student_id, course_id) VALUES (?,?)")) {
            ps.setInt(1, studentId);
            ps.setInt(2, courseId);
            ps.executeUpdate();
        } catch (SQLException e) { throw new LmsException("Could not enroll", e); }
    }

    public boolean isEnrolled(int studentId, int courseId) {
        try (Connection c = DBConnection.get();
             PreparedStatement ps = c.prepareStatement("SELECT 1 FROM enrollments WHERE student_id = ? AND course_id = ?")) {
            ps.setInt(1, studentId);
            ps.setInt(2, courseId);
            try (ResultSet rs = ps.executeQuery()) { return rs.next(); }
        } catch (SQLException e) { throw new LmsException("Database error", e); }
    }

    public Set<Integer> enrolledCourseIds(int studentId) {
        Set<Integer> ids = new HashSet<>();
        try (Connection c = DBConnection.get();
             PreparedStatement ps = c.prepareStatement("SELECT course_id FROM enrollments WHERE student_id = ?")) {
            ps.setInt(1, studentId);
            try (ResultSet rs = ps.executeQuery()) { while (rs.next()) ids.add(rs.getInt(1)); }
        } catch (SQLException e) { throw new LmsException("Database error", e); }
        return ids;
    }

    /** Enrolled courses mapped to completion percentage (0-100). */
    public Map<Course, Integer> coursesWithProgress(int studentId, CourseDao courseDao) {
        Map<Course, Integer> result = new LinkedHashMap<>();
        String sql = "SELECT e.course_id, "
                   + "(SELECT COUNT(*) FROM lessons l WHERE l.course_id = e.course_id) AS total, "
                   + "(SELECT COUNT(*) FROM lesson_progress p JOIN lessons l ON l.id = p.lesson_id "
                   + " WHERE l.course_id = e.course_id AND p.student_id = e.student_id) AS done "
                   + "FROM enrollments e WHERE e.student_id = ?";
        try (Connection c = DBConnection.get(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, studentId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Course course = courseDao.findById(rs.getInt("course_id"));
                    int total = rs.getInt("total");
                    int pct = total == 0 ? 0 : (rs.getInt("done") * 100) / total;
                    if (course != null) result.put(course, pct);
                }
            }
        } catch (SQLException e) { throw new LmsException("Database error", e); }
        return result;
    }
}
