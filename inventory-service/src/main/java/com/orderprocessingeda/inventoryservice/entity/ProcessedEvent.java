package com.orderprocessingeda.inventoryservice.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "processed_events")
public class ProcessedEvent {

    @Id
    private String eventId;
    private String consumerGroup;
    private LocalDateTime processedAt;

    public ProcessedEvent() {
    }

    public ProcessedEvent(String eventId,
                          String consumerGroup,
                          LocalDateTime processedAt) {
        this.eventId = eventId;
        this.consumerGroup = consumerGroup;
        this.processedAt = processedAt;
    }

    public String getEventId() {
        return eventId;
    }

    public String getConsumerGroup() {
        return consumerGroup;
    }

    public LocalDateTime getProcessedAt() {
        return processedAt;
    }

}
