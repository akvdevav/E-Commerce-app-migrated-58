package com.techie.microservices.order.config;

import com.techie.microservices.order.client.InventoryClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

/**
 * Configuration for creating a type-safe {@link InventoryClient} using Spring's
 * {@link RestClient} and {@link HttpServiceProxyFactory}.
 *
 * Provides a default URL in case the {@code inventory.service.url} property is
 * not supplied, preventing placeholder resolution failures at startup.
 */
@Configuration
public class RestClientConfig {

    @Value("${inventory.service.url:http://localhost:8081/api/inventory}")
    private String inventoryServiceUrl;

    @Bean
    public InventoryClient inventoryClient() {
        RestClient restClient = RestClient.builder()
                .baseUrl(inventoryServiceUrl)
                .build();
        var restClientAdapter = RestClientAdapter.create(restClient);
        var httpServiceProxyFactory = HttpServiceProxyFactory.builderFor(restClientAdapter).build();
        return httpServiceProxyFactory.createClient(InventoryClient.class);
    }
}