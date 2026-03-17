package com.jobsportal.service;

import com.jobsportal.entity.Application;
import com.jobsportal.entity.Job;
import com.jobsportal.entity.User;
import com.jobsportal.repository.ApplicationRepository;
import com.jobsportal.repository.JobRepository;
import com.jobsportal.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final JobRepository jobRepository;
    private final UserRepository userRepository;
    private final FileStorageService fileStorageService;

    // APPLY TO JOB
    public String applyToJob(Long jobId,
                             MultipartFile resume,
                             String jobSeekerEmail) {

        User jobSeeker = userRepository.findByEmail(jobSeekerEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));

        // Check if already applied
        if (applicationRepository.existsByJobIdAndJobSeekerId(
                job.getId(),
                jobSeeker.getId())) {

            throw new RuntimeException("You have already applied to this job");
        }

        String savedFileName = null;

        // Resume optional
        if (resume != null && !resume.isEmpty()) {
            savedFileName = fileStorageService.saveResume(resume);
        }

        Application application = Application.builder()
                .job(job)
                .jobSeeker(jobSeeker)
                .resumePath(savedFileName)
                .appliedAt(LocalDateTime.now())
                .build();

        applicationRepository.save(application);

        return "Application submitted successfully";
    }


    // GET MY APPLICATIONS
    public Page<Application> getMyApplications(
            String jobSeekerEmail,
            Pageable pageable
    ) {

        User jobSeeker = userRepository.findByEmail(jobSeekerEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return applicationRepository
                .findByJobSeekerId(jobSeeker.getId(), pageable);
    }


    // GET APPLICANTS FOR EMPLOYER
    public Page<Application> getApplicantsForJob(
            Long jobId,
            String employerEmail,
            Pageable pageable
    ) {

        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));

        if (!job.getEmployer().getEmail().equals(employerEmail)) {
            throw new RuntimeException("You are not allowed to view applicants for this job");
        }

        return applicationRepository.findByJobId(jobId, pageable);
    }
}