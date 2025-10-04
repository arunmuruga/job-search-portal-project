package com.pro.java;

public class Job {
    private int id;
    private String title;
    private String description;
    private String location;
    private double salary;
    private int companyId;
    private String status;

    public Job() {}

    public Job(String title, String description, String location, double salary, int companyId) {
        this.title = title;
        this.description = description;
        this.location = location;
        this.salary = salary;
        this.companyId = companyId;
        this.status = "open";
    }

    // Getters and setters
    public int getId() 
    { 
    	return id;
    }
    public void setId(int id)
    { 
    	this.id = id;
    }
    public String getTitle() 
    { 
    	return title; 
    }
    public void setTitle(String title) 
    { 
    	this.title = title; 
    }
    public String getDescription()
    { 
    	return description;
    }
    public void setDescription(String description)
    { 
    	this.description = description;
    }
    public String getLocation()
    { 
    	return location;
    }
    public void setLocation(String location)
    { 
    	this.location = location;
    }
    public double getSalary()
    { 
    	return salary; 
    }
    public void setSalary(double salary)
    {
    	this.salary = salary;
    }
    public int getCompanyId() 
    { 
    	return companyId; 
    }
    public void setCompanyId(int companyId)
    { 
    	this.companyId = companyId;
    }
    public String getStatus()
    { 
    	return status;
    }
    public void setStatus(String status) 
    { 
    	this.status = status; 
    }

    @Override
    public String toString() {
        return "Job ID: " + id + " | Title: " + title + " | Location: " + location + 
               " | Salary: $" + salary + " | Status: " + status;
    }
}