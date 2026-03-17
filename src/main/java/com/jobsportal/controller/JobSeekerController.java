package com.jobsportal.controller;

import com.jobsportal.entity.Application;
import com.jobsportal.entity.Job;
import com.jobsportal.service.ApplicationService;
import com.jobsportal.service.JobService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/jobseeker")
@RequiredArgsConstructor
public class JobSeekerController {

    private final JobService jobService;
    private final ApplicationService applicationService;

    // View all jobs + search
    @GetMapping("/jobs")
    public Page<Job> getAllJobs(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String location,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size
    ) {
        return jobService.getAllJobs(
                keyword,
                location,
                PageRequest.of(page, size)
        );
    }

    // Apply to job (resume optional)
    @PostMapping(value = "/apply/{jobId}", consumes = "multipart/form-data")
    public String applyToJob(
            @PathVariable Long jobId,
            @RequestParam(value = "resume", required = false) MultipartFile resume,
            Authentication authentication
    ) {
        return applicationService.applyToJob(
                jobId,
                resume,
                authentication.getName()
        );
    }

    // View applied jobs
    @GetMapping("/applications")
    public Page<Application> getMyApplications(
            Authentication authentication,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {
        return applicationService.getMyApplications(
                authentication.getName(),
                PageRequest.of(page, size)
        );
    }
}