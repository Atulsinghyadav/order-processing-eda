package com.orderprocessingeda.inventoryservice.service;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class InventoryService {

    private static final Logger log = LoggerFactory.getLogger(InventoryService.class);
    private final Map<Long, Long> stock = new HashMap<>();

    @PostConstruct
    void init() {
        stock.put(101L, 10L);
        stock.put(102L, 5L);
        stock.put(103L, 0L);
    }

    public void reduceStock(Long productId, Long quantity){

        Long currentStock = stock.getOrDefault(productId, 0L);

        if(currentStock < quantity){
            log.warn("Stock inconsistency for product {}", productId);
            return;
        }

        stock.put(productId, currentStock - quantity);
        log.info("Stock updated: productId={}, remaining={}", productId, stock.get(productId));

    }

    public boolean isAvailable(Long productId, Long quantity) {
        Long available = stock.getOrDefault(productId, 0L);
        return quantity != null && quantity > 0 && available >= quantity;
    }
}
