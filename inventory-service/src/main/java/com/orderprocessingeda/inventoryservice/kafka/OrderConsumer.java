package com.orderprocessingeda.inventoryservice.kafka;

import com.atulyadav.event.OrderCreatedEvent;
import com.atulyadav.event.OrderItemEvent;
import com.orderprocessingeda.inventoryservice.service.InventoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.BackOff;
import org.springframework.kafka.annotation.DltHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class OrderConsumer {

    private static final Logger log = LoggerFactory.getLogger(OrderConsumer.class);
    private final InventoryService inventoryService;
    private final Set<Long> processedOrders = new HashSet<>();

    public OrderConsumer(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
        log.info("OrderConsumer bean created");
    }

    @RetryableTopic(attempts = "3", backOff = @BackOff( delay = 2000, multiplier = 2))
    @KafkaListener(topics = "order-topic", groupId = "inventory-group")
    public void consume(OrderCreatedEvent event) {

        log.info("Received order event: {}", event.getOrderId());

        Long orderId = event.getOrderId();

        if(processedOrders.contains(orderId)){
            log.warn("Duplicate event ignored for orderId={}", orderId);
            return;
        }

        for (OrderItemEvent item : event.getItems()) {
            inventoryService.reduceStock(item.getProductId(), item.getQuantity(), orderId);
        }

        processedOrders.add(orderId);

        log.info("Processed orderId={}", orderId);
    }

    @DltHandler
    public void handleDlt(OrderCreatedEvent event){
        log.error("Message moved to DLT for orderId={}",
                event.getOrderId());
    }
}
