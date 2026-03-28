package com.programming.techie.api_gateway.routes;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.function.RequestPredicates;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.RouterFunctions;
import org.springframework.web.servlet.function.ServerResponse;

import java.net.URI;

@Configuration
public class Routes {

    @Value("${product.service.url:http://localhost:8082/api/product}")
    private String productServiceUrl;

    @Value("${order.service.url:http://localhost:8083/api/order}")
    private String orderServiceUrl;

    @Value("${inventory.service.url:http://localhost:8081/api/inventory}")
    private String inventoryServiceUrl;

    private RouterFunction<ServerResponse> redirectRoute(String path, String targetUrl) {
        return RouterFunctions.route(
                RequestPredicates.path(path),
                request -> ServerResponse.temporaryRedirect(URI.create(targetUrl)).build()
        );
    }

    @Bean
    public RouterFunction<ServerResponse> productServiceRoute() {
        return redirectRoute("/api/product", productServiceUrl);
    }

    @Bean
    public RouterFunction<ServerResponse> productServiceSwaggerRoute() {
        return redirectRoute("/aggregate/product-service/v3/api-docs", productServiceUrl);
    }

    @Bean
    public RouterFunction<ServerResponse> orderServiceRoute() {
        return redirectRoute("/api/order", orderServiceUrl);
    }

    @Bean
    public RouterFunction<ServerResponse> orderServiceSwaggerRoute() {
        return redirectRoute("/aggregate/order-service/v3/api-docs", orderServiceUrl);
    }

    @Bean
    public RouterFunction<ServerResponse> inventoryServiceRoute() {
        return redirectRoute("/api/inventory", inventoryServiceUrl);
    }

    @Bean
    public RouterFunction<ServerResponse> fallbackRoute() {
        return RouterFunctions.route()
                .GET("/fallbackRoute", request -> ServerResponse.status(HttpStatus.SERVICE_UNAVAILABLE)
                        .body("Service Unavailable, please try again later"))
                .build();
    }
}