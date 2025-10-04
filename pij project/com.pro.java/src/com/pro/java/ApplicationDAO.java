package com.pro.java;

import java.sql.*;
import java.sql.Date;
import java.util.*;

public class ApplicationDAO {
    public boolean applyForJob(int applicantId, int jobId) {
        // Check if already applied
        if (hasApplied(applicantId, jobId)) {
            return false;
        }

        String sql = "INSERT INTO applications (applicant_id, job_id, apply_date) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, applicantId);
            pstmt.setInt(2, jobId);
            pstmt.setDate(3, new Date(System.currentTimeMillis()));
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error applying for job: " + e.getMessage());
        }
        return false;
    }

    public boolean hasApplied(int applicantId, int jobId) {
        String sql = "SELECT COUNT(*) FROM applications WHERE applicant_id = ? AND job_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, applicantId);
            pstmt.setInt(2, jobId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("Error checking application: " + e.getMessage());
        }
        return false;
    }

    public List<Application> getApplicationsByApplicant(int applicantId) {
        List<Application> applications = new ArrayList<>();
        String sql = "SELECT * FROM applications WHERE applicant_id = ? ORDER BY apply_date DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, applicantId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                applications.add(createApplicationFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error fetching applications: " + e.getMessage());
        }
        return applications;
    }

    public List<Application> getApplicationsForJob(int jobId) {
        List<Application> applications = new ArrayList<>();
        String sql = "SELECT * FROM applications WHERE job_id = ? ORDER BY apply_date DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, jobId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                applications.add(createApplicationFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error fetching job applications: " + e.getMessage());
        }
        return applications;
    }

    public List<Application> getAllApplications() {
        List<Application> applications = new ArrayList<>();
        String sql = "SELECT * FROM applications ORDER BY apply_date DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                applications.add(createApplicationFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error fetching all applications: " + e.getMessage());
        }
        return applications;
    }

    public boolean updateApplicationStatus(int applicantId, int jobId, String status) {
        String sql = "UPDATE applications SET status = ? WHERE applicant_id = ? AND job_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, status);
            pstmt.setInt(2, applicantId);
            pstmt.setInt(3, jobId);
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating application status: " + e.getMessage());
        }
        return false;
    }

    public int getApplicationCount() {
        String sql = "SELECT COUNT(*) FROM applications";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Error counting applications: " + e.getMessage());
        }
        return 0;
    }

    private Application createApplicationFromResultSet(ResultSet rs) throws SQLException {
        Application app = new Application();
        app.setId(rs.getInt("id"));
        app.setApplicantId(rs.getInt("applicant_id"));
        app.setJobId(rs.getInt("job_id"));
        app.setStatus(rs.getString("status"));
        app.setApplyDate(rs.getDate("apply_date"));
        return app;
    }
}
