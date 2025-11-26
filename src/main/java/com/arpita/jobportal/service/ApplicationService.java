package com.arpita.jobportal.service;

import com.arpita.jobportal.dto.ApplicationRequest;
import com.arpita.jobportal.model.Application;
import com.arpita.jobportal.model.Application.ApplicationStatus;

import java.util.List;
import java.util.Optional;

public interface ApplicationService {
    Application applyForJob(ApplicationRequest request, Long applicantId);
    List<Application> findByApplicantId(Long applicantId);
    List<Application> findByJobId(Long jobId);
    Optional<Application> findById(Long applicationId);
    Application updateApplicationStatus(Long applicationId, ApplicationStatus status, String notes, Long employerId);
    void withdrawApplication(Long applicationId, Long applicantId);
    boolean hasAlreadyApplied(Long jobId, Long applicantId);
}