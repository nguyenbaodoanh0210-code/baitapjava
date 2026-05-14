package com.dao;

import com.util.DBConnection;
import java.sql.*;
import com.model.*;
import java.util.*;

public class CategoryDAO {
	public List<Categories> getAllCategories() {
        List<Categories> list = new ArrayList<>();
        String sql = "SELECT * FROM categories";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                list.add(new Categories(
                    rs.getInt("categoryId"), 
                    rs.getString("categoryName")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;}
    public boolean addCategory(String name) {
        String sql = "INSERT INTO categories (categoryName) VALUES (?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, name);
            return ps.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); return false; }
    }

    public boolean deleteCategory(int id) {
        String sql = "DELETE FROM categories WHERE categoryId = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); return false; }
    }
}
