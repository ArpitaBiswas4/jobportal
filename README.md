# **Job Portal System**

<div align="center">

![Java](https://img.shields.io/badge/Java-17-orange?style=for-the-badge&logo=java)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.7-brightgreen?style=for-the-badge&logo=spring)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-12+-blue?style=for-the-badge&logo=postgresql)

**A Full-Stack Job Portal Web Application**

</div>

## **📖 About the Project**

The **Job Portal System** is a comprehensive web application that connects employers with job seekers. Built using **Spring Boot** and **PostgreSQL**, it provides a secure, scalable platform for managing job postings and applications with role-based access control.

### **Project Highlights:**
- 🔐 Secure authentication with Spring Security
- 👥 Dual user roles (Employer & Applicant)
- 📊 Real-time application tracking
- 🔍 Advanced job search and filtering
- 📱 Responsive mobile-friendly design
- 🎨 Clean, intuitive user interface

---

## **✨ Features**

### **For Employers**
- ✅ Post, edit, and delete job listings
- ✅ View and manage all job applications
- ✅ Update application status (Pending/Reviewed/Shortlisted/Accepted/Rejected)
- ✅ Add notes to applications
- ✅ Dashboard with statistics (Total, Active, Closed jobs)
- ✅ Real-time application tracking

### **For Applicants**
- ✅ Browse and search jobs by keyword, location, and type
- ✅ Apply for jobs with cover letters
- ✅ Track application status in real-time
- ✅ Maintain professional profile (skills, experience, education)
- ✅ Resume management
- ✅ Withdraw pending applications
- ✅ Dashboard with application statistics

### **General Features**
- ✅ Secure user registration and login
- ✅ Role-based access control
- ✅ Password encryption (BCrypt)
- ✅ Search and filter functionality
- ✅ Pagination for large datasets
- ✅ Responsive Bootstrap UI
- ✅ Form validation
- ✅ Success/error notifications

---

## **🛠️ Technology Stack**

### **Backend**
| Technology | Version | Purpose |
|------------|---------|---------|
| Java | 17 | Programming Language |
| Spring Boot | 3.5.7 | Application Framework |
| Spring Data JPA | - | Data Access Layer |
| Spring Security | - | Authentication & Authorization |
| Hibernate | - | ORM Framework |
| PostgreSQL | 12+ | Relational Database |
| Lombok | - | Reduce Boilerplate Code |
| Maven | 3.6+ | Build Tool |

### **Frontend**
| Technology | Version | Purpose |
|------------|---------|---------|
| Thymeleaf | - | Template Engine |
| Bootstrap | 5.3.0 | CSS Framework |
| Font Awesome | 6.4.0 | Icons |
| JavaScript | ES6+ | Client-side Logic |

---

### **MVC Architecture Pattern**
- **Model**: Entity classes (User, Job, Application, ApplicantProfile)
- **View**: Thymeleaf templates
- **Controller**: REST and Web controllers

---

## **🚀 Installation**

### **Prerequisites**
- ☕ Java 17 or higher ([Download](https://www.oracle.com/java/technologies/downloads/))
- 🐘 PostgreSQL 12 or higher ([Download](https://www.postgresql.org/download/))
- 📦 Maven 3.6 or higher ([Download](https://maven.apache.org/download.cgi))
- 💻 IDE (IntelliJ IDEA, Eclipse, or VS Code)

### **Step 1: Clone the Repository**
```bash
git clone https://github.com/ArpitaBiswas4/jobportal.git
cd job-portal
```

### **Step 2: Create Database**
```bash
# Login to PostgreSQL
psql -U postgres

# Create database
CREATE DATABASE job_portal_db;

# Exit
\q
```

### **Step 3: Configure Application**
Edit `src/main/resources/application.properties`:

```properties
# Database Configuration
spring.datasource.url=jdbc:postgresql://localhost:5432/job_portal_db
spring.datasource.username=postgres
spring.datasource.password=your_password

# JPA Configuration
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

# Server Configuration
server.port=8080
```

### **Step 4: Build the Project**
```bash
mvn clean install
```

### **Step 5: Run the Application**
```bash
mvn spring-boot:run
```

Or run directly from IDE:
- Right-click `JobportalApplication.java`
- Select "Run 'JobportalApplication'"

### **Step 6: Access Application**
Open browser and navigate to:
```
http://localhost:8080
```

---

## **👤 Usage**

### **Test Credentials**

| Role | Email | Password |
|------|-------|----------|
| **Employer** | employer@jobportal.com | password123 |
| **Applicant** | applicant@jobportal.com | password123 |


## **👨‍💻 Contact**

**Developer**: Arpita Biswas

- **Email**: biswasa13198@gmail.com
- **GitHub**: [@arpitabiswas4](https://github.com/ArpitaBiswas4)
- **LinkedIn**: [Arpita Biswas](https://www.linkedin.com/in/arpita-biswas-994937320)
