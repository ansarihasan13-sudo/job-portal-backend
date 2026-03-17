package com.jobsportal.controller;

import com.jobsportal.dto.ApiResponse;
import com.jobsportal.dto.EmployerDashboardResponse;
import com.jobsportal.entity.Application;
import com.jobsportal.entity.Job;
import com.jobsportal.service.ApplicationService;
import com.jobsportal.service.JobService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/employer")
@RequiredArgsConstructor
public class EmployerController {

    private final ApplicationService applicationService;
    private final JobService jobService;


    // CREATE JOB WITH LOGO

    @PostMapping(value = "/jobs", consumes = {"multipart/form-data"})
    public ResponseEntity<ApiResponse<Job>> createJob(

            @RequestParam String title,
            @RequestParam String description,
            @RequestParam String location,
            @RequestParam Integer salary,

            @RequestParam(required = false) MultipartFile logo,

            Authentication authentication
    ) {

        Job job = jobService.createJob(
                title,
                description,
                location,
                salary,
                logo,
                authentication.getName()
        );

        return ResponseEntity.ok(
                new ApiResponse<>(
                        LocalDateTime.now(),
                        HttpStatus.OK.value(),
                        "Job created successfully",
                        job
                )
        );
    }


    // UPDATE JOB

    @PutMapping("/jobs/{id}")
    public ResponseEntity<ApiResponse<Job>> updateJob(
            @PathVariable Long id,
            @RequestBody Job updatedJob,
            Authentication authentication) {

        Job job = jobService.updateJob(
                id,
                updatedJob,
                authentication.getName()
        );

        return ResponseEntity.ok(
                new ApiResponse<>(
                        LocalDateTime.now(),
                        HttpStatus.OK.value(),
                        "Job updated successfully",
                        job
                )
        );
    }


    // GET EMPLOYER JOBS

    @GetMapping("/jobs")
    public ResponseEntity<ApiResponse<Page<Job>>> getMyJobs(
            Authentication authentication,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {

        Page<Job> jobs = jobService.getEmployerJobs(
                authentication.getName(),
                PageRequest.of(page, size)
        );

        return ResponseEntity.ok(
                new ApiResponse<>(
                        LocalDateTime.now(),
                        HttpStatus.OK.value(),
                        "Employer jobs fetched successfully",
                        jobs
                )
        );
    }


    // DELETE JOB

    @DeleteMapping("/jobs/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteJob(
            @PathVariable Long id,
            Authentication authentication) {

        jobService.deleteJob(id, authentication.getName());

        return ResponseEntity.ok(
                new ApiResponse<>(
                        LocalDateTime.now(),
                        HttpStatus.OK.value(),
                        "Job deleted successfully",
                        null
                )
        );
    }


    // VIEW APPLICANTS

    @GetMapping("/jobs/{jobId}/applicants")
    public ResponseEntity<ApiResponse<Page<Application>>> getApplicants(
            @PathVariable Long jobId,
            Authentication authentication,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {

        Page<Application> applicants =
                applicationService.getApplicantsForJob(
                        jobId,
                        authentication.getName(),
                        PageRequest.of(page, size)
                );

        return ResponseEntity.ok(
                new ApiResponse<>(
                        LocalDateTime.now(),
                        HttpStatus.OK.value(),
                        "Applicants fetched successfully",
                        applicants
                )
        );
    }

    // EMPLOYER DASHBOARD
    @GetMapping("/dashboard")
    public ResponseEntity<ApiResponse<EmployerDashboardResponse>> getDashboard(
            Authentication authentication) {

        EmployerDashboardResponse dashboard =
                jobService.getEmployerDashboard(authentication.getName());

        return ResponseEntity.ok(
                new ApiResponse<>(
                        LocalDateTime.now(),
                        HttpStatus.OK.value(),
                        "Dashboard data fetched successfully",
                        dashboard
                )
        );
    }
}