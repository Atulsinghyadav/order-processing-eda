package com.orderprocessingeda.inventoryservice.service;

import com.orderprocessingeda.inventoryservice.entity.Inventory;
import com.orderprocessingeda.inventoryservice.repository.InventoryRepository;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class InventoryService {

    private static final Logger log = LoggerFactory.getLogger(InventoryService.class);
    private final InventoryRepository inventoryRepository;

    public InventoryService(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }

    @Transactional
    public void reduceStock(Long productId, Long quantity){

        Inventory inventory = inventoryRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        if(inventory.getQuantity() < quantity){
            log.warn("Insufficient stock for product {}", productId);
            return;
        }

        inventory.setQuantity(inventory.getQuantity() - quantity);
        inventoryRepository.save(inventory);

        log.info("Stock updated for product {} → remaining {}", productId, inventory.getQuantity());
    }

}