package com.jobsportal.repository;

import com.jobsportal.entity.Job;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface JobRepository extends JpaRepository<Job, Long> {

    Page<Job> findByEmployerId(Long employerId, Pageable pageable);

    long countByEmployerId(Long employerId);

    @Query("""
        SELECT j FROM Job j
        WHERE (:keyword IS NULL OR 
               LOWER(j.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
               LOWER(j.description) LIKE LOWER(CONCAT('%', :keyword, '%')))
        AND (:location IS NULL OR 
               LOWER(j.location) LIKE LOWER(CONCAT('%', :location, '%')))
    """)
    Page<Job> searchJobs(
            @Param("keyword") String keyword,
            @Param("location") String location,
            Pageable pageable
    );
}