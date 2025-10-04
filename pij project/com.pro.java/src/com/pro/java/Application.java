package com.pro.java;

import java.sql.Date;

public class Application {
    private int id;
    private int applicantId;
    private int jobId;
    private String status;
    private Date applyDate;

    public Application() {}

    public Application(int applicantId, int jobId) {
        this.applicantId = applicantId;
        this.jobId = jobId;
        this.status = "applied";
        this.applyDate = new Date(System.currentTimeMillis());
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
    public int getApplicantId()
    { 
    	return applicantId;
    }
    public void setApplicantId(int applicantId)
    { 
    	this.applicantId = applicantId; 
    }
    public int getJobId()
    { 
    	return jobId; 
    }
    public void setJobId(int jobId) 
    {
    	this.jobId = jobId;
    }
    public String getStatus() 
    { 
    	return status; 
    }
    public void setStatus(String status)
    { 
    	this.status = status;
    }
    public Date getApplyDate()
    { 
    	return applyDate;
    }
    public void setApplyDate(Date applyDate)
    { 
    	this.applyDate = applyDate;
    }

    @Override
    public String toString() {
        return "Application ID: " + id + " | Applicant: " + applicantId + 
               " | Job: " + jobId + " | Status: " + status + " | Date: " + applyDate;
    }
}