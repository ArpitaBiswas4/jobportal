package com.arpita.jobportal.repository;

import com.arpita.jobportal.model.Application;
import com.arpita.jobportal.model.Application.ApplicationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Long> {

    List<Application> findByApplicantUserId(Long applicantId);

    List<Application> findByJobJobId(Long jobId);

    Optional<Application> findByJobJobIdAndApplicantUserId(Long jobId, Long applicantId);

    boolean existsByJobJobIdAndApplicantUserId(Long jobId, Long applicantId);

    long countByJobJobIdAndStatus(Long jobId, ApplicationStatus status);
}