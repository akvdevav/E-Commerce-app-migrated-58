package com.techie.microservices.product;

import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;

/**
 * Product Service entry point.
 *
 * <p>
 * Provides default PostgreSQL connection properties and ensures that the
 * {@code product.service.url} placeholder required by the API Gateway is set
 * when the application is started without external configuration.
 * </p>
 */
@SpringBootApplication
@EnableFeignClients
@EnableDiscoveryClient
@EnableRabbit
public class ProductServiceApplication {

    public static void main(String[] args) {
        // Default PostgreSQL connection settings
        Map<String, Object> defaultProps = new HashMap<>();
        defaultProps.put("spring.datasource.url", "jdbc:postgresql://localhost:5432/postgres");
        defaultProps.put("spring.datasource.username", "postgres");
        defaultProps.put("spring.datasource.password", "postgres");
        defaultProps.put("spring.jpa.hibernate.ddl-auto", "update");
        defaultProps.put("spring.jpa.show-sql", "true");
        defaultProps.put("spring.jpa.properties.hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");

        // Ensure the product service URL placeholder used by the API Gateway is resolved
        if (System.getProperty("product.service.url") == null && System.getenv("PRODUCT_SERVICE_URL") == null) {
            System.setProperty("product.service.url", "http://localhost:8082/api/product");
        }

        SpringApplication app = new SpringApplication(ProductServiceApplication.class);
        app.setDefaultProperties(defaultProps);
        app.run(args);
    }
}