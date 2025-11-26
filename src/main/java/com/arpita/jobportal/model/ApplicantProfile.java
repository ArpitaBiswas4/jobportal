package com.arpita.jobportal.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "applicant_profiles")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApplicantProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long profileId;

    @OneToOne
    @JoinColumn(name = "user_id", unique = true, nullable = false)
    private User user;

    private String resumeUrl;

    @Column(columnDefinition = "TEXT")
    private String skills;

    private Integer experience; // in years

    private String education;

    private String linkedin;

    private String portfolio;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
