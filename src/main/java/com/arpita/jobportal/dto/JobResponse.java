package com.arpita.jobportal.dto;

import com.arpita.jobportal.model.Job.JobStatus;
import com.arpita.jobportal.model.Job.JobType;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class JobResponse {
    private Long jobId;
    private String title;
    private String description;
    private String company;
    private String location;
    private JobType jobType;
    private BigDecimal salaryMin;
    private BigDecimal salaryMax;
    private JobStatus status;
    private LocalDateTime postedDate;
    private LocalDateTime closingDate;
    private String requirements;
    private Long employerId;
    private String employerName;
}
