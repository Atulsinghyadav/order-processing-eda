package com.orderprocessingeda.inventoryservice.repository;

import com.orderprocessingeda.inventoryservice.entity.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {


}
