package com.team7.spliito_server;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;

@ActiveProfiles("test")
@SpringBootTest
public abstract class IntegrationTestSupport {
    private static final String MYSQL_VERSION = "mysql:latest";

    private static final MySQLContainer<?> mysql;

    static {
        mysql = new MySQLContainer<>(MYSQL_VERSION);
        mysql.start();
    }

    @DynamicPropertySource
    public static void dynamicConfiguration(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mysql::getJdbcUrl);
        registry.add("spring.datasource.username", mysql::getUsername);
        registry.add("spring.datasource.password", mysql::getPassword);
    }
}