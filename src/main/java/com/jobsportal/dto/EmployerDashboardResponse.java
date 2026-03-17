package com.jobsportal.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EmployerDashboardResponse {

    private long totalJobs;
    private long totalApplications;
}
