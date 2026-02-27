package com.example.authzsample.zk;

import com.example.authzsample.security.PermissionService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component("authorizationGuard")
public class AuthorizationGuard {

    private final PermissionService permissionService;

    public AuthorizationGuard(PermissionService permissionService) {
        this.permissionService = permissionService;
    }

    public boolean can(String resource, String action) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) return false;
        return permissionService.hasPermission(auth.getName(), resource, action);
    }
}
