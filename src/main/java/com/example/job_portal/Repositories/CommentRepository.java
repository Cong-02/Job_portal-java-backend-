package com.example.job_portal.Repositories;

import com.example.job_portal.Entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment,Long> {
    List<Comment> findByJobPostId(Long jobPostId);
    List<Comment> findByUserId(Long userId);
}
