package com.util;

import java.sql.*;

public class DBConnection{
      public static Connection getConnection() throws Exception{
          String url = "jdbc:mysql://localhost:3306/fastfood_db?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
          String user = "root";
          String pass = "";
          Class.forName("com.mysql.cj.jdbc.Driver");
          return DriverManager.getConnection(url,user,pass);
      }
}