package com.example.job_portal.DTOs;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class JobSearchRequest {
    private String keyword;
    private String location;
    private Integer minSalary;
    private Integer maxSalary;
}
