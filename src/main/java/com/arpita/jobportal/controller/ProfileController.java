package com.arpita.jobportal.controller;

import com.arpita.jobportal.model.ApplicantProfile;
import com.arpita.jobportal.service.ApplicantProfileService;
import com.arpita.jobportal.service.AuthService;
import com.arpita.jobportal.service.FileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/profiles")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@PreAuthorize("hasRole('APPLICANT')")
public class ProfileController {

    private final ApplicantProfileService profileService;
    private final FileStorageService fileStorageService;
    private final AuthService authService;

    // Get current user's profile
    @GetMapping("/me")
    public ResponseEntity<ApplicantProfile> getMyProfile() {
        Long userId = authService.getCurrentUser().getUserId();
        ApplicantProfile profile = profileService.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Profile not found"));

        return ResponseEntity.ok(profile);
    }

    // Create profile
    @PostMapping
    public ResponseEntity<ApplicantProfile> createProfile(@RequestBody ApplicantProfile profile) {
        Long userId = authService.getCurrentUser().getUserId();
        ApplicantProfile createdProfile = profileService.createProfile(profile, userId);

        return new ResponseEntity<>(createdProfile, HttpStatus.CREATED);
    }

    // Update profile
    @PutMapping
    public ResponseEntity<ApplicantProfile> updateProfile(@RequestBody ApplicantProfile profile) {
        Long userId = authService.getCurrentUser().getUserId();
        ApplicantProfile updatedProfile = profileService.updateProfile(profile, userId);

        return ResponseEntity.ok(updatedProfile);
    }

    // Upload resume
    @PostMapping("/upload-resume")
    public ResponseEntity<String> uploadResume(@RequestParam("file") MultipartFile file) {
        Long userId = authService.getCurrentUser().getUserId();

        // Validate file
        if (file.isEmpty()) {
            throw new RuntimeException("Please select a file to upload");
        }

        // Check file type
        String contentType = file.getContentType();
        if (contentType == null || !contentType.equals("application/pdf")) {
            throw new RuntimeException("Only PDF files are allowed");
        }

        // Store file
        String fileUrl = fileStorageService.storeFile(file, userId);

        // Update profile with resume URL
        ApplicantProfile profile = profileService.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Profile not found"));

        profile.setResumeUrl(fileUrl);
        profileService.updateProfile(profile, userId);

        return ResponseEntity.ok("Resume uploaded successfully: " + fileUrl);
    }

    // Delete profile
    @DeleteMapping
    public ResponseEntity<String> deleteProfile() {
        Long userId = authService.getCurrentUser().getUserId();
        profileService.deleteProfile(userId);

        return ResponseEntity.ok("Profile deleted successfully");
    }
}