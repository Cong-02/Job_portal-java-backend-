package com.example.job_portal.DTOs;

import com.example.job_portal.Entity.JobPost;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor

public class JobPublicDTO {
    private Long id;
    private String title;
    private String location;
    private Integer minSalary;
    private Integer maxSalary;
    private String companyName;

    public JobPublicDTO(JobPost job) {
        this.id = job.getId();
        this.title = job.getTitle();
        this.location = job.getLocation();
        this.minSalary = job.getMinSalary();
        this.maxSalary = job.getMaxSalary();
        this.companyName = job.getCompanyName();
    }
}
