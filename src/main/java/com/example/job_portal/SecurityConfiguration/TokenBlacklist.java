package com.example.job_portal.SecurityConfiguration;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
@Component
public class TokenBlacklist {
    public static List<String> tokens = new ArrayList<>();
}
