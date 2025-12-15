package com.example.job_portal.Services;

import com.example.job_portal.DTOs.ChangePasswordRequest;
import com.example.job_portal.DTOs.LoginRequest;
import com.example.job_portal.DTOs.ProfileUpdateRequest;
import com.example.job_portal.DTOs.SignupRequest;
import com.example.job_portal.Entity.User;
import com.example.job_portal.Repositories.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // Register
    public User registerUser(SignupRequest signupRequest) {
        User user = new User();
        user.setUsername(signupRequest.getUsername());
        user.setPassword(passwordEncoder.encode(signupRequest.getPassword()));
        user.setEmail(signupRequest.getEmail());
        user.setRole(signupRequest.getRoles().toString());
        user.setPhone(signupRequest.getPhone());
        return userRepository.save(user);
    }

    public boolean existsByUsername(String username) {

        return userRepository.existsByUsername(username);
    }

    //Login
    public User login(LoginRequest loginRequest) {
        Optional<User> optionalUser = userRepository.findByUsername(loginRequest.getUsername());
        if (optionalUser.isEmpty()) {
            throw new RuntimeException("User not found");
        }
        User user = optionalUser.get();
        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new RuntimeException("Incorrect password");
        }
        return user;
    }

    // Page
    public Page<User> getUserPage(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    // Lấy userInfo
    public User getCurrentUser() {
        String username = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User không tồn tại"));
    }

    // Update profile
    public User updateProfile(ProfileUpdateRequest updateRequest) {
        String username = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();
        User update = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        update.setFullName(updateRequest.getFullName());
        update.setAddress(updateRequest.getAddress());
        update.setEmail(updateRequest.getEmail());
        update.setPhone(updateRequest.getPhone());
        update.setBio(updateRequest.getBio());
        return userRepository.save(update);
    }

    // Change password
    public void changePassword(String username, ChangePasswordRequest request) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User không tồn tại"));

        // Check old password
        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new RuntimeException("Mật khẩu cũ không đúng");
        }
        // Encode & save
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }
}
