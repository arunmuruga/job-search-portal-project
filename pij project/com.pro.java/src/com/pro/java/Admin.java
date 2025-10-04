package com.pro.java;

import java.util.*;

public class Admin extends User {
    public Admin(String username, String password, String email) {
        super(username, password, email, "admin");
    }

    @Override
    public void showMenu() {
        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.println("\n=== ADMIN DASHBOARD ===");
            System.out.println("1. View All Users");
            System.out.println("2. View All Jobs");
            System.out.println("3. View All Applications");
            System.out.println("4. Delete User");
            System.out.println("5. Delete Job");
            System.out.println("6. System Statistics");
            System.out.println("7. Logout");
            System.out.print("Choose option: ");
            
            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1:
                    viewAllUsers();
                    break;
                case 2:
                    viewAllJobs();
                    break;
                case 3:
                    viewAllApplications();
                    break;
                case 4:
                    deleteUser(sc);
                    break;
                case 5:
                    deleteJob(sc);
                    break;
                case 6:
                    showStatistics();
                    break;
                case 7:
                    System.out.println("Logging out...");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void viewAllUsers() {
        UserDAO userDAO = new UserDAO();
        List<User> users = userDAO.getAllUsers();
        
        System.out.println("\n=== ALL USERS ===");
        for (User user : users) {
            System.out.println("ID: " + user.getId() + " | Username: " + user.getUsername() + 
                             " | Role: " + user.getRole() + " | Email: " + user.getEmail());
        }
    }

    private void viewAllJobs() {
        JobDAO jobDAO = new JobDAO();
        List<Job> jobs = jobDAO.getAllJobs();
        
        System.out.println("\n=== ALL JOBS ===");
        for (Job job : jobs) {
            UserDAO userDAO = new UserDAO();
            User company = userDAO.getUserById(job.getCompanyId());
            System.out.println("ID: " + job.getId() + " | Title: " + job.getTitle() + 
                             " | Company: " + (company != null ? company.getUsername() : "Unknown") +
                             " | Location: " + job.getLocation() + " | Salary: $" + job.getSalary());
        }
    }

    private void viewAllApplications() {
        ApplicationDAO appDAO = new ApplicationDAO();
        List<Application> applications = appDAO.getAllApplications();
        
        System.out.println("\n=== ALL APPLICATIONS ===");
        for (Application app : applications) {
            UserDAO userDAO = new UserDAO();
            User applicant = userDAO.getUserById(app.getApplicantId());
            JobDAO jobDAO = new JobDAO();
            Job job = jobDAO.getJobById(app.getJobId());
            System.out.println("Applicant: " + (applicant != null ? applicant.getUsername() : "Unknown") +
                             " | Job: " + (job != null ? job.getTitle() : "Unknown") +
                             " | Status: " + app.getStatus() + " | Date: " + app.getApplyDate());
        }
    }

    private void deleteUser(Scanner sc) {
        System.out.print("Enter User ID to delete: ");
        int userId = sc.nextInt();
        sc.nextLine();

        UserDAO userDAO = new UserDAO();
        User user = userDAO.getUserById(userId);
        
        if (user != null) {
            System.out.print("Are you sure you want to delete user '" + user.getUsername() + "'? (yes/no): ");
            String confirm = sc.nextLine();
            if (confirm.equalsIgnoreCase("yes")) {
                boolean success = userDAO.deleteUser(userId);
                if (success) {
                    System.out.println("User deleted successfully!");
                } else {
                    System.out.println("Failed to delete user.");
                }
            }
        } else {
            System.out.println("User not found.");
        }
    }

    private void deleteJob(Scanner sc) {
        System.out.print("Enter Job ID to delete: ");
        int jobId = sc.nextInt();
        sc.nextLine();

        JobDAO jobDAO = new JobDAO();
        Job job = jobDAO.getJobById(jobId);
        
        if (job != null) {
            System.out.print("Are you sure you want to delete job '" + job.getTitle() + "'? (yes/no): ");
            String confirm = sc.nextLine();
            if (confirm.equalsIgnoreCase("yes")) {
                boolean success = jobDAO.deleteJob(jobId);
                if (success) {
                    System.out.println("Job deleted successfully!");
                } else {
                    System.out.println("Failed to delete job.");
                }
            }
        } else {
            System.out.println("Job not found.");
        }
    }

    private void showStatistics() {
        UserDAO userDAO = new UserDAO();
        JobDAO jobDAO = new JobDAO();
        ApplicationDAO appDAO = new ApplicationDAO();

        int totalUsers = userDAO.getUserCount();
        int totalJobs = jobDAO.getJobCount();
        int totalApplications = appDAO.getApplicationCount();
        int applicants = userDAO.getUserCountByRole("applicant");
        int companies = userDAO.getUserCountByRole("company");

        System.out.println("\n=== SYSTEM STATISTICS ===");
        System.out.println("Total Users: " + totalUsers);
        System.out.println("Applicants: " + applicants);
        System.out.println("Companies: " + companies);
        System.out.println("Total Jobs: " + totalJobs);
        System.out.println("Total Applications: " + totalApplications);
        System.out.println("Average Applications per Job: " + (totalJobs > 0 ? (double) totalApplications / totalJobs : 0));
    }
}