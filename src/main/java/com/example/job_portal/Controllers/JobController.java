package com.example.job_portal.Controllers;

import com.example.job_portal.DTOs.JobFullDTO;
import com.example.job_portal.DTOs.JobRequest;
import com.example.job_portal.Services.JobService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/jobs")
public class JobController {
    private final JobService jobService;

    public JobController(JobService jobService) {
        this.jobService = jobService;
    }

    //search job
    @GetMapping("/search")
    public ResponseEntity<List<JobFullDTO>> searchJobsFull(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) Integer minSalary,
            @RequestParam(required = false) Integer maxSalary,
            @RequestParam(required = false) String companyName,
            @RequestParam(required = true) int page,
            @RequestParam(required = true) int size
    ) {

        Pageable pageable = PageRequest.of(
                page, size, Sort.by("createdAt").descending());
        List<JobFullDTO> jobs = jobService.loginSearch(
                keyword, location, minSalary, maxSalary, companyName, pageable);

        return ResponseEntity.ok(jobs);
    }

    // Lấy All Job
    @GetMapping("/all")
    public ResponseEntity<List<JobFullDTO>> getAllJobsFull() {
        return ResponseEntity.ok(jobService.getAllJobsFull());
    }

    @GetMapping("/getjob/{id}")
    public ResponseEntity<JobFullDTO> getJobById(@RequestParam Long id) {
        return ResponseEntity.ok(jobService.getJobFullById(id));
    }

    //    Tạo job mới (HR)
    @PostMapping("/hr")
    public ResponseEntity<?> createJob(@RequestBody JobRequest request) {
        try {
            Long userId = request.getHrId();
            JobFullDTO job = jobService.createJob(userId, request);
            return ResponseEntity.ok(job);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // Tạo update theo id
    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateJob(
            @PathVariable Long id,
            @RequestBody JobRequest request) {
        try {
            Long userId = request.getHrId();
            JobFullDTO job = jobService.updateJob(id, userId, request);
            return ResponseEntity.ok(job);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // Delete job
    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteJob(@RequestParam Long hrId) {
        try {
            jobService.deleteJob(hrId);
            return ResponseEntity.ok(Map.of("message", "Xóa job thành công"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

}
