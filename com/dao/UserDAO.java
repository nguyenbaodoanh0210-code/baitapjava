package com.dao;

import com.model.User;
import com.util.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UserDAO {
    public User login(String username, String password) {
        String sql = "SELECT * FROM account WHERE useName = ? AND password = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
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
}
