package com.model;

public class User {
    private int userId;
    private String userName;
    private String password;
    private String fullName;
    private boolean role;

    public User() {}

    public User(int userId, String userName, String password, String fullName, boolean role) {
        this.userId = userId;
        this.userName = userName;
        this.password = password;
        this.fullName = fullName;
        this.role = role;
    }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public boolean isRole() { return role; }
    public void setRole(boolean role) { this.role = role; }
}
