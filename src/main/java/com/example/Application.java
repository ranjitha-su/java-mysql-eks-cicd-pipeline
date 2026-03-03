package com.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

@SpringBootApplication
public class Application {

    public static void main(String[] args)
    {
        SpringApplication.run(Application.class, args);
    }

    @PostConstruct
    public void init()
    {
        Logger log = LoggerFactory.getLogger(Application.class);
        log.info("Java app started");
    }

    private void createTable(Statement stmt) throws SQLException {
        String sqlStatement = "CREATE TABLE students (\n" +
                "    StudentID INT PRIMARY KEY AUTO_INCREMENT,\n" +
                "    Name VARCHAR(100) NOT NULL,\n" +
                "    Major VARCHAR(255) NOT NULL\n" +
                ");";
        stmt.executeUpdate(sqlStatement);
    }

    private void generateData(Statement stmt) throws SQLException {
        String sqlQuery = "SELECT StudentID, Name, Major FROM students";
        ResultSet resultSet = stmt.executeQuery(sqlQuery);
        if (!resultSet.next()) {
            String sqlStatement = "INSERT INTO Students (StudentID, Name, Major) VALUES\n" +
                    "('John Simon', 'biology')," +
                    "('Jane Marie', 'arts')," +
                    "('Peter Alex', 'computer-science');";
            stmt.executeUpdate(sqlStatement);
        }
    }

    public String getStatus() {
        return "OK";
    }

    public boolean getCondition(boolean condition) {
        return condition;
    }
}
