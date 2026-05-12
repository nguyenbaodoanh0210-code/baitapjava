package com.btnhom.dao;
import com.btnhom.entity.*;
import com.btnhom.utils.*;
import java.sql.*;




public class AccountDAO {
	public Account checkLogin(String user,String pass) {
	String sql = "SELECT useId, useName, password, fullName, role FROM account WHERE useName = ? AND password = ?";
	try(Connection cnn = DBContext.getConnection();
			PreparedStatement ps = cnn.prepareStatement(sql)){
		ps.setString(1, user);
		ps.setString(2, pass);
		try(ResultSet rs = ps.executeQuery()){
			if(rs.next()) {
				Account acc = new Account();
                acc.setUseId(rs.getInt("useId"));
                acc.setUseName(rs.getString("useName"));
                acc.setPassword(rs.getString("password"));
                acc.setFullName(rs.getString("fullName"));
                acc.setRole(rs.getBoolean("role"));
                return acc;
			}
		}
		
	}catch (Exception e) {
		e.printStackTrace();
	}
	return null;
	
}
	}
