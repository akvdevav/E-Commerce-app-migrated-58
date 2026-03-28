package com.techie.microservices.inventory.service;

import com.techie.microservices.inventory.dto.InventoryRequest;
import com.techie.microservices.inventory.dto.InventoryResponse;
import com.techie.microservices.inventory.exception.InsufficientStockException;
import com.techie.microservices.inventory.exception.ResourceNotFoundException;
import com.techie.microservices.inventory.model.Inventory;
import com.techie.microservices.inventory.repository.InventoryRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InventoryService {

    private final InventoryRepository inventoryRepository;

    public boolean isInStock(String skuCode, Integer quantity) {
        return inventoryRepository.existsBySkuCodeAndQuantityIsGreaterThanEqual(skuCode, quantity);
    }

    @Transactional
    public InventoryResponse upsertInventory(InventoryRequest request) {
        int updatedCount = inventoryRepository.increaseInventoryQuantity(
                request.skuCode(),
                request.quantity()
        );

        inventoryRepository.flush();

        if (updatedCount == 0) {
            Inventory newInventory = new Inventory();
            newInventory.setSkuCode(request.skuCode());
            newInventory.setQuantity(request.quantity());
            inventoryRepository.save(newInventory);
            return mapToResponse(newInventory);
        }

        return inventoryRepository.findBySkuCode(request.skuCode())
                .map(this::mapToResponse)
                .orElseThrow(() -> new IllegalStateException("Inventory not found after update"));
    }

    @Transactional
    public InventoryResponse decreaseInventory(InventoryRequest request) {
        int updatedCount = inventoryRepository.decreaseInventoryQuantity(
                request.skuCode(),
                request.quantity()
        );

        if (updatedCount == 0) {
            boolean exists = inventoryRepository.findBySkuCode(request.skuCode()).isPresent();
            if (!exists) {
                throw new ResourceNotFoundException("Inventory not found for skuCode: " + request.skuCode());
            }
            throw new InsufficientStockException("Insufficient stock for skuCode: " + request.skuCode());
        }

        return inventoryRepository.findBySkuCode(request.skuCode())
                .map(this::mapToResponse)
                .orElseThrow(() -> new IllegalStateException("Failed to retrieve updated inventory"));
    }

    public InventoryResponse getInventoryBySkuCode(String skuCode) {
        return inventoryRepository.findBySkuCode(skuCode)
                .map(this::mapToResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Inventory not found for skuCode: " + skuCode));
    }

    private InventoryResponse mapToResponse(Inventory inventory) {
        return new InventoryResponse(
                inventory.getId(),
                inventory.getSkuCode(),
                inventory.getQuantity()
        );
    }
}