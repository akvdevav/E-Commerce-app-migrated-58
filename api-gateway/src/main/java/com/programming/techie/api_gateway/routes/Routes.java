package com.programming.techie.api_gateway.routes;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.function.RequestPredicates;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.RouterFunctions;
import org.springframework.web.servlet.function.ServerResponse;

@Configuration
public class Routes {

    @Value("${product.service.url}")
    private String productServiceUrl;

    @Value("${order.service.url}")
    private String orderServiceUrl;

    @Value("${inventory.service.url}")
    private String inventoryServiceUrl;

    @Bean
    public RouterFunction<ServerResponse> productServiceRoute() {
        return RouterFunctions.route(
                RequestPredicates.path("/api/product"),
                HandlerFunctions.http(productServiceUrl)
        );
    }

    @Bean
    public RouterFunction<ServerResponse> productServiceSwaggerRoute() {
        return RouterFunctions.route(
                RequestPredicates.path("/aggregate/product-service/v3/api-docs"),
                HandlerFunctions.http(productServiceUrl)
        );
    }

    @Bean
    public RouterFunction<ServerResponse> orderServiceRoute() {
        return RouterFunctions.route(
                RequestPredicates.path("/api/order"),
                HandlerFunctions.http(orderServiceUrl)
        );
    }

    @Bean
    public RouterFunction<ServerResponse> orderServiceSwaggerRoute() {
        return RouterFunctions.route(
                RequestPredicates.path("/aggregate/order-service/v3/api-docs"),
                HandlerFunctions.http(orderServiceUrl)
        );
    }

    @Bean
    public RouterFunction<ServerResponse> inventoryServiceRoute() {
        return RouterFunctions.route(
                RequestPredicates.path("/api/inventory"),
                HandlerFunctions.http(inventoryServiceUrl)
        );
    }

    @Bean
    public RouterFunction<ServerResponse> fallbackRoute() {
        return RouterFunctions.route()
                .GET("/fallbackRoute", request -> ServerResponse.status(HttpStatus.SERVICE_UNAVAILABLE)
                        .body("Service Unavailable, please try again later"))
                .build();
    }
}