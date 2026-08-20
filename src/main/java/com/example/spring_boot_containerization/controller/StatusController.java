package com.example.spring_boot_containerization.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Simple demo/status endpoint - handy for quickly confirming that the app
 * is up and reachable (e.g. right after a Kubernetes deployment) without
 * needing any database connectivity.
 */
@RestController
@RequestMapping("/api/status")
public class StatusController {

    @GetMapping
    public ResponseEntity<String> getStatus() {
        return ResponseEntity.ok("Kudos!! You've successfully deployed your spring boot application to kubernetes");
    }
}

