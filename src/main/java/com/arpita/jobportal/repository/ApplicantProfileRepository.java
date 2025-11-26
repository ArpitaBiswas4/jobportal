package com.arpita.jobportal.repository;

import com.arpita.jobportal.model.ApplicantProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ApplicantProfileRepository extends JpaRepository<ApplicantProfile, Long> {
    Optional<ApplicantProfile> findByUserUserId(Long userId);
}