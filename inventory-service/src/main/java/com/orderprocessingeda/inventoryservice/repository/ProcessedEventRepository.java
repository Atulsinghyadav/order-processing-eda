package com.orderprocessingeda.inventoryservice.repository;

import com.orderprocessingeda.inventoryservice.entity.ProcessedEvent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface ProcessedEventRepository extends JpaRepository<ProcessedEvent, String> {

}
