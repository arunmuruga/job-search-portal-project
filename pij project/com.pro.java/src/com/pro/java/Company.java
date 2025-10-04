package com.pro.java;

import java.util.*;

public class Company extends User {
    public Company(String username, String password, String email) {
        super(username, password, email, "company");
    }

    @Override
    public void showMenu() {
        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.println("\n=== COMPANY DASHBOARD ===");
            System.out.println("1. Post Job Advertisement");
            System.out.println("2. Update Job Advertisement");
            System.out.println("3. Delete Job Advertisement");
            System.out.println("4. View Job Applications");
            System.out.println("5. Update Company Profile");
            System.out.println("6. Logout");
            System.out.print("Choose option: ");
            
            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1:
                    postJob(sc);
                    break;
                case 2:
                    updateJob(sc);
                    break;
                case 3:
                    deleteJob(sc);
                    break;
                case 4:
                    viewApplications(sc);
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

    private void postJob(Scanner sc) {
        System.out.println("\n=== POST JOB ADVERTISEMENT ===");
        System.out.print("Job Title: ");
        String title = sc.nextLine();
        System.out.print("Job Description: ");
        String description = sc.nextLine();
        System.out.print("Location: ");
        String location = sc.nextLine();
        System.out.print("Salary: $");
        double salary = sc.nextDouble();
        sc.nextLine();

        Job job = new Job(title, description, location, salary, this.id);
        JobDAO jobDAO = new JobDAO();
        boolean success = jobDAO.createJob(job);
        
        if (success) {
            System.out.println("Job advertisement posted successfully!");
        } else {
            System.out.println("Failed to post job advertisement.");
        }
    }

    private void updateJob(Scanner sc) {
        System.out.println("\n=== UPDATE JOB ADVERTISEMENT ===");
        System.out.print("Enter Job ID to update: ");
        int jobId = sc.nextInt();
        sc.nextLine();

        JobDAO jobDAO = new JobDAO();
        Job job = jobDAO.getJobById(jobId);
        
        if (job == null || job.getCompanyId() != this.id) {
            System.out.println("Job not found or you don't have permission to edit it.");
            return;
        }

        System.out.print("New Title (" + job.getTitle() + "): ");
        String title = sc.nextLine();
        if (!title.isEmpty()) job.setTitle(title);

        System.out.print("New Description: ");
        String description = sc.nextLine();
        if (!description.isEmpty()) job.setDescription(description);

        System.out.print("New Location (" + job.getLocation() + "): ");
        String location = sc.nextLine();
        if (!location.isEmpty()) job.setLocation(location);

        System.out.print("New Salary ($" + job.getSalary() + "): ");
        String salaryInput = sc.nextLine();
        if (!salaryInput.isEmpty()) {
            job.setSalary(Double.parseDouble(salaryInput));
        }

        boolean success = jobDAO.updateJob(job);
        if (success) {
            System.out.println("Job updated successfully!");
        } else {
            System.out.println("Failed to update job.");
        }
    }

    private void deleteJob(Scanner sc) {
        System.out.println("\n=== DELETE JOB ADVERTISEMENT ===");
        System.out.print("Enter Job ID to delete: ");
        int jobId = sc.nextInt();
        sc.nextLine();

        JobDAO jobDAO = new JobDAO();
        Job job = jobDAO.getJobById(jobId);
        
        if (job != null && job.getCompanyId() == this.id) {
            System.out.print("Are you sure you want to delete '" + job.getTitle() + "'? (yes/no): ");
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
            System.out.println("Job not found or you don't have permission to delete it.");
        }
    }

    private void viewApplications(Scanner sc) {
        System.out.println("\n=== VIEW JOB APPLICATIONS ===");
        JobDAO jobDAO = new JobDAO();
        List<Job> companyJobs = jobDAO.getJobsByCompany(this.id);
        
        if (companyJobs.isEmpty()) {
            System.out.println("No jobs posted by your company.");
            return;
        }

        System.out.println("Your Jobs:");
        for (Job job : companyJobs) {
            System.out.println("ID: " + job.getId() + " - " + job.getTitle());
        }

        System.out.print("Enter Job ID to view applications: ");
        int jobId = sc.nextInt();
        sc.nextLine();

        ApplicationDAO appDAO = new ApplicationDAO();
        List<Application> applications = appDAO.getApplicationsForJob(jobId);
        
        if (applications.isEmpty()) {
            System.out.println("No applications found for this job.");
        } else {
            System.out.println("Applications for Job ID " + jobId + ":");
            for (Application app : applications) {
                UserDAO userDAO = new UserDAO();
                User applicant = userDAO.getUserById(app.getApplicantId());
                System.out.println("Applicant: " + applicant.getUsername() + " | Status: " + app.getStatus() + " | Date: " + app.getApplyDate());
            }

            System.out.print("Update application status? (yes/no): ");
            String update = sc.nextLine();
            if (update.equalsIgnoreCase("yes")) {
                System.out.print("Enter Applicant ID: ");
                int applicantId = sc.nextInt();
                sc.nextLine();
                System.out.print("New Status (PENDING/APPROVED/REJECTED): ");
                String newStatus = sc.nextLine();
                
                boolean success = appDAO.updateApplicationStatus(applicantId, jobId, newStatus);
                if (success) {
                    System.out.println("Application status updated!");
                    UserDAO userDAO = new UserDAO();
                    User applicant = userDAO.getUserById(applicantId);
                    Job job = jobDAO.getJobById(jobId);
                    sendEmailAlert("Your application status for '" + job.getTitle() + "' has been updated to: " + newStatus, applicant.getEmail());
                }
            }
        }
    }

    private void updateProfile(Scanner sc) {
        System.out.println("\n=== UPDATE COMPANY PROFILE ===");
        System.out.print("New Email: ");
        this.email = sc.nextLine();
        System.out.print("New Company Description: ");
        this.profile = sc.nextLine();

        UserDAO userDAO = new UserDAO();
        boolean success = userDAO.updateUser(this);
        
        if (success) {
            System.out.println("Company profile updated successfully!");
        } else {
            System.out.println("Failed to update profile.");
        }
    }
}