package com.programming.techie.api_gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;

/**
 * API Gateway application entry point.
 * <p>
 * Sets default service URLs if they are not provided externally to avoid
 * placeholder resolution failures during startup.
 */
@SpringBootApplication(exclude = MongoAutoConfiguration.class)
public class ApiGatewayApplication {

    public static void main(String[] args) {
        // Set default service URLs if they are not provided externally.
        // This prevents {@code IllegalArgumentException} caused by missing placeholders.
        if (System.getProperty("product.service.url") == null && System.getenv("PRODUCT_SERVICE_URL") == null) {
            System.setProperty("product.service.url", "http://localhost:8082/api/product");
        }
        if (System.getProperty("order.service.url") == null && System.getenv("ORDER_SERVICE_URL") == null) {
            System.setProperty("order.service.url", "http://localhost:8083/api/order");
        }
        SpringApplication.run(ApiGatewayApplication.class, args);
    }
}