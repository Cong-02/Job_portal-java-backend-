package com.example.job_portal.Controllers;

import com.example.job_portal.DTOs.ChangePasswordRequest;
import com.example.job_portal.DTOs.LoginRequest;
import com.example.job_portal.DTOs.ProfileUpdateRequest;
import com.example.job_portal.DTOs.SignupRequest;
import com.example.job_portal.Entity.User;
import com.example.job_portal.SecurityConfiguration.JwtTokenProvider;
import com.example.job_portal.SecurityConfiguration.TokenBlacklist;
import com.example.job_portal.Services.UserService;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
//@CrossOrigin(origins = "*")
@Getter
@Setter
public class AuthController {

    private final UserService userService;
    private final JwtTokenProvider tokenProvider;
    private final AuthenticationManager authenticationManager;

    public AuthController(UserService userService, JwtTokenProvider tokenProvider, AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.tokenProvider = tokenProvider;
        this.authenticationManager = authenticationManager;
    }


    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signupRequest) {
        try {
            if (userService.existsByUsername(signupRequest.getUsername())) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("message", "Username đã tồn tại");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
            User user = userService.registerUser(signupRequest);
            Map<String, Object> userInfo = new HashMap<>();
            userInfo.put("id", user.getId());
            userInfo.put("UserName", user.getUsername());
            userInfo.put("Email", user.getEmail());
            userInfo.put("Phone", user.getPhone());
            userInfo.put("Role", user.getRole());

            Map<String, Object> responseRegister = new HashMap<>();
            responseRegister.put("Success", true);
            responseRegister.put("message", "Registered successfully!");
            responseRegister.put("user", userInfo);
            return ResponseEntity.status(HttpStatus.CREATED).body(responseRegister);
        } catch (RuntimeException e) {
            System.err.println("Error:" + e.getMessage());
            e.printStackTrace();

            Map<String, Object> responseExc = new HashMap<>();
            responseExc.put("Success", false);
            responseExc.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseExc);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@Valid @RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()));
        String token = tokenProvider.ganerateToken(authentication.getName());

        try {
            User userLogin = userService.login(loginRequest);
            Map<String, Object> userInfo = new HashMap<>();
            userInfo.put("id", userLogin.getId());
            userInfo.put("UserName", userLogin.getUsername());
            userInfo.put("Role", userLogin.getRole());

            Map<String, Object> responseLogin = new HashMap<>();
            responseLogin.put("Success", true);
            responseLogin.put("message", "Login successfully!");
            responseLogin.put("token", token);
            responseLogin.put("user", userInfo);
            return ResponseEntity.ok(responseLogin);
        } catch (RuntimeException e) {
            Map<String, Object> responseExc = new HashMap<>();
            responseExc.put("Success", false);
            responseExc.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(responseExc);
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader("Authorization") String header) {
        try {
            String token = header.substring(7);
            TokenBlacklist.tokens.add(token);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Đăng xuất thành công");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Lỗi khi đăng xuất");
            return ResponseEntity.badRequest().body(error);
        }
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminApi() {
        return "Admin Only";
    }

    @GetMapping("/common")
    @PreAuthorize("hasAnyRole('ADMIN','USER','HR')")
    public String userApi() {
        return "Admin or User or HR";
    }

    //    Phan trang
    @GetMapping("/page")
    public Page<User> getAllPage(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id"));
        return userService.getUserPage(pageable);
    }

    // lấy user info
    @GetMapping("/user")
    public ResponseEntity<?> getUSer(Authentication authentication) {
        return ResponseEntity.ok(userService.getCurrentUser());
    }

    @PutMapping("/profile")
    public ResponseEntity<?> updateProfile(@Valid @RequestBody ProfileUpdateRequest profileRequest) {
        try {
            User updatedUser = userService.updateProfile(profileRequest);
            return ResponseEntity.ok(updatedUser);
        } catch (RuntimeException e) {
            return ResponseEntity
                    .badRequest()
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping("/change-password")
    public ResponseEntity<?> changePassword(
            @RequestBody ChangePasswordRequest request,
            Authentication authentication
    ) {
        userService.changePassword(authentication.getName(), request);
        return ResponseEntity.ok("Đổi mật khẩu thành công");
    }

}
