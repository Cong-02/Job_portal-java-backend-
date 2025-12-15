package com.example.job_portal.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "users")
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(unique = true, nullable = false, length = 100)
    private String email;

    @Column(length = 20)
    private String phone;
    @Column
    private String role;
    @Column
    private String fullName;
    @Column
    private String address;
    @Column
    private String bio;

    @OneToMany(mappedBy = "hr")
    private List<JobPost> jobPost;
    @OneToMany(mappedBy = "user")
    private List<Comment>comments;

}
