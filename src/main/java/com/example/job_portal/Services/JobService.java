package com.example.job_portal.Services;

import com.example.job_portal.DTOs.JobFullDTO;
import com.example.job_portal.DTOs.JobRequest;
import com.example.job_portal.Entity.JobPost;
import com.example.job_portal.Entity.User;
import com.example.job_portal.Repositories.JobPostRepository;
import com.example.job_portal.Repositories.UserRepository;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class JobService {
    private final JobPostRepository jobRepo;
    private final UserRepository userRepo;

    public JobService(JobPostRepository jobRepo, UserRepository userRepo) {
        this.jobRepo = jobRepo;
        this.userRepo = userRepo;
    }

    // Lấy All Job
    public List<JobFullDTO> getAllJobsFull() {
        return jobRepo.findAll()
                .stream()
                .map(JobFullDTO::new)   // chuyển Entity → DTO
                .toList();
    }

    // Lấy Job theo id
    public JobFullDTO getJobFullById(Long id) {
        JobPost job = jobRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Job không tồn tại"));
        return new JobFullDTO(job);
    }


    // Tạo job(HR)
    public JobFullDTO createJob(Long hrId, JobRequest request) {
        User hr = userRepo.findById(hrId)
                .orElseThrow(() -> new RuntimeException("User không tồn tại"));

        JobPost job = new JobPost();
        job.setTitle(request.getTitle());
        job.setDescription(request.getDescription());
        job.setRequirements(request.getRequirements());
        job.setMinSalary(request.getMinSalary());
        job.setMaxSalary(request.getMaxSalary());
        job.setLocation(request.getLocation());
        job.setCompanyName(request.getCompanyName());
        job.setHr(hr);
        return new JobFullDTO(jobRepo.save(job));
    }

    // Cập nhật job
    public JobFullDTO updateJob(Long jobId, Long hrId, JobRequest request) {
        JobPost job = jobRepo.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job không tồn tại"));

        if (!job.getHr().getId().equals(hrId)) {
            throw new RuntimeException("Bạn không có quyền sửa job này");
        }

        job.setTitle(request.getTitle());
        job.setDescription(request.getDescription());
        job.setRequirements(request.getRequirements());
        job.setMinSalary(request.getMinSalary());
        job.setMaxSalary(request.getMaxSalary());
        job.setLocation(request.getLocation());
        job.setCompanyName(request.getCompanyName());

        return new JobFullDTO(jobRepo.save(job));
    }

    //Xóa job
    public void deleteJob(Long hrId) {
        JobPost job = jobRepo.findById(hrId)
                .orElseThrow(() -> new RuntimeException("Job không tồn tại"));
        if (!job.getId().equals(hrId)) {
            throw new RuntimeException("Bạn không có quyền xóa job này");
        }
        jobRepo.delete(job);
    }

    //Search Job
    public List<JobFullDTO> loginSearch(
            String keyword,
            String location,
            Integer minSalary,
            Integer maxSalary,
            String companyName,
            Pageable pageable
    ) {
        Specification<JobPost> spec =
                loginSearchSpec(
                        keyword, location, minSalary, maxSalary, companyName
                );

        Page<JobPost> jobPostEntityPage = jobRepo.findAll(spec, pageable);
        Page<JobFullDTO> dtoPage = jobPostEntityPage.map(JobFullDTO::new);
        return dtoPage.getContent();
    }

    public static Specification<JobPost> loginSearchSpec(
            String keyword,
            String location,
            Integer minSalary,
            Integer maxSalary,
            String companyName
    ) {
        return (root, query, cb) -> {

            List<Predicate> predicates = new ArrayList<>();

            // keyword: title OR description
            if (keyword != null && !keyword.isBlank()) {
                String likeKeyword = "%" + keyword.toLowerCase() + "%";

                Predicate titleLike =
                        cb.like(cb.lower(root.get("title")), likeKeyword);

                Predicate descriptionLike =
                        cb.like(cb.lower(root.get("description")), likeKeyword);

                predicates.add(cb.or(titleLike, descriptionLike));
            }

            // location
            if (location != null && !location.isBlank()) {
                predicates.add(
                        cb.like(
                                cb.lower(root.get("location")),
                                "%" + location.toLowerCase() + "%"
                        )
                );
            }

            // companyName
            if (companyName != null && !companyName.isBlank()) {
                predicates.add(
                        cb.like(
                                cb.lower(root.get("companyName")),
                                "%" + companyName.toLowerCase() + "%"
                        )
                );
            }
           // minSalary
            if (minSalary != null) {
                predicates.add(
                        cb.or(
                                cb.isNull(root.get("minSalary")),
                                cb.greaterThanOrEqualTo(root.get("minSalary"), minSalary)
                        )
                );
            }

           // maxSalary
            if (maxSalary != null) {
                predicates.add(
                        cb.or(
                                cb.isNull(root.get("maxSalary")),
                                cb.lessThanOrEqualTo(root.get("maxSalary"), maxSalary)
                        )
                );
            }

            // ORDER BY createdAt DESC
            query.orderBy(cb.desc(root.get("createdAt")));

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
