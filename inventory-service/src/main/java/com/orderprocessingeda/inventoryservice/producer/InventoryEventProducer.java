package com.orderprocessingeda.inventoryservice.producer;

import com.atulyadav.event.StockFailedEvent;
import com.atulyadav.event.StockReservedEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class InventoryEventProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public InventoryEventProducer(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publishStockReserved(Long orderId){

        kafkaTemplate.send(
                "stock-reserved-topic",
                new StockReservedEvent(orderId)
        );
    }

    public void publishStockFailed(Long orderId, String reason){

        kafkaTemplate.send(
                "stock-failed-topic",
                new StockFailedEvent(orderId, reason)
        );
    }
}
