package com.orderprocessingeda.orderservice.client;

import com.orderprocessingeda.orderservice.exception.InventoryUnavailableException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class InventoryServiceWrapper {

    private InventoryClient inventoryClient;
    private static final Logger log = LoggerFactory.getLogger(InventoryServiceWrapper.class);

    public InventoryServiceWrapper(InventoryClient inventoryClient){
        this.inventoryClient = inventoryClient;
    }

    @CircuitBreaker(name = "inventoryservice", fallbackMethod = "inventoryFallBack")
    public boolean checkInventory(Long productId, Long quantity){
        log.info("Calling inventory service");
        return inventoryClient.checkStock(productId, quantity);
    }

    public boolean inventoryFallBack(Long productId, Long quantity, Throwable ex){
        log.error("Circuit breaker fallback triggered");
        throw new InventoryUnavailableException("Inventory Service Unavailable");
    }
}
