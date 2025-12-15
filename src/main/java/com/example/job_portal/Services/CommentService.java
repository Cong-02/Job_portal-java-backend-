package com.example.job_portal.Services;

import com.example.job_portal.DTOs.CreateCommentRequest;
import com.example.job_portal.Repositories.CommentRepository;
import com.example.job_portal.Repositories.JobPostRepository;
import com.example.job_portal.Repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {
    private final CommentRepository commentRepo;
    private final JobPostRepository jobPostRepo;
    private final UserRepository userRepo;

    public CommentService(CommentRepository commentRepo, JobPostRepository jobPostRepo, UserRepository userRepo) {
        this.commentRepo = commentRepo;
        this.jobPostRepo = jobPostRepo;
        this.userRepo = userRepo;
    }

//    public List getComment(CreateCommentRequest commentRequest){
//        List comment= commentRepo.findByUserId();
//    }
}
