package com.example;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LivenessController {

    @GetMapping("/health")
    public ResponseEntity getHealth() {
        return ResponseEntity.ok("java-mysql-app is UP");
    }
}
