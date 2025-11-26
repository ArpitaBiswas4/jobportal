package com.arpita.jobportal.controller.web;

import com.arpita.jobportal.dto.JobRequest;
import com.arpita.jobportal.model.Application;
import com.arpita.jobportal.model.Job;
import com.arpita.jobportal.model.Job.JobStatus;
import com.arpita.jobportal.model.User;
import com.arpita.jobportal.service.ApplicationService;
import com.arpita.jobportal.service.AuthService;
import com.arpita.jobportal.service.JobService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/employer")
@RequiredArgsConstructor
public class EmployerWebController {

    private final JobService jobService;
    private final ApplicationService applicationService;
    private final AuthService authService;

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        try {
            User user = authService.getCurrentUser();
            List<Job> jobs = jobService.findByEmployerId(user.getUserId());

            model.addAttribute("user", user);
            model.addAttribute("jobs", jobs);
            model.addAttribute("totalJobs", jobs.size());

            return "employer/dashboard";
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "Error loading dashboard: " + e.getMessage());
            return "redirect:/login?error=true";
        }
    }

    @GetMapping("/jobs/new")
    public String showCreateJobForm(Model model) {
        try {
            model.addAttribute("jobRequest", new JobRequest());
            model.addAttribute("jobTypes", Job.JobType.values());
            return "employer/job-form";
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/employer/dashboard?error";
        }
    }

    @PostMapping("/jobs/new")
    public String createJob(@Valid @ModelAttribute JobRequest request,
                            BindingResult result,
                            Model model) {

        if (result.hasErrors()) {
            model.addAttribute("jobTypes", Job.JobType.values());
            return "employer/job-form";
        }

        try {
            User user = authService.getCurrentUser();
            jobService.createJob(request, user.getUserId());
            return "redirect:/employer/dashboard?success";
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", e.getMessage());
            model.addAttribute("jobTypes", Job.JobType.values());
            return "employer/job-form";
        }
    }

    @GetMapping("/jobs/edit/{id}")
    public String showEditJobForm(@PathVariable Long id, Model model) {
        try {
            Job job = jobService.findById(id)
                    .orElseThrow(() -> new RuntimeException("Job not found"));

            JobRequest jobRequest = new JobRequest();
            jobRequest.setTitle(job.getTitle());
            jobRequest.setDescription(job.getDescription());
            jobRequest.setCompany(job.getCompany());
            jobRequest.setLocation(job.getLocation());
            jobRequest.setJobType(job.getJobType());
            jobRequest.setSalaryMin(job.getSalaryMin());
            jobRequest.setSalaryMax(job.getSalaryMax());
            jobRequest.setRequirements(job.getRequirements());
            jobRequest.setClosingDate(job.getClosingDate());

            model.addAttribute("jobRequest", jobRequest);
            model.addAttribute("jobId", id);
            model.addAttribute("jobTypes", Job.JobType.values());

            return "employer/job-form";
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/employer/dashboard?error";
        }
    }

    @PostMapping("/jobs/edit/{id}")
    public String updateJob(@PathVariable Long id,
                            @Valid @ModelAttribute JobRequest request,
                            BindingResult result,
                            Model model) {

        if (result.hasErrors()) {
            model.addAttribute("jobId", id);
            model.addAttribute("jobTypes", Job.JobType.values());
            return "employer/job-form";
        }

        try {
            User user = authService.getCurrentUser();
            jobService.updateJob(id, request, user.getUserId());
            return "redirect:/employer/dashboard?updated";
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", e.getMessage());
            model.addAttribute("jobId", id);
            model.addAttribute("jobTypes", Job.JobType.values());
            return "employer/job-form";
        }
    }

    @GetMapping("/jobs/{id}/applications")
    public String viewApplications(@PathVariable Long id, Model model) {
        System.out.println("=== VIEW APPLICATIONS DEBUG ===");
        System.out.println("Job ID: " + id);

        try {
            // Get the job
            Job job = jobService.findById(id)
                    .orElseThrow(() -> new RuntimeException("Job not found with ID: " + id));

            System.out.println("Job found: " + job.getTitle());

            // Get applications
            List<Application> applications = applicationService.findByJobId(id);
            System.out.println("Applications found: " + applications.size());

            // Calculate statistics
            long pendingCount = applications.stream()
                    .filter(app -> app.getStatus() == Application.ApplicationStatus.PENDING)
                    .count();

            long shortlistedCount = applications.stream()
                    .filter(app -> app.getStatus() == Application.ApplicationStatus.SHORTLISTED)
                    .count();

            long acceptedCount = applications.stream()
                    .filter(app -> app.getStatus() == Application.ApplicationStatus.ACCEPTED)
                    .count();

            System.out.println("Stats - Pending: " + pendingCount + ", Shortlisted: " + shortlistedCount + ", Accepted: " + acceptedCount);

            model.addAttribute("job", job);
            model.addAttribute("applications", applications);
            model.addAttribute("pendingCount", pendingCount);
            model.addAttribute("shortlistedCount", shortlistedCount);
            model.addAttribute("acceptedCount", acceptedCount);

            System.out.println("=== RETURNING TEMPLATE ===");
            return "employer/applications";

        } catch (Exception e) {
            System.err.println("=== ERROR IN VIEW APPLICATIONS ===");
            e.printStackTrace();
            model.addAttribute("error", "Error loading applications: " + e.getMessage());
            return "redirect:/employer/dashboard?error";
        }
    }

    @PostMapping("/jobs/delete/{id}")
    public String deleteJob(@PathVariable Long id) {
        try {
            User user = authService.getCurrentUser();
            jobService.deleteJob(id, user.getUserId());
            return "redirect:/employer/dashboard?deleted";
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/employer/dashboard?error";
        }
    }

    @PostMapping("/jobs/{id}/status")
    public String updateJobStatus(@PathVariable Long id,
                                  @RequestParam JobStatus status) {
        try {
            User user = authService.getCurrentUser();
            jobService.updateJobStatus(id, status, user.getUserId());
            return "redirect:/employer/dashboard?statusUpdated";
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/employer/dashboard?error";
        }
    }

    @PostMapping("/applications/{id}/update-status")
    public String updateApplicationStatus(@PathVariable Long id,
                                          @RequestParam String status,
                                          @RequestParam(required = false) String notes) {
        try {
            System.out.println("=== UPDATE APPLICATION STATUS ===");
            System.out.println("Application ID: " + id);
            System.out.println("New Status: " + status);
            System.out.println("Notes: " + notes);

            User user = authService.getCurrentUser();
            Application.ApplicationStatus newStatus = Application.ApplicationStatus.valueOf(status);

            Application application = applicationService.updateApplicationStatus(id, newStatus, notes, user.getUserId());

            System.out.println("Status updated successfully");

            // Redirect back to the applications page for this job
            return "redirect:/employer/jobs/" + application.getJob().getJobId() + "/applications?updated";

        } catch (Exception e) {
            System.err.println("=== ERROR UPDATING STATUS ===");
            e.printStackTrace();
            return "redirect:/employer/dashboard?error";
        }
    }
}