package com.lms.dao;

import com.lms.exception.LmsException;
import com.lms.model.Course;
import com.lms.util.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CourseDao implements Dao<Course> {
    private static final String BASE =
        "SELECT c.*, u.name AS instructor_name FROM courses c JOIN users u ON u.id = c.instructor_id ";

    private Course map(ResultSet rs) throws SQLException {
        Course c = new Course();
        c.setId(rs.getInt("id"));
        c.setTitle(rs.getString("title"));
        c.setDescription(rs.getString("description"));
        c.setInstructorId(rs.getInt("instructor_id"));
        c.setInstructorName(rs.getString("instructor_name"));
        return c;
    }

    private List<Course> query(String where, Integer param) {
        List<Course> list = new ArrayList<>();
        try (Connection c = DBConnection.get(); PreparedStatement ps = c.prepareStatement(BASE + where + " ORDER BY c.id DESC")) {
            if (param != null) ps.setInt(1, param);
            try (ResultSet rs = ps.executeQuery()) { while (rs.next()) list.add(map(rs)); }
        } catch (SQLException e) { throw new LmsException("Database error", e); }
        return list;
    }

    public int create(String title, String description, int instructorId) {
        String sql = "INSERT INTO courses(title,description,instructor_id) VALUES (?,?,?)";
        try (Connection c = DBConnection.get(); PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, title);
            ps.setString(2, description);
            ps.setInt(3, instructorId);
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) { keys.next(); return keys.getInt(1); }
        } catch (SQLException e) { throw new LmsException("Could not create course", e); }
    }

    public List<Course> findByInstructor(int instructorId) { return query("WHERE c.instructor_id = ?", instructorId); }

    @Override public Course findById(int id) {
        List<Course> l = query("WHERE c.id = ?", id);
        return l.isEmpty() ? null : l.get(0);
    }

    @Override public List<Course> findAll() { return query("", null); }

    @Override public boolean delete(int id) {
        try (Connection c = DBConnection.get(); PreparedStatement ps = c.prepareStatement("DELETE FROM courses WHERE id = ?")) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { throw new LmsException("Database error", e); }
    }
}
