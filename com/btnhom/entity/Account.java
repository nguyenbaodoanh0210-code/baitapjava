package com.btnhom.entity;

public class Account {
	private int useId;
    private String useName;
    private String password;
    private String fullName;
    private boolean role;
    public Account() {}
    public Account(int useId, String useName, String password, String fullName, boolean role) {
    	this.useId = useId;
        this.useName = useName;
        this.password = password;
        this.fullName = fullName;
        this.role = role;
       
    }
    public int getUseId() { return useId; }
    public String getUseName (){return useName;}
    public String getPassWord(){return password;}
    public String getFullName (){return fullName;}
    public boolean getRole() { return role;}
    public void setUseId(int useId) { this.useId = useId; }
    public void setUseName(String useName) { this.useName = useName; }
    public void setPassword(String password) { this.password = password; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public void setRole(boolean role) { this.role = role; }
    
}
