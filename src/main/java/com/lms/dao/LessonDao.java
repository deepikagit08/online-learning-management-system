package com.lms.dao;

import com.lms.exception.LmsException;
import com.lms.model.Lesson;
import com.lms.util.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LessonDao {

    public void add(int courseId, String title, String content) {
        String sql = "INSERT INTO lessons(course_id,title,content) VALUES (?,?,?)";
        try (Connection c = DBConnection.get(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, courseId);
            ps.setString(2, title);
            ps.setString(3, content);
            ps.executeUpdate();
        } catch (SQLException e) { throw new LmsException("Could not add lesson", e); }
    }

    /** Lessons of a course; 'completed' is filled for the given student (use 0 for none). */
    public List<Lesson> findByCourse(int courseId, int studentId) {
        String sql = "SELECT l.*, (SELECT COUNT(*) FROM lesson_progress p WHERE p.lesson_id = l.id AND p.student_id = ?) AS done "
                   + "FROM lessons l WHERE l.course_id = ? ORDER BY l.id";
        List<Lesson> list = new ArrayList<>();
        try (Connection c = DBConnection.get(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, studentId);
            ps.setInt(2, courseId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Lesson l = new Lesson();
                    l.setId(rs.getInt("id"));
                    l.setCourseId(courseId);
                    l.setTitle(rs.getString("title"));
                    l.setContent(rs.getString("content"));
                    l.setCompleted(rs.getInt("done") > 0);
                    list.add(l);
                }
            }
        } catch (SQLException e) { throw new LmsException("Database error", e); }
        return list;
    }

    public void markComplete(int studentId, int lessonId) {
        String sql = "INSERT IGNORE INTO lesson_progress(student_id, lesson_id) VALUES (?,?)";
        try (Connection c = DBConnection.get(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, studentId);
            ps.setInt(2, lessonId);
            ps.executeUpdate();
        } catch (SQLException e) { throw new LmsException("Database error", e); }
    }
}
