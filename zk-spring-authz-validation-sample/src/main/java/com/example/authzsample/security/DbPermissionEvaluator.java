package com.example.authzsample.security;

import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
public class DbPermissionEvaluator implements PermissionEvaluator {

    private final PermissionService permissionService;

    public DbPermissionEvaluator(PermissionService permissionService) {
        this.permissionService = permissionService;
    }

    @Override
    public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
        if (authentication == null || !authentication.isAuthenticated()) return false;
        if (targetDomainObject == null || permission == null) return false;

        String username = authentication.getName();
        String resource = String.valueOf(targetDomainObject);
        String action = String.valueOf(permission);
        return permissionService.hasPermission(username, resource, action);
    }

    @Override
    public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permission) {
        // Not used in this sample.
        return false;
    }
}
