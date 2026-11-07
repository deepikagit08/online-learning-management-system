package com.lms.dao;

import com.lms.exception.LmsException;
import com.lms.model.*;
import com.lms.util.DBConnection;
import com.lms.util.PasswordUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDao implements Dao<User> {

    private User map(ResultSet rs) throws SQLException {
        User u = switch (rs.getString("role")) {
            case "ADMIN" -> new Admin();
            case "INSTRUCTOR" -> new Instructor();
            default -> new Student();
        };
        u.setId(rs.getInt("id"));
        u.setName(rs.getString("name"));
        u.setEmail(rs.getString("email"));
        u.setPasswordHash(rs.getString("password_hash"));
        return u;
    }

    public boolean emailExists(String email) {
        String sql = "SELECT 1 FROM users WHERE email = ?";
        try (Connection c = DBConnection.get(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) { return rs.next(); }
        } catch (SQLException e) { throw new LmsException("Database error", e); }
    }

    public void register(String name, String email, String password, String role) {
        String sql = "INSERT INTO users(name,email,password_hash,role) VALUES (?,?,?,?)";
        try (Connection c = DBConnection.get(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setString(2, email);
            ps.setString(3, PasswordUtil.hash(password));
            ps.setString(4, role);
            ps.executeUpdate();
        } catch (SQLException e) { throw new LmsException("Could not register user", e); }
    }

    /** Returns the user if credentials are valid, otherwise null. */
    public User authenticate(String email, String password) {
        String sql = "SELECT * FROM users WHERE email = ? AND password_hash = ?";
        try (Connection c = DBConnection.get(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, email);
            ps.setString(2, PasswordUtil.hash(password));
            try (ResultSet rs = ps.executeQuery()) { return rs.next() ? map(rs) : null; }
        } catch (SQLException e) { throw new LmsException("Login failed due to database error", e); }
    }

    @Override public User findById(int id) {
        try (Connection c = DBConnection.get(); PreparedStatement ps = c.prepareStatement("SELECT * FROM users WHERE id = ?")) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) { return rs.next() ? map(rs) : null; }
        } catch (SQLException e) { throw new LmsException("Database error", e); }
    }

    @Override public List<User> findAll() {
        List<User> list = new ArrayList<>();
        try (Connection c = DBConnection.get(); Statement st = c.createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM users ORDER BY role, name")) {
            while (rs.next()) list.add(map(rs));
        } catch (SQLException e) { throw new LmsException("Database error", e); }
        return list;
    }

    @Override public boolean delete(int id) {
        try (Connection c = DBConnection.get(); PreparedStatement ps = c.prepareStatement("DELETE FROM users WHERE id = ? AND role <> 'ADMIN'")) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { throw new LmsException("Database error", e); }
    }
}
