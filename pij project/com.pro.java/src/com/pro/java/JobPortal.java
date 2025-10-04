package com.pro.java;

import java.util.*;

public class JobPortal {
    private static Scanner scanner = new Scanner(System.in);
    private static User currentUser = null;

    public static void main(String[] args) {
        System.out.println("=== WELCOME TO JOB SEARCH PORTAL ===");
        
        while (true) {
            if (currentUser == null) {
                showMainMenu();
            } else {
                currentUser.showMenu();
                currentUser = null; // Logout
            }
        }
    }

    private static void showMainMenu() {
        System.out.println("\n=== MAIN MENU ===");
        System.out.println("1. Login");
        System.out.println("2. Register");
        System.out.println("3. Browse Jobs");
        System.out.println("4. Exit");
        System.out.print("Choose option: ");
        
        int choice = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        switch (choice) {
            case 1:
                login();
                break;
            case 2:
                register();
                break;
            case 3:
                browseJobsPublic();
                break;
            case 4:
                System.out.println("Thank you for using Job Search Portal!");
                System.exit(0);
                break;
            default:
                System.out.println("Invalid choice. Please try again.");
        }
    }

    private static void login() {
        System.out.println("\n=== LOGIN ===");
        System.out.print("Username: ");
        String username = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();

        UserDAO userDAO = new UserDAO();
        User user = userDAO.getUserByUsername(username);
        
        if (user != null && user.getPassword().equals(password)) {
            currentUser = user;
            System.out.println("Login successful! Welcome, " + user.getUsername() + " (" + user.getRole() + ")");
        } else {
            System.out.println("Invalid username or password.");
        }
    }

    private static void register() {
        System.out.println("\n=== REGISTER ===");
        System.out.println("1. Register as Applicant");
        System.out.println("2. Register as Company");
        System.out.print("Choose role: ");
        
        int roleChoice = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Username: ");
        String username = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Profile Description: ");
        String profile = scanner.nextLine();

        User user;
        if (roleChoice == 1) {
            user = new Applicant(username, password, email);
        } else if (roleChoice == 2) {
            user = new Company(username, password, email);
        } else {
            System.out.println("Invalid role choice.");
            return;
        }

        user.setProfile(profile);
        UserDAO userDAO = new UserDAO();
        
        if (userDAO.createUser(user)) {
            System.out.println("Registration successful! You can now login.");
        } else {
            System.out.println("Registration failed. Username may already exist.");
        }
    }

    private static void browseJobsPublic() {
        System.out.println("\n=== AVAILABLE JOBS ===");
        JobDAO jobDAO = new JobDAO();
        List<Job> jobs = jobDAO.getAllJobs();
        
        if (jobs.isEmpty()) {
            System.out.println("No jobs available at the moment.");
        } else {
            for (Job job : jobs) {
                System.out.println(job);
            }
            System.out.println("\nRegister or login to apply for these jobs!");
        }
    }
}
