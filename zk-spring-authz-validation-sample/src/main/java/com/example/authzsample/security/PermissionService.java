package com.example.authzsample.security;

import com.example.authzsample.security.db.UserPermissionRepository;
import org.springframework.stereotype.Service;

@Service
public class PermissionService {

    private final UserPermissionRepository repo;

    public PermissionService(UserPermissionRepository repo) {
        this.repo = repo;
    }

    public boolean hasPermission(String username, String resource, String action) {
        return repo.existsByUsernameAndResourceAndAction(username, resource, action);
    }
}
