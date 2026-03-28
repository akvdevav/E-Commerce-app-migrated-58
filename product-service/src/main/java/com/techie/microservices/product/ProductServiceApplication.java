package com.techie.microservices.product;

import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;

@SpringBootApplication
@EnableFeignClients
@EnableDiscoveryClient
@EnableRabbit
public class ProductServiceApplication {

    public static void main(String[] args) {
        // Set default PostgreSQL connection properties if they are not supplied via external configuration
        Map<String, Object> defaultProps = new HashMap<>();
        defaultProps.put("spring.datasource.url", "jdbc:postgresql://localhost:5432/postgres");
        defaultProps.put("spring.datasource.username", "postgres");
        defaultProps.put("spring.datasource.password", "postgres");
        defaultProps.put("spring.jpa.hibernate.ddl-auto", "update");
        defaultProps.put("spring.jpa.show-sql", "true");
        defaultProps.put("spring.jpa.properties.hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");

        SpringApplication app = new SpringApplication(ProductServiceApplication.class);
        app.setDefaultProperties(defaultProps);
        app.run(args);
    }
}