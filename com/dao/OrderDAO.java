package com.dao;

import com.util.DBConnection;
import java.sql.*;

public class OrderDAO {
    public int createOrder(double total, int userId) {
        String sql = "INSERT INTO orders (priceTotal, useId) VALUES (?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setDouble(1, total);
            ps.setInt(2, userId);
            ps.executeUpdate();
            
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return -1;
    }

    public void createOrderDetail(int orderId, int productId, int quantity, double price) {
        String sql = "INSERT INTO detailOrder (orderId, productId, quantity, priceAtSale) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, orderId);
            ps.setInt(2, productId);
            ps.setInt(3, quantity);
            ps.setDouble(4, price);
            ps.executeUpdate();
        } catch (Exception e) { e.printStackTrace(); }
    }
}
