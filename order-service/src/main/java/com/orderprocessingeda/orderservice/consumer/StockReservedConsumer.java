package com.orderprocessingeda.orderservice.consumer;

import com.atulyadav.event.StockReservedEvent;
import com.orderprocessingeda.orderservice.entity.Order;
import com.orderprocessingeda.orderservice.entity.OrderStatus;
import com.orderprocessingeda.orderservice.repository.OrderRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class StockReservedConsumer {

    private OrderRepository orderRepository;

    public StockReservedConsumer(OrderRepository orderRepository){
        this.orderRepository = orderRepository;
    }


    @KafkaListener(topics = "stock-reserved-topic", groupId = "order-group")
    public void consume(StockReservedEvent event) {

        Order order = orderRepository
                .findById(event.getOrderId())
                .orElseThrow();

        order.setStatus(OrderStatus.CONFIRMED);
        orderRepository.save(order);

    }
}
