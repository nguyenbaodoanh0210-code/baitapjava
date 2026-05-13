package com.dao;

import com.model.User;
import com.util.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UserDAO {
    public UserDAO() {
        ensureAdminExists();
    }

    private void ensureAdminExists() {
        try (Connection conn = DBConnection.getConnection()) {
            if (conn == null) return;
            String sql = "INSERT INTO account (useName, password, fullName, role) " +
                         "VALUES ('admin', '123', 'Administrator', 1) " +
                         "ON DUPLICATE KEY UPDATE password = '123', role = 1";
            try (java.sql.Statement st = conn.createStatement()) {
                st.executeUpdate(sql);
            }
        } catch (Exception e) { e.printStackTrace(); }
    }

    public User login(String username, String password) {
        String sql = "SELECT * FROM account WHERE useName = ? AND password = ?";
        try (Connection conn = DBConnection.getConnection()) {
            if (conn == null) return null;
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, username);
                ps.setString(2, password);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    return new User(
                        rs.getInt("useId"),
                        rs.getString("useName"),
                        rs.getString("password"),
                        rs.getString("fullName"),
                        rs.getBoolean("role")
                    );
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
        return null;
    }

    public boolean register(String username, String password, String fullName) {
        String sql = "INSERT INTO account (useName, password, fullName, role) VALUES (?, ?, ?, 0)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, password);
            ps.setString(3, fullName);
            return ps.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); return false; }
    }

    public java.util.List<User> getAllUsers() {
        java.util.List<User> list = new java.util.ArrayList<>();
        String sql = "SELECT * FROM account";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new User(
                    rs.getInt("useId"),
                    rs.getString("useName"),
                    rs.getString("password"),
                    rs.getString("fullName"),
                    rs.getBoolean("role")
                ));
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    public boolean deleteUser(int userId) {
        String sql = "DELETE FROM account WHERE useId = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            return ps.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); return false; }
    }
}
