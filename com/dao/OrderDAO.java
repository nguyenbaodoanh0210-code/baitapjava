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
        String sql = "INSERT INTO detailorder (orderId, productId, quantity, priceAtSale) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, orderId);
            ps.setInt(2, productId);
            ps.setInt(3, quantity);
            ps.setDouble(4, price);
            ps.executeUpdate();
        } catch (Exception e) { e.printStackTrace(); }
    }
    public java.util.List<Object[]> getAllOrders() {
        java.util.List<Object[]> list = new java.util.ArrayList<>();
        String sql = "SELECT o.*, a.fullName FROM orders o JOIN account a ON o.useId = a.useId ORDER BY o.orderDate DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new Object[]{rs.getInt("orderId"), rs.getTimestamp("orderDate"), rs.getDouble("priceTotal"), rs.getString("fullName")});
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    public java.util.List<Object[]> getOrdersByUser(int userId) {
        java.util.List<Object[]> list = new java.util.ArrayList<>();
        String sql = "SELECT * FROM orders WHERE useId = ? ORDER BY orderDate DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new Object[]{rs.getInt("orderId"), rs.getTimestamp("orderDate"), rs.getDouble("priceTotal"), "Bạn"});
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    public java.util.List<Object[]> getOrderDetails(int orderId) {
        java.util.List<Object[]> list = new java.util.ArrayList<>();
        String sql = "SELECT d.*, p.productName FROM detailorder d JOIN products p ON d.productId = p.productId WHERE d.orderId = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, orderId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new Object[]{rs.getString("productName"), rs.getInt("quantity"), rs.getDouble("priceAtSale")});
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    public double getDailyRevenue() {
        String sql = "SELECT SUM(priceTotal) FROM orders WHERE DATE(orderDate) = CURDATE()";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getDouble(1);
        } catch (Exception e) { e.printStackTrace(); }
        return 0;
    }

    public double getMonthlyRevenue() {
        String sql = "SELECT SUM(priceTotal) FROM orders WHERE MONTH(orderDate) = MONTH(CURDATE()) AND YEAR(orderDate) = YEAR(CURDATE())";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getDouble(1);
        } catch (Exception e) { e.printStackTrace(); }
        return 0;
    }

    public double getYearlyRevenue() {
        String sql = "SELECT SUM(priceTotal) FROM orders WHERE YEAR(orderDate) = YEAR(CURDATE())";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getDouble(1);
        } catch (Exception e) { e.printStackTrace(); }
        return 0;
    }

    public double getTotalRevenue() {
        String sql = "SELECT SUM(priceTotal) FROM orders";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getDouble(1);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return 0;
    }
    public java.util.LinkedHashMap<String, Double> getRevenueLast7Days() {
        java.util.LinkedHashMap<String, Double> map = new java.util.LinkedHashMap<>();
        String sql = "SELECT DATE(orderDate) as date, SUM(priceTotal) as total FROM orders " +
                     "WHERE orderDate >= DATE_SUB(CURDATE(), INTERVAL 6 DAY) " +
                     "GROUP BY DATE(orderDate) ORDER BY DATE(orderDate) ASC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                map.put(rs.getString("date"), rs.getDouble("total"));
            }
        } catch (Exception e) { e.printStackTrace(); }
        return map;
    }

    public java.util.LinkedHashMap<String, Double> getRevenueLast6Months() {
        java.util.LinkedHashMap<String, Double> map = new java.util.LinkedHashMap<>();
        String sql = "SELECT DATE_FORMAT(orderDate, '%Y-%m') as month, SUM(priceTotal) as total FROM orders " +
                     "WHERE orderDate >= DATE_SUB(CURDATE(), INTERVAL 5 MONTH) " +
                     "GROUP BY month ORDER BY month ASC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                map.put(rs.getString("month"), rs.getDouble("total"));
            }
        } catch (Exception e) { e.printStackTrace(); }
        return map;
    }

    public java.util.List<Object[]> getTopSellingProducts() {
        java.util.List<Object[]> list = new java.util.ArrayList<>();
        String sql = "SELECT p.productName, SUM(d.quantity) as totalQty, SUM(d.quantity * d.priceAtSale) as totalRev " +
                     "FROM detailorder d JOIN products p ON d.productId = p.productId " +
                     "GROUP BY p.productId, p.productName ORDER BY totalQty DESC LIMIT 5";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new Object[]{rs.getString("productName"), rs.getInt("totalQty"), rs.getDouble("totalRev")});
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    public int getTotalOrdersToday() {
        String sql = "SELECT COUNT(*) FROM orders WHERE DATE(orderDate) = CURDATE()";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (Exception e) { e.printStackTrace(); }
        return 0;
    }

    public double getAverageOrderValue() {
        String sql = "SELECT AVG(priceTotal) FROM orders";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getDouble(1);
        } catch (Exception e) { e.printStackTrace(); }
        return 0;
    }

    public boolean deleteOrder(int orderId) {
        String sqlDetail = "DELETE FROM detailorder WHERE orderId = ?";
        String sqlOrder = "DELETE FROM orders WHERE orderId = ?";
        
        try (Connection conn = DBConnection.getConnection()) {
            if (conn == null) return false;
            conn.setAutoCommit(false);
            try (PreparedStatement ps1 = conn.prepareStatement(sqlDetail);
                 PreparedStatement ps2 = conn.prepareStatement(sqlOrder)) {
                
                ps1.setInt(1, orderId);
                ps1.executeUpdate();

                ps2.setInt(1, orderId);
                int res = ps2.executeUpdate();

                conn.commit();
                return res > 0;
            } catch (Exception e) {
                conn.rollback();
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
