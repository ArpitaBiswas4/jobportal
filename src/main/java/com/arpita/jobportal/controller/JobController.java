package com.arpita.jobportal.controller;

import com.arpita.jobportal.dto.JobRequest;
import com.arpita.jobportal.dto.JobResponse;
import com.arpita.jobportal.model.Job;
import com.arpita.jobportal.model.Job.JobStatus;
import com.arpita.jobportal.model.Job.JobType;
import com.arpita.jobportal.service.AuthService;
import com.arpita.jobportal.service.JobService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/jobs")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class JobController {

    private final JobService jobService;
    private final AuthService authService;

    // Public - Get all active jobs with filters
    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllJobs(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) JobType jobType,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "postedDate,desc") String[] sort) {

        Sort.Direction direction = sort[1].equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sort[0]));

        Page<Job> jobPage = jobService.searchJobs(keyword, location, jobType, pageable);

        List<JobResponse> jobs = jobPage.getContent().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());

        Map<String, Object> response = new HashMap<>();
        response.put("content", jobs);
        response.put("currentPage", jobPage.getNumber());
        response.put("totalItems", jobPage.getTotalElements());
        response.put("totalPages", jobPage.getTotalPages());

        return ResponseEntity.ok(response);
    }

    // Public - Get job by ID
    @GetMapping("/{id}")
    public ResponseEntity<JobResponse> getJobById(@PathVariable Long id) {
        Job job = jobService.findById(id)
                .orElseThrow(() -> new RuntimeException("Job not found with id: " + id));

        return ResponseEntity.ok(convertToResponse(job));
    }

    // Employer only - Create new job
    @PostMapping
    @PreAuthorize("hasRole('EMPLOYER')")
    public ResponseEntity<JobResponse> createJob(@Valid @RequestBody JobRequest request) {
        Long employerId = authService.getCurrentUser().getUserId();
        Job job = jobService.createJob(request, employerId);

        return new ResponseEntity<>(convertToResponse(job), HttpStatus.CREATED);
    }

    // Employer only - Update job
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('EMPLOYER')")
    public ResponseEntity<JobResponse> updateJob(
            @PathVariable Long id,
            @Valid @RequestBody JobRequest request) {

        Long employerId = authService.getCurrentUser().getUserId();
        Job job = jobService.updateJob(id, request, employerId);

        return ResponseEntity.ok(convertToResponse(job));
    }

    // Employer only - Delete job
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('EMPLOYER')")
    public ResponseEntity<String> deleteJob(@PathVariable Long id) {
        Long employerId = authService.getCurrentUser().getUserId();
        jobService.deleteJob(id, employerId);

        return ResponseEntity.ok("Job deleted successfully");
    }

    // Employer only - Get employer's jobs
    @GetMapping("/employer/my-jobs")
    @PreAuthorize("hasRole('EMPLOYER')")
    public ResponseEntity<List<JobResponse>> getMyJobs() {
        Long employerId = authService.getCurrentUser().getUserId();
        List<Job> jobs = jobService.findByEmployerId(employerId);

        List<JobResponse> responses = jobs.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(responses);
    }

    // Employer only - Update job status
    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('EMPLOYER')")
    public ResponseEntity<JobResponse> updateJobStatus(
            @PathVariable Long id,
            @RequestParam JobStatus status) {

        Long employerId = authService.getCurrentUser().getUserId();
        Job job = jobService.updateJobStatus(id, status, employerId);

        return ResponseEntity.ok(convertToResponse(job));
    }

    // Helper method to convert Job to JobResponse
    private JobResponse convertToResponse(Job job) {
        JobResponse response = new JobResponse();
        response.setJobId(job.getJobId());
        response.setTitle(job.getTitle());
        response.setDescription(job.getDescription());
        response.setCompany(job.getCompany());
        response.setLocation(job.getLocation());
        response.setJobType(job.getJobType());
        response.setSalaryMin(job.getSalaryMin());
        response.setSalaryMax(job.getSalaryMax());
        response.setStatus(job.getStatus());
        response.setPostedDate(job.getPostedDate());
        response.setClosingDate(job.getClosingDate());
        response.setRequirements(job.getRequirements());
        response.setEmployerId(job.getEmployer().getUserId());
        response.setEmployerName(job.getEmployer().getFullName());

        return response;
    }
}