package com.arpita.jobportal.service.impl;

import com.arpita.jobportal.dto.JobRequest;
import com.arpita.jobportal.model.Job;
import com.arpita.jobportal.model.Job.JobStatus;
import com.arpita.jobportal.model.Job.JobType;
import com.arpita.jobportal.model.User;
import com.arpita.jobportal.repository.JobRepository;
import com.arpita.jobportal.repository.UserRepository;
import com.arpita.jobportal.service.JobService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class JobServiceImpl implements JobService {

    private final JobRepository jobRepository;
    private final UserRepository userRepository;

    @Override
    public Job createJob(JobRequest request, Long employerId) {
        User employer = userRepository.findById(employerId)
                .orElseThrow(() -> new RuntimeException("Employer not found"));

        if (employer.getRole() != User.Role.EMPLOYER) {
            throw new RuntimeException("Only employers can post jobs");
        }

        Job job = new Job();
        job.setEmployer(employer);
        job.setTitle(request.getTitle());
        job.setDescription(request.getDescription());
        job.setCompany(request.getCompany());
        job.setLocation(request.getLocation());
        job.setJobType(request.getJobType());
        job.setSalaryMin(request.getSalaryMin());
        job.setSalaryMax(request.getSalaryMax());
        job.setRequirements(request.getRequirements());
        job.setClosingDate(request.getClosingDate());
        job.setStatus(JobStatus.ACTIVE);
        job.setPostedDate(LocalDateTime.now());

        return jobRepository.save(job);
    }

    @Override
    public Job updateJob(Long jobId, JobRequest request, Long employerId) {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));

        // Check if the user is the owner of the job
        if (!job.getEmployer().getUserId().equals(employerId)) {
            throw new RuntimeException("You are not authorized to update this job");
        }

        job.setTitle(request.getTitle());
        job.setDescription(request.getDescription());
        job.setCompany(request.getCompany());
        job.setLocation(request.getLocation());
        job.setJobType(request.getJobType());
        job.setSalaryMin(request.getSalaryMin());
        job.setSalaryMax(request.getSalaryMax());
        job.setRequirements(request.getRequirements());
        job.setClosingDate(request.getClosingDate());

        return jobRepository.save(job);
    }

    @Override
    public void deleteJob(Long jobId, Long employerId) {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));

        if (!job.getEmployer().getUserId().equals(employerId)) {
            throw new RuntimeException("You are not authorized to delete this job");
        }

        jobRepository.delete(job);
    }

    @Override
    public Optional<Job> findById(Long jobId) {
        return jobRepository.findById(jobId);
    }

    @Override
    public List<Job> findByEmployerId(Long employerId) {
        return jobRepository.findByEmployerUserId(employerId);
    }

    @Override
    public Page<Job> findAllActiveJobs(Pageable pageable) {
        return jobRepository.findByStatus(JobStatus.ACTIVE, pageable);
    }

    @Override
    public Page<Job> searchJobs(String keyword, String location, JobType jobType, Pageable pageable) {
        return jobRepository.findWithFilters(keyword, location, jobType, pageable);
    }

    @Override
    public Job updateJobStatus(Long jobId, JobStatus status, Long employerId) {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));

        if (!job.getEmployer().getUserId().equals(employerId)) {
            throw new RuntimeException("You are not authorized to update this job");
        }

        job.setStatus(status);
        return jobRepository.save(job);
    }
}