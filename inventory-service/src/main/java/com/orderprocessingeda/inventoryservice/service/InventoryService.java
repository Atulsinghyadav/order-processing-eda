package com.orderprocessingeda.inventoryservice.service;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class InventoryService {

    private final Map<Long, Integer> stock = new HashMap<>();

    @PostConstruct
    void init() {
        stock.put(101L, 10);
        stock.put(102L, 5);
        stock.put(103L, 0);
    }

    public boolean isAvailable(Long productId, Long quantity) {
        Integer available = stock.getOrDefault(productId, 0);
        return quantity != null && quantity > 0 && available >= quantity;
    }
}
