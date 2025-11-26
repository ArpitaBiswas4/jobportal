package com.arpita.jobportal.service;

import com.arpita.jobportal.dto.JobRequest;
import com.arpita.jobportal.model.Job;
import com.arpita.jobportal.model.Job.JobStatus;
import com.arpita.jobportal.model.Job.JobType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface JobService {
    Job createJob(JobRequest request, Long employerId);
    Job updateJob(Long jobId, JobRequest request, Long employerId);
    void deleteJob(Long jobId, Long employerId);
    Optional<Job> findById(Long jobId);
    List<Job> findByEmployerId(Long employerId);
    Page<Job> findAllActiveJobs(Pageable pageable);
    Page<Job> searchJobs(String keyword, String location, JobType jobType, Pageable pageable);
    Job updateJobStatus(Long jobId, JobStatus status, Long employerId);
}
