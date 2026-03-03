package com.example;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@RestController
public class AppController {

    private final DataSource dataSource;

    public AppController(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @GetMapping("/get-data")
    public ResponseEntity<List<User>> getData() {
        List<User> users = fetchDataFromDB();
        return ResponseEntity.ok(users);
    }

    @PostMapping("/update-roles")
    public ResponseEntity<List<User>> updateRoles(@RequestBody ArrayList<User> users) {
        updateDatabase(users);
        return ResponseEntity.ok(users);
    }

    private void updateDatabase(ArrayList<User> users) {
        String sql = "UPDATE students SET Major=? WHERE Name=?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            for (User user : users) {
                stmt.setString(1, user.getRole());
                stmt.setString(2, user.getName());
                stmt.executeUpdate();
            }

        } catch (SQLException e) {
            throw new RuntimeException("Database update failed", e);
        }
    }

    private List<User> fetchDataFromDB() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT Name, Major FROM students";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                User user = new User();
                user.setName(rs.getString("Name"));
                user.setRole(rs.getString("Major"));
                users.add(user);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Database fetch failed", e);
        }

        return users;
    }

    private static class User {
        private String name;
        private String role;

        public User() {}

        public User(String name, String role) {
            this.name = name;
            this.role = role;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setRole(String role) {
            this.role = role;
        }

        public String getName() {
            return name;
        }

        public String getRole() {
            return role;
        }
    }
}
