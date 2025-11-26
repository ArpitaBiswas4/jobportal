package com.arpita.jobportal.controller.web;

import com.arpita.jobportal.dto.ApplicationRequest;
import com.arpita.jobportal.model.Application;
import com.arpita.jobportal.model.ApplicantProfile;
import com.arpita.jobportal.model.Job;
import com.arpita.jobportal.model.User;
import com.arpita.jobportal.service.ApplicantProfileService;
import com.arpita.jobportal.service.ApplicationService;
import com.arpita.jobportal.service.AuthService;
import com.arpita.jobportal.service.JobService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
@RequestMapping("/applicant")
@RequiredArgsConstructor
public class ApplicantWebController {

    private final ApplicationService applicationService;
    private final ApplicantProfileService profileService;
    private final JobService jobService;
    private final AuthService authService;

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        User user = authService.getCurrentUser();
        List<Application> applications = applicationService.findByApplicantId(user.getUserId());

        model.addAttribute("user", user);
        model.addAttribute("applications", applications);
        model.addAttribute("totalApplications", applications.size());

        return "applicant/dashboard";
    }

    @GetMapping("/profile")
    public String profile(Model model) {
        User user = authService.getCurrentUser();
        ApplicantProfile profile = profileService.findByUserId(user.getUserId())
                .orElse(new ApplicantProfile());

        model.addAttribute("user", user);
        model.addAttribute("profile", profile);

        return "applicant/profile";
    }

    @PostMapping("/profile")
    public String updateProfile(@ModelAttribute ApplicantProfile profile,
                                @RequestParam(required = false) MultipartFile resume,
                                Model model) {

        try {
            User user = authService.getCurrentUser();

            // Check if profile exists
            if (profileService.findByUserId(user.getUserId()).isPresent()) {
                profileService.updateProfile(profile, user.getUserId());
            } else {
                profileService.createProfile(profile, user.getUserId());
            }

            model.addAttribute("success", "Profile updated successfully");
            return "redirect:/applicant/profile?success";

        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "applicant/profile";
        }
    }

    @PostMapping("/apply/{jobId}")
    public String applyForJob(@PathVariable Long jobId,
                              @RequestParam(required = false) String coverLetter,
                              Model model) {

        try {
            User user = authService.getCurrentUser();

            ApplicationRequest request = new ApplicationRequest();
            request.setJobId(jobId);
            request.setCoverLetter(coverLetter);

            applicationService.applyForJob(request, user.getUserId());

            return "redirect:/applicant/dashboard?applied";

        } catch (Exception e) {
            Job job = jobService.findById(jobId)
                    .orElseThrow(() -> new RuntimeException("Job not found"));
            model.addAttribute("job", job);
            model.addAttribute("error", e.getMessage());
            return "jobs/view";
        }
    }

    @PostMapping("/applications/{id}/withdraw")
    public String withdrawApplication(@PathVariable Long id) {
        User user = authService.getCurrentUser();
        applicationService.withdrawApplication(id, user.getUserId());
        return "redirect:/applicant/dashboard?withdrawn";
    }
}
