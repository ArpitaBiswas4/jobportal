package com.arpita.jobportal.controller;

import com.arpita.jobportal.dto.ApplicationRequest;
import com.arpita.jobportal.dto.ApplicationResponse;
import com.arpita.jobportal.model.Application;
import com.arpita.jobportal.model.Application.ApplicationStatus;
import com.arpita.jobportal.service.ApplicationService;
import com.arpita.jobportal.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/applications")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ApplicationController {

    private final ApplicationService applicationService;
    private final AuthService authService;

    // Applicant only - Apply for a job
    @PostMapping
    @PreAuthorize("hasRole('APPLICANT')")
    public ResponseEntity<ApplicationResponse> applyForJob(@Valid @RequestBody ApplicationRequest request) {
        Long applicantId = authService.getCurrentUser().getUserId();
        Application application = applicationService.applyForJob(request, applicantId);

        return new ResponseEntity<>(convertToResponse(application), HttpStatus.CREATED);
    }

    // Applicant only - Get my applications
    @GetMapping("/my-applications")
    @PreAuthorize("hasRole('APPLICANT')")
    public ResponseEntity<List<ApplicationResponse>> getMyApplications() {
        Long applicantId = authService.getCurrentUser().getUserId();
        List<Application> applications = applicationService.findByApplicantId(applicantId);

        List<ApplicationResponse> responses = applications.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(responses);
    }

    // Employer only - Get applications for a job
    @GetMapping("/job/{jobId}")
    @PreAuthorize("hasRole('EMPLOYER')")
    public ResponseEntity<List<ApplicationResponse>> getApplicationsForJob(@PathVariable Long jobId) {
        List<Application> applications = applicationService.findByJobId(jobId);

        List<ApplicationResponse> responses = applications.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(responses);
    }

    // Get application by ID (Applicant sees own, Employer sees for their jobs)
    @GetMapping("/{id}")
    public ResponseEntity<ApplicationResponse> getApplicationById(@PathVariable Long id) {
        Application application = applicationService.findById(id)
                .orElseThrow(() -> new RuntimeException("Application not found with id: " + id));

        return ResponseEntity.ok(convertToResponse(application));
    }

    // Employer only - Update application status
    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('EMPLOYER')")
    public ResponseEntity<ApplicationResponse> updateApplicationStatus(
            @PathVariable Long id,
            @RequestBody Map<String, String> request) {

        Long employerId = authService.getCurrentUser().getUserId();
        ApplicationStatus status = ApplicationStatus.valueOf(request.get("status"));
        String notes = request.get("notes");

        Application application = applicationService.updateApplicationStatus(id, status, notes, employerId);

        return ResponseEntity.ok(convertToResponse(application));
    }

    // Applicant only - Withdraw application
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('APPLICANT')")
    public ResponseEntity<String> withdrawApplication(@PathVariable Long id) {
        Long applicantId = authService.getCurrentUser().getUserId();
        applicationService.withdrawApplication(id, applicantId);

        return ResponseEntity.ok("Application withdrawn successfully");
    }

    // Helper method to convert Application to ApplicationResponse
    private ApplicationResponse convertToResponse(Application application) {
        ApplicationResponse response = new ApplicationResponse();
        response.setApplicationId(application.getApplicationId());
        response.setJobId(application.getJob().getJobId());
        response.setJobTitle(application.getJob().getTitle());
        response.setCompany(application.getJob().getCompany());
        response.setApplicantId(application.getApplicant().getUserId());
        response.setApplicantName(application.getApplicant().getFullName());
        response.setApplicantEmail(application.getApplicant().getEmail());
        response.setCoverLetter(application.getCoverLetter());
        response.setStatus(application.getStatus());
        response.setAppliedDate(application.getAppliedDate());
        response.setReviewedDate(application.getReviewedDate());
        response.setNotes(application.getNotes());

        return response;
    }
}