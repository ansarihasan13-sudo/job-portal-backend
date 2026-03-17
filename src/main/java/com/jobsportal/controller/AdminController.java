package com.jobsportal.controller;

import com.jobsportal.entity.Job;
import com.jobsportal.entity.User;
import com.jobsportal.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    // View all users
    @GetMapping("/users")
    public Page<User> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {
        return adminService.getAllUsers(
                PageRequest.of(page, size)
        );
    }

    // Delete user
    @DeleteMapping("/users/{id}")
    public String deleteUser(@PathVariable Long id) {

        adminService.deleteUser(id);
        return "User deleted successfully";
    }

    // View all jobs
    @GetMapping("/jobs")
    public Page<Job> getAllJobs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {
        return adminService.getAllJobs(
                PageRequest.of(page, size)
        );
    }

    // Delete job
    @DeleteMapping("/jobs/{id}")
    public String deleteJob(@PathVariable Long id) {

        adminService.deleteJob(id);
        return "Job deleted successfully";
    }
}