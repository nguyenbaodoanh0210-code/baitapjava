package com.btnhom.utils;
import java.sql.*;

public class DBContext {
      public static Connection getConnection() throws Exception{
    	  String url = "jdbc:sqlserver://localhost:1433;databaseName=java;encrypt=true;trustServerCertificate=true;";
          String user = "sa";
          String pass = "123456";
          Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
          return DriverManager.getConnection(url,user,pass);
      }
}