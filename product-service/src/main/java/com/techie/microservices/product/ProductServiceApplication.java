package com.techie.microservices.product;

import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
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
@EnableRabbit
public class ProductServiceApplication {

    public static void main(String[] args) {
        // Default PostgreSQL connection settings
        String defaultUrl = "jdbc:postgresql://localhost:5432/postgres";
        String defaultUsername = "postgres";
        String defaultPassword = "postgres";

        // Set system properties so they have higher precedence than any external
        // configuration files (e.g., application.yml) that might refer to a
        // non‑existent database such as "productdb".
        System.setProperty("spring.datasource.url", defaultUrl);
        System.setProperty("spring.datasource.username", defaultUsername);
        System.setProperty("spring.datasource.password", defaultPassword);
        System.setProperty("spring.jpa.hibernate.ddl-auto", "update");
        System.setProperty("spring.jpa.show-sql", "true");
        System.setProperty("spring.jpa.properties.hibernate.dialect",
                "org.hibernate.dialect.PostgreSQLDialect");

        // Ensure the product service URL placeholder used by the API Gateway is resolved
        if (System.getProperty("product.service.url") == null && System.getenv("PRODUCT_SERVICE_URL") == null) {
            System.setProperty("product.service.url", "http://localhost:8082/api/product");
        }

        // Also keep default properties map for completeness (e.g., IDE runs)
        Map<String, Object> defaultProps = new HashMap<>();
        defaultProps.put("spring.datasource.url", defaultUrl);
        defaultProps.put("spring.datasource.username", defaultUsername);
        defaultProps.put("spring.datasource.password", defaultPassword);
        defaultProps.put("spring.jpa.hibernate.ddl-auto", "update");
        defaultProps.put("spring.jpa.show-sql", "true");
        defaultProps.put("spring.jpa.properties.hibernate.dialect",
                "org.hibernate.dialect.PostgreSQLDialect");

        SpringApplication app = new SpringApplication(ProductServiceApplication.class);
        app.setDefaultProperties(defaultProps);
        app.run(args);
    }
}