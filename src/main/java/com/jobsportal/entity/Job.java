package com.jobsportal.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "jobs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String logoUrl;
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String location;

    private Integer salary;

    @ManyToOne
    @JoinColumn(name = "employer_id")
    private User employer;

    private LocalDateTime createdAt;
}