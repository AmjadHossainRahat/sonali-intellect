package com.example.authzsample.web;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/audit")
public class AuditApi {

    @GetMapping
    @PreAuthorize("hasPermission('AUDIT','VIEW')")
    public ResponseEntity<String> viewAudit() {
        return ResponseEntity.ok("audit-ok");
    }
}
