package com.example.atm.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.Map;

@RestController
public class PingController {

    @GetMapping(value = "/api/ping", produces = "application/json")
    public Map<String, Object> ping() {
        return Map.of(
                "status", "OK",
                "timestamp", Instant.now().toString(),
                "note", "Spring Framework (non-Boot) app is running. Multi-port TCP listeners are started from RootContext."
        );
    }
}
