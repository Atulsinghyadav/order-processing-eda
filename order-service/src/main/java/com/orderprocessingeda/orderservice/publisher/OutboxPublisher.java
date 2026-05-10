package com.orderprocessingeda.orderservice.publisher;

import com.atulyadav.event.OrderCreatedEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.orderprocessingeda.orderservice.entity.OutboxEvent;
import com.orderprocessingeda.orderservice.repository.OutboxRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OutboxPublisher {

    private static final Logger log = LoggerFactory.getLogger(OutboxPublisher.class);
    private final OutboxRepository outboxRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public OutboxPublisher(OutboxRepository outboxRepository, KafkaTemplate<String, Object> kafkaTemplate, ObjectMapper objectMapper) {
        this.outboxRepository = outboxRepository;
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    @Scheduled(fixedRate = 5000)
    public void publishEvents() {

        List<OutboxEvent> outboxEventList = outboxRepository.findByPublishedFalse();

        for(OutboxEvent event: outboxEventList){
            OrderCreatedEvent orderCreatedEvent;

            try {
                orderCreatedEvent  = objectMapper.readValue(
                                    event.getPayload(),
                                     OrderCreatedEvent.class);

                kafkaTemplate.send("order-topic", orderCreatedEvent);
                event.setPublished(true);
                outboxRepository.save(event);

            } catch (Exception ex) {
                log.error("Failed to publish outbox event", ex);
            }
        }
    }
}
