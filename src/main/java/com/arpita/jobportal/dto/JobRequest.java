package com.arpita.jobportal.dto;

import com.arpita.jobportal.model.Job.JobType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class JobRequest {
    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Description is required")
    private String description;

    @NotBlank(message = "Company is required")
    private String company;

    @NotBlank(message = "Location is required")
    private String location;

    @NotNull(message = "Job type is required")
    private JobType jobType;

    private BigDecimal salaryMin;
    private BigDecimal salaryMax;
    private String requirements;
    private LocalDateTime closingDate;
}
