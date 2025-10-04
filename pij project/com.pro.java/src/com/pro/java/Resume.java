package com.pro.java;

import java.sql.Date;

public class Resume {
    private int id;
    private int applicantId;
    private String content;
    private Date uploadedDate;

    public Resume() {}

    public Resume(int applicantId, String content) 
    {
        this.applicantId = applicantId;
        this.content = content;
        this.uploadedDate = new Date(System.currentTimeMillis());
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
    public String getContent() 
    { 
    	return content; 
    }
    public void setContent(String content) 
    {
    	this.content = content; 
    }
    public Date getUploadedDate()
    { 
    	return uploadedDate; 
    }
    public void setUploadedDate(Date uploadedDate) 
    { 
    	this.uploadedDate = uploadedDate; 
    }

    @Override
    public String toString() {
        return "Resume ID: " + id + " | Applicant: " + applicantId + 
               " | Uploaded: " + uploadedDate + "\nContent: " + 
               (content.length() > 100 ? content.substring(0, 100) + "..." : content);
    }
}