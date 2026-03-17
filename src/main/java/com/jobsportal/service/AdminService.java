package com.jobsportal.service;

import com.jobsportal.entity.Job;
import com.jobsportal.entity.User;
import com.jobsportal.repository.JobRepository;
import com.jobsportal.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;
    private final JobRepository jobRepository;

    public Page<User> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    public void deleteUser(Long userId) {

        if (!userRepository.existsById(userId)) {
            throw new RuntimeException("User not found");
        }

        userRepository.deleteById(userId);
    }

    public Page<Job> getAllJobs(Pageable pageable) {
        return jobRepository.findAll(pageable);
    }

    public void deleteJob(Long jobId) {

        if (!jobRepository.existsById(jobId)) {
            throw new RuntimeException("Job not found");
        }

        jobRepository.deleteById(jobId);
    }
}