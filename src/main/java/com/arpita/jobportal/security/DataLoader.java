package com.arpita.jobportal.security;

import com.arpita.jobportal.model.*;
import com.arpita.jobportal.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Configuration
@RequiredArgsConstructor
public class DataLoader {

    private final UserRepository userRepository;
    private final JobRepository jobRepository;
    private final ApplicantProfileRepository profileRepository;
    private final ApplicationRepository applicationRepository;
    private final PasswordEncoder passwordEncoder;

    @Bean
    CommandLineRunner initDatabase() {
        return args -> {
            // Check if data already exists
            if (userRepository.count() > 0) {
                System.out.println("Database already initialized. Skipping data loading.");
                return;
            }

            System.out.println("Loading test data...");

            // Create Users
            User employer1 = new User();
            employer1.setEmail("employer@jobportal.com");
            employer1.setPassword(passwordEncoder.encode("password123"));
            employer1.setFullName("John Employer");
            employer1.setPhone("+1234567890");
            employer1.setRole(User.Role.EMPLOYER);
            employer1.setEnabled(true);
            employer1 = userRepository.save(employer1);

            User applicant1 = new User();
            applicant1.setEmail("applicant@jobportal.com");
            applicant1.setPassword(passwordEncoder.encode("password123"));
            applicant1.setFullName("Jane Applicant");
            applicant1.setPhone("+0987654321");
            applicant1.setRole(User.Role.APPLICANT);
            applicant1.setEnabled(true);
            applicant1 = userRepository.save(applicant1);

            User employer2 = new User();
            employer2.setEmail("employer2@jobportal.com");
            employer2.setPassword(passwordEncoder.encode("password123"));
            employer2.setFullName("Tech Corp HR");
            employer2.setPhone("+1122334455");
            employer2.setRole(User.Role.EMPLOYER);
            employer2.setEnabled(true);
            employer2 = userRepository.save(employer2);

            User applicant2 = new User();
            applicant2.setEmail("applicant2@jobportal.com");
            applicant2.setPassword(passwordEncoder.encode("password123"));
            applicant2.setFullName("Bob Developer");
            applicant2.setPhone("+5566778899");
            applicant2.setRole(User.Role.APPLICANT);
            applicant2.setEnabled(true);
            applicant2 = userRepository.save(applicant2);

            System.out.println("✓ Users created");

            // Create Jobs
            Job job1 = new Job();
            job1.setEmployer(employer1);
            job1.setTitle("Senior Java Developer");
            job1.setDescription("We are seeking an experienced Java Developer to join our team. You will be responsible for developing high-quality applications using Java, Spring Boot, and related technologies.");
            job1.setCompany("Tech Innovations Inc");
            job1.setLocation("New York, NY");
            job1.setJobType(Job.JobType.FULL_TIME);
            job1.setSalaryMin(new BigDecimal("80000"));
            job1.setSalaryMax(new BigDecimal("120000"));
            job1.setStatus(Job.JobStatus.ACTIVE);
            job1.setPostedDate(LocalDateTime.now());
            job1.setClosingDate(LocalDateTime.now().plusDays(30));
            job1.setRequirements("5+ years Java experience\nSpring Boot expertise\nPostgreSQL/MySQL\nMicroservices architecture");
            job1 = jobRepository.save(job1);

            Job job2 = new Job();
            job2.setEmployer(employer1);
            job2.setTitle("Frontend Developer");
            job2.setDescription("Join our frontend team to build amazing user interfaces using modern JavaScript frameworks.");
            job2.setCompany("Tech Innovations Inc");
            job2.setLocation("San Francisco, CA");
            job2.setJobType(Job.JobType.FULL_TIME);
            job2.setSalaryMin(new BigDecimal("70000"));
            job2.setSalaryMax(new BigDecimal("100000"));
            job2.setStatus(Job.JobStatus.ACTIVE);
            job2.setPostedDate(LocalDateTime.now());
            job2.setClosingDate(LocalDateTime.now().plusDays(45));
            job2.setRequirements("3+ years React.js\nHTML5, CSS3, JavaScript\nResponsive design");
            job2 = jobRepository.save(job2);

            Job job3 = new Job();
            job3.setEmployer(employer2);
            job3.setTitle("DevOps Engineer");
            job3.setDescription("Looking for a DevOps engineer to maintain and improve our CI/CD pipelines and infrastructure.");
            job3.setCompany("Cloud Solutions Ltd");
            job3.setLocation("Remote");
            job3.setJobType(Job.JobType.FULL_TIME);
            job3.setSalaryMin(new BigDecimal("90000"));
            job3.setSalaryMax(new BigDecimal("130000"));
            job3.setStatus(Job.JobStatus.ACTIVE);
            job3.setPostedDate(LocalDateTime.now());
            job3.setClosingDate(LocalDateTime.now().plusDays(60));
            job3.setRequirements("Docker & Kubernetes\nAWS/Azure/GCP\nTerraform\nCI/CD tools");
            job3 = jobRepository.save(job3);

            Job job4 = new Job();
            job4.setEmployer(employer2);
            job4.setTitle("Data Analyst Intern");
            job4.setDescription("Summer internship opportunity for aspiring data analysts.");
            job4.setCompany("Cloud Solutions Ltd");
            job4.setLocation("Boston, MA");
            job4.setJobType(Job.JobType.INTERNSHIP);
            job4.setSalaryMin(new BigDecimal("20000"));
            job4.setSalaryMax(new BigDecimal("30000"));
            job4.setStatus(Job.JobStatus.ACTIVE);
            job4.setPostedDate(LocalDateTime.now());
            job4.setClosingDate(LocalDateTime.now().plusDays(20));
            job4.setRequirements("Pursuing degree in Data Science\nPython or R\nSQL knowledge");
            job4 = jobRepository.save(job4);

            System.out.println("✓ Jobs created");

            // Create Applicant Profiles
            ApplicantProfile profile1 = new ApplicantProfile();
            profile1.setUser(applicant1);
            profile1.setSkills("Java, Spring Boot, Hibernate, PostgreSQL, REST APIs, Microservices, Docker, Git");
            profile1.setExperience(5);
            profile1.setEducation("Bachelor of Science in Computer Science");
            profile1.setLinkedin("https://linkedin.com/in/janeapplicant");
            profile1.setPortfolio("https://janeapplicant.dev");
            profileRepository.save(profile1);

            ApplicantProfile profile2 = new ApplicantProfile();
            profile2.setUser(applicant2);
            profile2.setSkills("Python, Django, Flask, JavaScript, React, SQL, Machine Learning, Data Analysis");
            profile2.setExperience(3);
            profile2.setEducation("Master of Science in Data Science");
            profile2.setLinkedin("https://linkedin.com/in/bobdeveloper");
            profile2.setPortfolio("https://bobdeveloper.com");
            profileRepository.save(profile2);

            System.out.println("✓ Profiles created");

            // Create Applications
            Application app1 = new Application();
            app1.setJob(job1);
            app1.setApplicant(applicant1);
            app1.setCoverLetter("I am very interested in the Senior Java Developer position. With 5 years of experience in Java development and strong expertise in Spring Boot, I believe I would be a great fit for your team.");
            app1.setStatus(Application.ApplicationStatus.SHORTLISTED);
            app1.setAppliedDate(LocalDateTime.now().minusDays(3));
            app1.setReviewedDate(LocalDateTime.now().minusDays(1));
            app1.setNotes("Strong candidate, schedule for interview");
            applicationRepository.save(app1);

            Application app2 = new Application();
            app2.setJob(job2);
            app2.setApplicant(applicant1);
            app2.setCoverLetter("I would love to join your frontend team. My experience with React and modern JavaScript makes me well-suited for this role.");
            app2.setStatus(Application.ApplicationStatus.PENDING);
            app2.setAppliedDate(LocalDateTime.now().minusDays(1));
            applicationRepository.save(app2);

            Application app3 = new Application();
            app3.setJob(job3);
            app3.setApplicant(applicant2);
            app3.setCoverLetter("As a DevOps enthusiast with hands-on experience in Docker and Kubernetes, I am excited about this opportunity.");
            app3.setStatus(Application.ApplicationStatus.REVIEWED);
            app3.setAppliedDate(LocalDateTime.now().minusDays(5));
            app3.setReviewedDate(LocalDateTime.now().minusDays(2));
            app3.setNotes("Good technical skills, consider for next round");
            applicationRepository.save(app3);

            Application app4 = new Application();
            app4.setJob(job4);
            app4.setApplicant(applicant2);
            app4.setCoverLetter("I am currently pursuing my Master's in Data Science and would be thrilled to intern at Cloud Solutions Ltd.");
            app4.setStatus(Application.ApplicationStatus.ACCEPTED);
            app4.setAppliedDate(LocalDateTime.now().minusDays(7));
            app4.setReviewedDate(LocalDateTime.now().minusDays(3));
            app4.setNotes("Excellent academic background, offered position");
            applicationRepository.save(app4);

            System.out.println("✓ Applications created");
            System.out.println("\n========================================");
            System.out.println("Test Data Loaded Successfully!");
            System.out.println("========================================");
            System.out.println("\nTest Credentials:");
            System.out.println("Employer: employer@jobportal.com / password123");
            System.out.println("Applicant: applicant@jobportal.com / password123");
            System.out.println("========================================\n");
        };
    }
}