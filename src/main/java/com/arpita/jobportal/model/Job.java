package com.arpita.jobportal.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "jobs")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long jobId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employer_id", nullable = false)
    private User employer;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String company;

    private String location;

    @Enumerated(EnumType.STRING)
    private JobType jobType;

    @Column(precision = 10, scale = 2)
    private BigDecimal salaryMin;

    @Column(precision = 10, scale = 2)
    private BigDecimal salaryMax;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private JobStatus status = JobStatus.ACTIVE;

    private LocalDateTime postedDate;

    private LocalDateTime closingDate;

    @Column(columnDefinition = "TEXT")
    private String requirements;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public enum JobType {
        FULL_TIME, PART_TIME, CONTRACT, INTERNSHIP
    }

    public enum JobStatus {
        ACTIVE, CLOSED, DRAFT
    }

    @PrePersist
    protected void onCreate() {
        if (postedDate == null) {
            postedDate = LocalDateTime.now();
        }
    }
}