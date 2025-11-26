package com.arpita.jobportal.service.impl;

import com.arpita.jobportal.dto.ApplicationRequest;
import com.arpita.jobportal.model.Application;
import com.arpita.jobportal.model.Application.ApplicationStatus;
import com.arpita.jobportal.model.Job;
import com.arpita.jobportal.model.User;
import com.arpita.jobportal.repository.ApplicationRepository;
import com.arpita.jobportal.repository.JobRepository;
import com.arpita.jobportal.repository.UserRepository;
import com.arpita.jobportal.service.ApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class ApplicationServiceImpl implements ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final JobRepository jobRepository;
    private final UserRepository userRepository;

    @Override
    public Application applyForJob(ApplicationRequest request, Long applicantId) {
        // Verify applicant exists and is an applicant
        User applicant = userRepository.findById(applicantId)
                .orElseThrow(() -> new RuntimeException("Applicant not found"));

        if (applicant.getRole() != User.Role.APPLICANT) {
            throw new RuntimeException("Only applicants can apply for jobs");
        }

        // Verify job exists and is active
        Job job = jobRepository.findById(request.getJobId())
                .orElseThrow(() -> new RuntimeException("Job not found"));

        if (job.getStatus() != Job.JobStatus.ACTIVE) {
            throw new RuntimeException("This job is no longer accepting applications");
        }

        // Check if already applied
        if (applicationRepository.existsByJobJobIdAndApplicantUserId(request.getJobId(), applicantId)) {
            throw new RuntimeException("You have already applied for this job");
        }

        // Check if closing date has passed
        if (job.getClosingDate() != null && job.getClosingDate().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("The application deadline has passed");
        }

        Application application = new Application();
        application.setJob(job);
        application.setApplicant(applicant);
        application.setCoverLetter(request.getCoverLetter());
        application.setStatus(ApplicationStatus.PENDING);
        application.setAppliedDate(LocalDateTime.now());

        return applicationRepository.save(application);
    }

    @Override
    public List<Application> findByApplicantId(Long applicantId) {
        return applicationRepository.findByApplicantUserId(applicantId);
    }

    @Override
    public List<Application> findByJobId(Long jobId) {
        List<Application> applications = applicationRepository.findByJobJobId(jobId);

        // Ensure relationships are loaded
        applications.forEach(app -> {
            if (app.getApplicant() != null) {
                app.getApplicant().getFullName(); // Force load
            }
            if (app.getJob() != null) {
                app.getJob().getTitle(); // Force load
            }
        });

        return applications;
    }

    @Override
    public Optional<Application> findById(Long applicationId) {
        return applicationRepository.findById(applicationId);
    }

    @Override
    public Application updateApplicationStatus(Long applicationId, ApplicationStatus status,
                                               String notes, Long employerId) {
        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new RuntimeException("Application not found"));

        // Verify that the employer owns this job
        if (!application.getJob().getEmployer().getUserId().equals(employerId)) {
            throw new RuntimeException("You are not authorized to update this application");
        }

        application.setStatus(status);
        application.setNotes(notes);
        application.setReviewedDate(LocalDateTime.now());

        return applicationRepository.save(application);
    }

    @Override
    public void withdrawApplication(Long applicationId, Long applicantId) {
        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new RuntimeException("Application not found"));

        // Verify that the applicant owns this application
        if (!application.getApplicant().getUserId().equals(applicantId)) {
            throw new RuntimeException("You are not authorized to withdraw this application");
        }

        // Only allow withdrawal if status is PENDING
        if (application.getStatus() != ApplicationStatus.PENDING) {
            throw new RuntimeException("You can only withdraw pending applications");
        }

        applicationRepository.delete(application);
    }

    @Override
    public boolean hasAlreadyApplied(Long jobId, Long applicantId) {
        return applicationRepository.existsByJobJobIdAndApplicantUserId(jobId, applicantId);
    }
}
