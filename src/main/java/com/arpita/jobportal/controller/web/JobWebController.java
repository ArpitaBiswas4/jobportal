package com.arpita.jobportal.controller.web;

import com.arpita.jobportal.model.Job;
import com.arpita.jobportal.model.Job.JobStatus;
import com.arpita.jobportal.model.Job.JobType;
import com.arpita.jobportal.service.JobService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/jobs")
@RequiredArgsConstructor
public class JobWebController {

    private final JobService jobService;

    @GetMapping
    public String listJobs(@RequestParam(required = false) String keyword,
                           @RequestParam(required = false) String location,
                           @RequestParam(required = false) JobType jobType,
                           @RequestParam(defaultValue = "0") int page,
                           @RequestParam(defaultValue = "10") int size,
                           Model model) {

        try {
            Pageable pageable = PageRequest.of(page, size, Sort.by("postedDate").descending());

            // Get only active jobs for public view
            Page<Job> jobPage;
            if (keyword != null || location != null || jobType != null) {
                jobPage = jobService.searchJobs(keyword, location, jobType, pageable);
            } else {
                jobPage = jobService.findAllActiveJobs(pageable);
            }

            model.addAttribute("jobs", jobPage.getContent());
            model.addAttribute("currentPage", page);
            model.addAttribute("totalPages", jobPage.getTotalPages());
            model.addAttribute("totalItems", jobPage.getTotalElements());
            model.addAttribute("keyword", keyword);
            model.addAttribute("location", location);
            model.addAttribute("jobType", jobType);
            model.addAttribute("jobTypes", JobType.values());

            return "jobs/list";
        } catch (Exception e) {
            System.err.println("Error loading jobs list: " + e.getMessage());
            e.printStackTrace();
            model.addAttribute("error", "Error loading jobs: " + e.getMessage());
            model.addAttribute("jobs", java.util.Collections.emptyList());
            model.addAttribute("totalItems", 0);
            model.addAttribute("totalPages", 0);
            model.addAttribute("currentPage", 0);
            model.addAttribute("jobTypes", JobType.values());
            return "jobs/list";
        }
    }

    @GetMapping("/view/{id}")
    public String viewJob(@PathVariable Long id, Model model) {
        try {
            Job job = jobService.findById(id)
                    .orElseThrow(() -> new RuntimeException("Job not found with ID: " + id));

            model.addAttribute("job", job);
            return "jobs/view";
        } catch (Exception e) {
            System.err.println("Error loading job: " + e.getMessage());
            e.printStackTrace();
            model.addAttribute("error", "Job not found");
            return "redirect:/jobs";
        }
    }
}