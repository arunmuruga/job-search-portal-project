package com.pro.java;
import java.sql.*;
import java.util.*;

public class JobDAO {
    public boolean createJob(Job job) {
        String sql = "INSERT INTO jobs (title, description, location, salary, company_id) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setString(1, job.getTitle());
            pstmt.setString(2, job.getDescription());
            pstmt.setString(3, job.getLocation());
            pstmt.setDouble(4, job.getSalary());
            pstmt.setInt(5, job.getCompanyId());
            
            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    job.setId(rs.getInt(1));
                }
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Error creating job: " + e.getMessage());
        }
        return false;
    }

    public Job getJobById(int id) {
        String sql = "SELECT * FROM jobs WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return createJobFromResultSet(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error fetching job: " + e.getMessage());
        }
        return null;
    }

    public List<Job> getAllJobs() {
        List<Job> jobs = new ArrayList<>();
        String sql = "SELECT * FROM jobs WHERE status = 'open'";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                jobs.add(createJobFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error fetching jobs: " + e.getMessage());
        }
        return jobs;
    }

    public List<Job> getJobsByCompany(int companyId) {
        List<Job> jobs = new ArrayList<>();
        String sql = "SELECT * FROM jobs WHERE company_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, companyId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                jobs.add(createJobFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error fetching company jobs: " + e.getMessage());
        }
        return jobs;
    }

    public List<Job> searchJobs(String location, double minSalary, String designation) {
        List<Job> jobs = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM jobs WHERE status = 'open'");
        List<Object> params = new ArrayList<>();
        
        if (location != null && !location.isEmpty()) {
            sql.append(" AND location LIKE ?");
            params.add("%" + location + "%");
        }
        
        if (minSalary > 0) {
            sql.append(" AND salary >= ?");
            params.add(minSalary);
        }
        
        if (designation != null && !designation.isEmpty()) {
            sql.append(" AND title LIKE ?");
            params.add("%" + designation + "%");
        }
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql.toString())) {
            
            for (int i = 0; i < params.size(); i++) {
                pstmt.setObject(i + 1, params.get(i));
            }
            
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                jobs.add(createJobFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error searching jobs: " + e.getMessage());
        }
        return jobs;
    }

    public boolean updateJob(Job job) {
        String sql = "UPDATE jobs SET title = ?, description = ?, location = ?, salary = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, job.getTitle());
            pstmt.setString(2, job.getDescription());
            pstmt.setString(3, job.getLocation());
            pstmt.setDouble(4, job.getSalary());
            pstmt.setInt(5, job.getId());
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating job: " + e.getMessage());
        }
        return false;
    }

    public boolean deleteJob(int id) {
        String sql = "DELETE FROM jobs WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting job: " + e.getMessage());
        }
        return false;
    }

    public int getJobCount() {
        String sql = "SELECT COUNT(*) FROM jobs";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Error counting jobs: " + e.getMessage());
        }
        return 0;
    }

    private Job createJobFromResultSet(ResultSet rs) throws SQLException {
        Job job = new Job();
        job.setId(rs.getInt("id"));
        job.setTitle(rs.getString("title"));
        job.setDescription(rs.getString("description"));
        job.setLocation(rs.getString("location"));
        job.setSalary(rs.getDouble("salary"));
        job.setCompanyId(rs.getInt("company_id"));
        job.setStatus(rs.getString("status"));
        return job;
    }
}