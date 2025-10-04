package com.pro.java;

import java.util.*;

public class Applicant extends User {
    public Applicant(String username, String password, String email) {
        super(username, password, email, "applicant");
    }

    @Override
    public void showMenu() {
        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.println("\n=== APPLICANT DASHBOARD ===");
            System.out.println("1. Search Jobs");
            System.out.println("2. Apply for Job");
            System.out.println("3. Upload/Update Resume");
            System.out.println("4. Track Application Status");
            System.out.println("5. Update Profile");
            System.out.println("6. Logout");
            System.out.print("Choose option: ");
            
            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1:
                    searchJobs(sc);
                    break;
                case 2:
                    applyJob(sc);
                    break;
                case 3:
                    manageResume(sc);
                    break;
                case 4:
                    trackStatus(sc);
                    break;
                case 5:
                    updateProfile(sc);
                    break;
                case 6:
                    System.out.println("Logging out...");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void searchJobs(Scanner sc) {
        System.out.println("\n=== JOB SEARCH ===");
        System.out.print("Location (press enter to skip): ");
        String location = sc.nextLine();
        System.out.print("Minimum Salary (0 to skip): ");
        double minSalary = sc.nextDouble();
        sc.nextLine();
        System.out.print("Job Title/Designation (press enter to skip): ");
        String designation = sc.nextLine();

        JobDAO jobDAO = new JobDAO();
        List<Job> jobs = jobDAO.searchJobs(location, minSalary, designation);
        
        if (jobs.isEmpty()) {
            System.out.println("No jobs found matching your criteria.");
        } else {
            System.out.println("\nFound " + jobs.size() + " job(s):");
            for (int i = 0; i < jobs.size(); i++) {
                System.out.println((i + 1) + ". " + jobs.get(i));
            }
        }
    }

    private void applyJob(Scanner sc) {
        System.out.println("\n=== APPLY FOR JOB ===");
        System.out.print("Enter Job ID: ");
        int jobId = sc.nextInt();
        sc.nextLine();

        ApplicationDAO appDAO = new ApplicationDAO();
        boolean success = appDAO.applyForJob(this.id, jobId);
        
        if (success) {
            System.out.println("Application submitted successfully!");
            
            // Send email alerts
            JobDAO jobDAO = new JobDAO();
            Job job = jobDAO.getJobById(jobId);
            if (job != null) {
                sendEmailAlert("You have applied for: " + job.getTitle(), this.email);
                UserDAO userDAO = new UserDAO();
                User company = userDAO.getUserById(job.getCompanyId());
                if (company != null) {
                    sendEmailAlert("New application received for: " + job.getTitle() + " from " + this.username, company.getEmail());
                }
            }
        } else {
            System.out.println("Failed to submit application. You may have already applied for this job.");
        }
    }

    private void manageResume(Scanner sc) {
        System.out.println("\n=== MANAGE RESUME ===");
        System.out.print("Enter your resume content: ");
        String content = sc.nextLine();

        ResumeDAO resumeDAO = new ResumeDAO();
        boolean success = resumeDAO.saveOrUpdateResume(this.id, content);
        
        if (success) {
            System.out.println("Resume saved successfully!");
        } else {
            System.out.println("Failed to save resume.");
        }
    }

    private void trackStatus(Scanner sc) {
        System.out.println("\n=== APPLICATION STATUS ===");
        ApplicationDAO appDAO = new ApplicationDAO();
        List<Application> applications = appDAO.getApplicationsByApplicant(this.id);
        
        if (applications.isEmpty()) {
            System.out.println("No applications found.");
        } else {
            System.out.println("Your Applications:");
            for (Application app : applications) {
                JobDAO jobDAO = new JobDAO();
                Job job = jobDAO.getJobById(app.getJobId());
                System.out.println("Job: " + job.getTitle() + " | Status: " + app.getStatus() + " | Date: " + app.getApplyDate());
            }
        }
    }

    private void updateProfile(Scanner sc) {
        System.out.println("\n=== UPDATE PROFILE ===");
        System.out.print("New Email: ");
        this.email = sc.nextLine();
        System.out.print("New Profile Description: ");
        this.profile = sc.nextLine();

        UserDAO userDAO = new UserDAO();
        boolean success = userDAO.updateUser(this);
        
        if (success) {
            System.out.println("Profile updated successfully!");
        } else {
            System.out.println("Failed to update profile.");
        }
    }
}