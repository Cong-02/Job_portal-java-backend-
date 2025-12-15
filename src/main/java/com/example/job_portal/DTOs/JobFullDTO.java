package com.example.job_portal.DTOs;

import com.example.job_portal.Entity.JobPost;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class JobFullDTO {
    private Long id;
    private String title;
    private String location;
    private Integer minSalary;
    private Integer maxSalary;
    private String companyName;
    private String description;
    private String requirements;
    private Long hrId;

    public JobFullDTO(JobPost job) {
        this.id = job.getId();
        this.title = job.getTitle();
        this.location = job.getLocation();
        this.minSalary = job.getMinSalary();
        this.maxSalary = job.getMaxSalary();
        this.companyName = job.getCompanyName();
        this.description = job.getDescription();
        this.requirements = job.getRequirements();
        this.hrId = job.getHr().getId();
    }
}
