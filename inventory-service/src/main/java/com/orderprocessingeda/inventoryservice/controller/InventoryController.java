package com.orderprocessingeda.inventoryservice.controller;

import com.orderprocessingeda.inventoryservice.entity.Inventory;
import com.orderprocessingeda.inventoryservice.repository.InventoryRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.orderprocessingeda.inventoryservice.service.InventoryService;

@RestController
@RequestMapping("/inventory")
public class InventoryController {

    private final InventoryService inventoryService;
    private final InventoryRepository inventoryRepository;

    public InventoryController(InventoryService inventoryService, InventoryRepository inventoryRepository) {
        this.inventoryService = inventoryService;
        this.inventoryRepository = inventoryRepository;
    }

//    @GetMapping("/check")
//    public boolean check(@RequestParam Long productId,
//                         @RequestParam Long quantity) {
//
//        return inventoryService.isAvailable(productId, quantity);
//    }

    @GetMapping("/stock")
    public Long getStock(@RequestParam Long productId){
        return inventoryRepository.findById(productId).
                map(Inventory :: getQuantity).orElse(0L);
    }
}
