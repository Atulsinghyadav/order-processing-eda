package com.orderprocessingeda.orderservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "inventory-service", url = "http://localhost:8082")
public interface InventoryClient {

    @GetMapping("/inventory/check")
    boolean checkStock(@RequestParam Long productId,
                       @RequestParam Long quantity);
}
