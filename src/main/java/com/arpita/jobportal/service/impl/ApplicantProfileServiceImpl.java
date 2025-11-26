package com.arpita.jobportal.service.impl;

import com.arpita.jobportal.model.ApplicantProfile;
import com.arpita.jobportal.model.User;
import com.arpita.jobportal.repository.ApplicantProfileRepository;
import com.arpita.jobportal.repository.UserRepository;
import com.arpita.jobportal.service.ApplicantProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class ApplicantProfileServiceImpl implements ApplicantProfileService {

    private final ApplicantProfileRepository profileRepository;
    private final UserRepository userRepository;

    @Override
    public ApplicantProfile createProfile(ApplicantProfile profile, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getRole() != User.Role.APPLICANT) {
            throw new RuntimeException("Only applicants can create profiles");
        }

        // Check if profile already exists
        if (profileRepository.findByUserUserId(userId).isPresent()) {
            throw new RuntimeException("Profile already exists for this user");
        }

        profile.setUser(user);
        return profileRepository.save(profile);
    }

    @Override
    public ApplicantProfile updateProfile(ApplicantProfile updatedProfile, Long userId) {
        ApplicantProfile profile = profileRepository.findByUserUserId(userId)
                .orElseThrow(() -> new RuntimeException("Profile not found"));

        profile.setSkills(updatedProfile.getSkills());
        profile.setExperience(updatedProfile.getExperience());
        profile.setEducation(updatedProfile.getEducation());
        profile.setLinkedin(updatedProfile.getLinkedin());
        profile.setPortfolio(updatedProfile.getPortfolio());

        if (updatedProfile.getResumeUrl() != null) {
            profile.setResumeUrl(updatedProfile.getResumeUrl());
        }

        return profileRepository.save(profile);
    }

    @Override
    public Optional<ApplicantProfile> findByUserId(Long userId) {
        return profileRepository.findByUserUserId(userId);
    }

    @Override
    public void deleteProfile(Long userId) {
        ApplicantProfile profile = profileRepository.findByUserUserId(userId)
                .orElseThrow(() -> new RuntimeException("Profile not found"));

        profileRepository.delete(profile);
    }
}
