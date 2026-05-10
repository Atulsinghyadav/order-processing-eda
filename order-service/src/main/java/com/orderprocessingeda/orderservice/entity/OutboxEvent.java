package com.orderprocessingeda.orderservice.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "outbox_events")
public class OutboxEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;

    private String aggregateType;

    private Long aggregateId;

    private String eventType;

    @Column(columnDefinition = "TEXT")
    private String payload;

    private boolean published;

    private LocalDateTime createdAt;

    public OutboxEvent() {

    }
    public OutboxEvent(String aggregateType,
                       Long aggregateId,
                       String eventType,
                       String payload,
                       boolean published,
                       LocalDateTime createdAt) {
        this.aggregateType = aggregateType;
        this.aggregateId = aggregateId;
        this.eventType = eventType;
        this.payload = payload;
        this.published = published;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public String getAggregateType() {
        return aggregateType;
    }

    public Long getAggregateId() {
        return aggregateId;
    }

    public String getEventType() {
        return eventType;
    }

    public String getPayload() {
        return payload;
    }

    public boolean isPublished() {
        return published;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setPublished(boolean published) {
        this.published = published;
    }

}
