package com.example.job_portal.DTOs;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProfileUpdateRequest {
    @NotBlank(message = "Họ tên không được để trống")
    private String fullName;
    @Email(message = "Email không hợp lệ")
    private String email;
    private String phone;
    private String address;
    private String bio;
}
