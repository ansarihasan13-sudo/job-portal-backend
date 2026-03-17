package com.jobsportal.service;

import com.jobsportal.dto.EmployerDashboardResponse;
import com.jobsportal.entity.Job;
import com.jobsportal.entity.User;
import com.jobsportal.repository.ApplicationRepository;
import com.jobsportal.repository.JobRepository;
import com.jobsportal.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class JobService {

    private final JobRepository jobRepository;
    private final UserRepository userRepository;
    private final ApplicationRepository applicationRepository;


    public Job createJob(
            String title,
            String description,
            String location,
            Integer salary,
            MultipartFile logo,
            String employerEmail
    ) {

        User employer = userRepository.findByEmail(employerEmail)
                .orElseThrow(() -> new RuntimeException("Employer not found"));

        String logoPath = null;

        if (logo != null && !logo.isEmpty()) {

            try {

                String uploadDir = System.getProperty("user.dir") + "/uploads/logos/";
                File dir = new File(uploadDir);

                if (!dir.exists()) {
                    dir.mkdirs();
                }

                String fileName = System.currentTimeMillis() + "_" + logo.getOriginalFilename();

                File file = new File(uploadDir + fileName);

                logo.transferTo(file);


                logoPath = "uploads/logos/" + fileName;

            } catch (IOException e) {
                throw new RuntimeException("Error saving logo");
            }
        }

        Job job = Job.builder()
                .title(title)
                .description(description)
                .location(location)
                .salary(salary)
                .logoUrl(logoPath)
                .employer(employer)
                .createdAt(LocalDateTime.now())
                .build();

        return jobRepository.save(job);
    }






    public Page<Job> getEmployerJobs(String employerEmail, Pageable pageable) {

        User employer = userRepository.findByEmail(employerEmail)
                .orElseThrow(() -> new RuntimeException("Employer not found"));

        return jobRepository.findByEmployerId(employer.getId(), pageable);
    }


    public void deleteJob(Long jobId, String employerEmail) {

        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));

        if (!job.getEmployer().getEmail().equals(employerEmail)) {
            throw new RuntimeException("You cannot delete this job");
        }
        applicationRepository.deleteByJobId(jobId);

        jobRepository.delete(job);
    }


    public Job updateJob(Long id, Job updatedJob, String employerEmail) {

        Job job = jobRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Job not found"));

        if (!job.getEmployer().getEmail().equals(employerEmail)) {
            throw new RuntimeException("You cannot edit this job");
        }

        job.setTitle(updatedJob.getTitle());
        job.setDescription(updatedJob.getDescription());
        job.setLocation(updatedJob.getLocation());
        job.setSalary(updatedJob.getSalary());

        return jobRepository.save(job);
    }


    public Page<Job> getAllJobs(String keyword, String location, Pageable pageable) {
        return jobRepository.searchJobs(keyword, location, pageable);
    }


    public EmployerDashboardResponse getEmployerDashboard(String employerEmail) {

        User employer = userRepository.findByEmail(employerEmail)
                .orElseThrow(() -> new RuntimeException("Employer not found"));

        long totalJobs = jobRepository.countByEmployerId(employer.getId());
        long totalApplications = applicationRepository.countByJobEmployerId(employer.getId());

        return new EmployerDashboardResponse(totalJobs, totalApplications);
    }
}