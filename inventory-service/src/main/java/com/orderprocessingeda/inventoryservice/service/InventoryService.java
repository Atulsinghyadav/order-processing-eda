package com.orderprocessingeda.inventoryservice.service;

import com.orderprocessingeda.inventoryservice.entity.Inventory;
import com.orderprocessingeda.inventoryservice.producer.InventoryEventProducer;
import com.orderprocessingeda.inventoryservice.repository.InventoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class InventoryService {

    private static final Logger log = LoggerFactory.getLogger(InventoryService.class);
    private final InventoryRepository inventoryRepository;
    private final InventoryEventProducer inventoryEventProducer;

    public InventoryService(InventoryRepository inventoryRepository, InventoryEventProducer inventoryEventProducer) {
        this.inventoryRepository = inventoryRepository;
        this.inventoryEventProducer = inventoryEventProducer;
    }

    @Transactional
    public void reduceStock(Long productId, Long quantity, Long orderId){

        Inventory inventory = inventoryRepository.findById(productId)
                .orElse(null);

        if(inventory == null){
            inventoryEventProducer.publishStockFailed(orderId, "Product not found in inventory");
            return;
        }
        if(inventory.getQuantity() < quantity){
            inventoryEventProducer.publishStockFailed(
                    orderId,
                    "Insufficient stock"
            );
            return;
        }

        inventory.setQuantity(inventory.getQuantity() - quantity);
        inventoryRepository.save(inventory);
        log.info("Stock updated for product {} → remaining {}", productId, inventory.getQuantity());
        inventoryEventProducer.publishStockReserved(orderId);
        log.info("Published StockReservedEvent for orderId={}", orderId);
    }

}