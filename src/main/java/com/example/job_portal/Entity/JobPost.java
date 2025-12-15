package com.example.job_portal.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "job_posts")
public class JobPost {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String title;
    @Column
    private String location;
    @Column(name = "min_salary")
    private Integer minSalary;

    @Column(name = "max_salary")
    private Integer maxSalary;
    @Column(name = "company_name")
    private String companyName;
    @Column(columnDefinition = "TEXT")
    private String description;
    @Column(columnDefinition = "TEXT")
    private String requirements;
    @Column
    private String status;
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "hr_id")
    private User hr;
    @OneToMany(mappedBy = "jobPost")
    private List<Comment>comments;

    public String getSalaryRange() {
        if (minSalary != null && maxSalary != null) {
            return minSalary + "-" + maxSalary + " triệu";
        } else if (minSalary != null) {
            return "Từ " + minSalary + " triệu";
        } else if (maxSalary != null) {
            return "Tối đa " + maxSalary + " triệu";
        }
        return "Thỏa thuận";
    }
}