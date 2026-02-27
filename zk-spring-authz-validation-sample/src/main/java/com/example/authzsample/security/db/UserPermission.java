package com.example.authzsample.security.db;

import jakarta.persistence.*;

@Entity
@Table(name = "user_permission")
public class UserPermission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 64)
    private String username;

    @Column(nullable = false, length = 64)
    private String resource;

    @Column(nullable = false, length = 64)
    private String action;

    protected UserPermission() {}

    public UserPermission(String username, String resource, String action) {
        this.username = username;
        this.resource = resource;
        this.action = action;
    }

    public Long getId() { return id; }
    public String getUsername() { return username; }
    public String getResource() { return resource; }
    public String getAction() { return action; }
}
