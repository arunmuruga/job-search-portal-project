package com.pro.java;

import java.sql.*;
import java.sql.Date;

public class ResumeDAO {
    public boolean saveOrUpdateResume(int applicantId, String content) {
        // Check if resume exists
        if (resumeExists(applicantId)) {
            return updateResume(applicantId, content);
        } else {
            return createResume(applicantId, content);
        }
    }

    private boolean createResume(int applicantId, String content) {
        String sql = "INSERT INTO resumes (applicant_id, content, uploaded_date) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, applicantId);
            pstmt.setString(2, content);
            pstmt.setDate(3, new Date(System.currentTimeMillis()));
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error creating resume: " + e.getMessage());
        }
        return false;
    }

    private boolean updateResume(int applicantId, String content) {
        String sql = "UPDATE resumes SET content = ?, uploaded_date = ? WHERE applicant_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, content);
            pstmt.setDate(2, new Date(System.currentTimeMillis()));
            pstmt.setInt(3, applicantId);
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating resume: " + e.getMessage());
        }
        return false;
    }

    public boolean resumeExists(int applicantId) {
        String sql = "SELECT COUNT(*) FROM resumes WHERE applicant_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, applicantId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("Error checking resume: " + e.getMessage());
        }
        return false;
    }

    public Resume getResumeByApplicant(int applicantId) {
        String sql = "SELECT * FROM resumes WHERE applicant_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, applicantId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                Resume resume = new Resume();
                resume.setId(rs.getInt("id"));
                resume.setApplicantId(rs.getInt("applicant_id"));
                resume.setContent(rs.getString("content"));
                resume.setUploadedDate(rs.getDate("uploaded_date"));
                return resume;
            }
        } catch (SQLException e) {
            System.err.println("Error fetching resume: " + e.getMessage());
        }
        return null;
    }
}