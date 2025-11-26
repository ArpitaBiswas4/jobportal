package com.arpita.jobportal.service;

import com.arpita.jobportal.model.ApplicantProfile;

import java.util.Optional;

public interface ApplicantProfileService {
    ApplicantProfile createProfile(ApplicantProfile profile, Long userId);
    ApplicantProfile updateProfile(ApplicantProfile profile, Long userId);
    Optional<ApplicantProfile> findByUserId(Long userId);
    void deleteProfile(Long userId);
}