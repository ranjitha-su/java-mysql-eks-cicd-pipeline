package com.example;

import com.mysql.cj.jdbc.MysqlDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;


@Configuration
public class DatabaseConfig {

    private String user = System.getenv("DB_USER");
        private String password = System.getenv("DB_PWD");
    private String serverName = System.getenv("DB_SERVER"); // db host name, like localhost without the port
    private String dbName = System.getenv("DB_NAME");

    @Bean
    public DataSource dataSource() {
        MysqlDataSource ds = new MysqlDataSource();
        ds.setUser(user);
        ds.setPassword(password);
        ds.setServerName(serverName);
        ds.setDatabaseName(dbName);
        return ds;
    }
}
