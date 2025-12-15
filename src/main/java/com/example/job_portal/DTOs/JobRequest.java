package com.example.job_portal.DTOs;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class JobRequest {
    private String title;
    private String description;
    private String requirements;
    private Integer minSalary;
    private Integer maxSalary;
    private String location;
    private String companyName;
    private Long hrId;
}
