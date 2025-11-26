package com.arpita.jobportal.dto;

import com.arpita.jobportal.model.Application.ApplicationStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ApplicationResponse {
    private Long applicationId;
    private Long jobId;
    private String jobTitle;
    private String company;
    private Long applicantId;
    private String applicantName;
    private String applicantEmail;
    private String coverLetter;
    private ApplicationStatus status;
    private LocalDateTime appliedDate;
    private LocalDateTime reviewedDate;
    private String notes;
}