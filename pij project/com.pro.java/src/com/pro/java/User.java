package com.pro.java;

public abstract class User {
    protected int id;
    protected String username;
    protected String password;
    protected String email;
    protected String role;
    protected String profile;

    public User() {}
    
    public User(String username, String password, String email, String role) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
    }

    public abstract void showMenu();

    // Getters and setters
    public int getId() 
    {
    	return id;
    }
    public void setId(int id)
    { 
    	this.id = id;
    }
    public String getUsername()
    { 
    	return username; 
    }
    public String getPassword() 
    { 
    	return password;
    }
    public String getEmail()
    { 
    	return email; 
    }
    public String getRole() 
    { 
    	return role;
    }
    public String getProfile() 
    { 
    	return profile; 
    }
    public void setProfile(String profile)
    { 
    	this.profile = profile; 
    }

    public void sendEmailAlert(String message, String recipient) 
    {
        System.out.println("\n=== EMAIL ALERT ===");
        System.out.println("To: " + recipient);
        System.out.println("Message: " + message);
        System.out.println("===================\n");
    }
}