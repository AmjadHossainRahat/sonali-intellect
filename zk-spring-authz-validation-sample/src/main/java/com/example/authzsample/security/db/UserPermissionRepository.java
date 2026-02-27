package com.example.authzsample.security.db;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserPermissionRepository extends JpaRepository<UserPermission, Long> {
    boolean existsByUsernameAndResourceAndAction(String username, String resource, String action);
}
