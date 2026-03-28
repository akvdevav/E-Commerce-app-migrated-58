package com.techie.microservices.product.client;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "inventory-service")
public interface InventoryFeignClient {

}