package com.allan.powerduel.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Admin access is granted by email, via the ADMIN_EMAILS environment variable
 * (comma-separated) - not hardcoded, not stored on the Player row. This keeps
 * the admin list configurable per-environment without touching code or the
 * database, and without any account needing to bootstrap its own admin flag.
 */
@Component
public class AdminAccessService {

    private final Set<String> adminEmails;

    public AdminAccessService(@Value("${app.admin-emails:}") String adminEmailsCsv) {
        this.adminEmails = Arrays.stream(adminEmailsCsv.split(","))
                .map(String::trim)
                .map(String::toLowerCase)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toSet());
    }

    public boolean isAdmin(String email) {
        return email != null && adminEmails.contains(email.toLowerCase());
    }
}
