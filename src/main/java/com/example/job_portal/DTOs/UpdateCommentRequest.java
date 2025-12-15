package com.example.job_portal.DTOs;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UpdateCommentRequest {
    @Size(max = 1000,message = "Comment must not exceed 1000 characters")
    private String content;
}
