package com.example;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.sql.Connection;

@RestController
public class ReadinessController {

    private final DataSource dataSource;

    public ReadinessController(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @GetMapping("/ready")
    public String readinessCheck() {
        try (Connection conn = dataSource.getConnection()) {
            if (conn.isValid(2)) {
                return "java-mysql-app is READY";
            } else {
                throw new RuntimeException("DB not valid");
            }
        } catch (Exception e) {
            throw new RuntimeException("DB not reachable", e);
        }
    }
}
