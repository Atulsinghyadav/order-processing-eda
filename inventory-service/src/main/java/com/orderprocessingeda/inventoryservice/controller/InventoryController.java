package com.orderprocessingeda.inventoryservice.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.orderprocessingeda.inventoryservice.service.InventoryService;

@RestController
@RequestMapping("/inventory")
public class InventoryController {

    private final InventoryService inventoryService;


    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @GetMapping("/check")
    public boolean check(@RequestParam Long productId,
                         @RequestParam Long quantity) {

        return inventoryService.isAvailable(productId, quantity);
    }
}
