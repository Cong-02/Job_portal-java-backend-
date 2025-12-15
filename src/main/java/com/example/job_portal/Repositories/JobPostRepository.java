package com.example.job_portal.Repositories;

import com.example.job_portal.Entity.JobPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JobPostRepository extends JpaRepository<JobPost, Long>,
        JpaSpecificationExecutor<JobPost> {
    Optional<JobPost> findById(Long hrId);

}
