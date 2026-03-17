package com.jobsportal.repository;

import com.jobsportal.entity.Application;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface ApplicationRepository extends JpaRepository<Application, Long> {

  @Transactional
  @Modifying
  @Query("DELETE FROM Application a WHERE a.job.id = :jobId")
  void deleteByJobId(@Param("jobId") Long jobId);


  boolean existsByJobIdAndJobSeekerId(Long jobId, Long jobSeekerId);

  Page<Application> findByJobSeekerId(Long jobSeekerId, Pageable pageable);

  Page<Application> findByJobId(Long jobId, Pageable pageable);

  long countByJobEmployerId(Long employerId);
}