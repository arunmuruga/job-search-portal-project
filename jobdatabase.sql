CREATE DATABASE job_portal;
USE job_portal;

-- Users table (Applicants and Companies)
CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(50) NOT NULL,
    role ENUM('admin','applicant', 'company') NOT NULL,
    email VARCHAR(100) NOT NULL,
    profile TEXT  -- For updates (e.g., bio, contact)
);

-- Jobs table (Posted by Companies)
CREATE TABLE jobs (
    id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(100) NOT NULL,
    description TEXT,
    location VARCHAR(50),
    salary DECIMAL(10,2),
    designation VARCHAR(50),
    company_id INT,
    FOREIGN KEY (company_id) REFERENCES users(id)
);

-- Applications table (Applicant applies to Job)
CREATE TABLE application (
    id INT AUTO_INCREMENT PRIMARY KEY,
    applicant_id INT,
    job_id INT,
    status ENUM('PENDING', 'APPROVED', 'REJECTED') DEFAULT 'PENDING',
    applied_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (applicant_id) REFERENCES users(id),
    FOREIGN KEY (job_id) REFERENCES jobs(id)
);

CREATE TABLE resumes (
    id INT AUTO_INCREMENT PRIMARY KEY,
    applicant_id INT,
    content TEXT,  -- Simulated resume text (in real app, store file path or BLOB)
    uploaded_date DATE,
    FOREIGN KEY (applicant_id) REFERENCES users(id)
);
SELECT * FROM job_portal.application;
INSERT INTO users (username,password, role, email, profile) VALUES 
('admin', 'admin123', 'admin', 'admin@portal.com', 'System Administrator');
INSERT INTO users (username,password, role, email, profile) VALUES ('tech mahindra', 'comp123', 'company', 'hr@techcorp.com', 'Leading Technology Company');

INSERT INTO jobs (title, description, location, salary, company_id) VALUES 
('Java Developer', 'Develop enterprise applications using Java Spring Boot', 'New York', 85000.00, 2),
('Data Analyst', 'Analyze business data and create reports', 'London', 65000.00, 2),
('Frontend Developer', 'Create responsive web applications', 'San Francisco', 90000.00, 2);

ALTER TABLE users MODIFY role ENUM('admin', 'company', 'applicant');
TRUNCATE TABLE users;
DELETE FROM jobs where id=12;
DELETE FROM users where id=5;
alter table jobs drop column designation;
alter table jobs add column status ENUM('open', 'closed') DEFAULT 'open';
alter table application rename column applied_date to apply_date;
RENAME TABLE application TO applications;

