package com.arpita.jobportal.repository;

import com.arpita.jobportal.model.Job;
import com.arpita.jobportal.model.Job.JobStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobRepository extends JpaRepository<Job, Long> {

    List<Job> findByEmployerUserId(Long employerId);

    Page<Job> findByStatus(JobStatus status, Pageable pageable);

    @Query("SELECT j FROM Job j WHERE j.status = :status AND " +
            "(LOWER(j.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(j.description) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    Page<Job> searchJobs(@Param("status") JobStatus status,
                         @Param("keyword") String keyword,
                         Pageable pageable);

    @Query("SELECT j FROM Job j WHERE j.status = 'ACTIVE' AND " +
            "(:keyword IS NULL OR LOWER(j.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(j.description) LIKE LOWER(CONCAT('%', :keyword, '%'))) AND " +
            "(:location IS NULL OR LOWER(j.location) LIKE LOWER(CONCAT('%', :location, '%'))) AND " +
            "(:jobType IS NULL OR j.jobType = :jobType)")
    Page<Job> findWithFilters(@Param("keyword") String keyword,
                              @Param("location") String location,
                              @Param("jobType") Job.JobType jobType,
                              Pageable pageable);
}