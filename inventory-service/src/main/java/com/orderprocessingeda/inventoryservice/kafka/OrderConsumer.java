package com.orderprocessingeda.inventoryservice.kafka;

import com.atulyadav.event.OrderCreatedEvent;
import com.atulyadav.event.OrderItemEvent;
import com.orderprocessingeda.inventoryservice.service.InventoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class OrderConsumer {

    private static final Logger log = LoggerFactory.getLogger(OrderConsumer.class);
    private final InventoryService inventoryService;

    public OrderConsumer(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
        log.info("OrderConsumer bean created");
    }

    @KafkaListener(topics = "order-topic", groupId = "inventory-group")
    public void consume(OrderCreatedEvent event) {

        log.info("Received order event: {}", event.getOrderId());

        for (OrderItemEvent item : event.getItems()) {
            inventoryService.reduceStock(item.getProductId(), item.getQuantity());
        }
    }



}
