-- Insert Test Users (passwords are BCrypt encoded 'password123')
INSERT INTO users (user_id, email, password, full_name, phone, role, enabled, created_at, updated_at)
VALUES
(1, 'employer@jobportal.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'John Employer', '+1234567890', 'EMPLOYER', true, NOW(), NOW()),
(2, 'applicant@jobportal.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Jane Applicant', '+0987654321', 'APPLICANT', true, NOW(), NOW()),
(3, 'employer2@jobportal.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Tech Corp HR', '+1122334455', 'EMPLOYER', true, NOW(), NOW()),
(4, 'applicant2@jobportal.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Bob Developer', '+5566778899', 'APPLICANT', true, NOW(), NOW())
ON CONFLICT DO NOTHING;

-- Insert Test Jobs
INSERT INTO jobs (job_id, employer_id, title, description, company, location, job_type, salary_min, salary_max, status, posted_date, closing_date, requirements, created_at, updated_at)
VALUES
(1, 1, 'Senior Java Developer', 'We are seeking an experienced Java Developer to join our team. You will be responsible for developing high-quality applications using Java, Spring Boot, and related technologies.', 'Tech Innovations Inc', 'New York, NY', 'FULL_TIME', 80000, 120000, 'ACTIVE', NOW(), NOW() + INTERVAL '30 days', 'Requirements:
- 5+ years of Java development experience
- Strong knowledge of Spring Boot framework
- Experience with RESTful APIs
- Database experience (PostgreSQL/MySQL)
- Knowledge of microservices architecture
- Excellent problem-solving skills', NOW(), NOW()),

(2, 1, 'Frontend Developer', 'Join our frontend team to build amazing user interfaces using modern JavaScript frameworks.', 'Tech Innovations Inc', 'San Francisco, CA', 'FULL_TIME', 70000, 100000, 'ACTIVE', NOW(), NOW() + INTERVAL '45 days', 'Requirements:
- 3+ years of frontend development
- React.js or Vue.js experience
- HTML5, CSS3, JavaScript (ES6+)
- Responsive design principles
- RESTful API integration', NOW(), NOW()),

(3, 3, 'DevOps Engineer', 'Looking for a DevOps engineer to maintain and improve our CI/CD pipelines and infrastructure.', 'Cloud Solutions Ltd', 'Remote', 'FULL_TIME', 90000, 130000, 'ACTIVE', NOW(), NOW() + INTERVAL '60 days', 'Requirements:
- Docker and Kubernetes experience
- AWS/Azure/GCP cloud platforms
- CI/CD tools (Jenkins, GitLab CI)
- Infrastructure as Code (Terraform)
- Linux administration', NOW(), NOW()),

(4, 3, 'Data Analyst Intern', 'Summer internship opportunity for aspiring data analysts. Learn from experienced professionals.', 'Cloud Solutions Ltd', 'Boston, MA', 'INTERNSHIP', 20000, 30000, 'ACTIVE', NOW(), NOW() + INTERVAL '20 days', 'Requirements:
- Currently pursuing degree in Data Science/Statistics
- Python or R programming
- SQL knowledge
- Data visualization tools
- Analytical mindset', NOW(), NOW()),

(5, 1, 'Project Manager', 'Experienced project manager needed to lead software development projects.', 'Tech Innovations Inc', 'Chicago, IL', 'FULL_TIME', 85000, 110000, 'CLOSED', NOW() - INTERVAL '10 days', NOW() - INTERVAL '5 days', 'Requirements:
- 7+ years project management experience
- PMP certification preferred
- Agile/Scrum methodology
- Excellent communication skills
- Technical background', NOW(), NOW())
ON CONFLICT DO NOTHING;

-- Insert Applicant Profiles
INSERT INTO applicant_profiles (profile_id, user_id, resume_url, skills, experience, education, linkedin, portfolio, created_at, updated_at)
VALUES
(1, 2, '/uploads/resumes/resume_2_sample.pdf', 'Java, Spring Boot, Hibernate, PostgreSQL, REST APIs, Microservices, Docker, Git', 5, 'Bachelor of Science in Computer Science', 'https://linkedin.com/in/janeapplicant', 'https://janeapplicant.dev', NOW(), NOW()),
(2, 4, '/uploads/resumes/resume_4_sample.pdf', 'Python, Django, Flask, JavaScript, React, SQL, Machine Learning, Data Analysis', 3, 'Master of Science in Data Science', 'https://linkedin.com/in/bobdeveloper', 'https://bobdeveloper.com', NOW(), NOW())
ON CONFLICT DO NOTHING;

-- Insert Test Applications
INSERT INTO applications (application_id, job_id, applicant_id, cover_letter, status, applied_date, reviewed_date, notes, created_at, updated_at)
VALUES
(1, 1, 2, 'I am very interested in the Senior Java Developer position. With 5 years of experience in Java development and strong expertise in Spring Boot, I believe I would be a great fit for your team.', 'SHORTLISTED', NOW() - INTERVAL '3 days', NOW() - INTERVAL '1 day', 'Strong candidate, schedule for interview', NOW(), NOW()),

(2, 2, 2, 'I would love to join your frontend team. My experience with React and modern JavaScript makes me well-suited for this role.', 'PENDING', NOW() - INTERVAL '1 day', NULL, NULL, NOW(), NOW()),

(3, 3, 4, 'As a DevOps enthusiast with hands-on experience in Docker and Kubernetes, I am excited about this opportunity.', 'REVIEWED', NOW() - INTERVAL '5 days', NOW() - INTERVAL '2 days', 'Good technical skills, consider for next round', NOW(), NOW()),

(4, 4, 4, 'I am currently pursuing my Master''s in Data Science and would be thrilled to intern at Cloud Solutions Ltd.', 'ACCEPTED', NOW() - INTERVAL '7 days', NOW() - INTERVAL '3 days', 'Excellent academic background, offered position', NOW(), NOW())
ON CONFLICT DO NOTHING;

-- Reset sequences (PostgreSQL specific)
SELECT setval('users_user_id_seq', (SELECT MAX(user_id) FROM users));
SELECT setval('jobs_job_id_seq', (SELECT MAX(job_id) FROM jobs));
SELECT setval('applicant_profiles_profile_id_seq', (SELECT MAX(profile_id) FROM applicant_profiles));
SELECT setval('applications_application_id_seq', (SELECT MAX(application_id) FROM applications));