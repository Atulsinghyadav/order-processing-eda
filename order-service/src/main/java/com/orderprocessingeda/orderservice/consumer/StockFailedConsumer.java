package com.orderprocessingeda.orderservice.consumer;

import com.atulyadav.event.StockFailedEvent;
import com.orderprocessingeda.orderservice.entity.Order;
import com.orderprocessingeda.orderservice.entity.OrderStatus;
import com.orderprocessingeda.orderservice.repository.OrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class StockFailedConsumer {

    private static final Logger log = LoggerFactory.getLogger(StockFailedConsumer.class);
    private OrderRepository orderRepository;

    public StockFailedConsumer(OrderRepository orderRepository){
        this.orderRepository = orderRepository;
    }

    @KafkaListener(topics = "stock-failed-topic", groupId = "order-group")
    public void consumer(StockFailedEvent event){

        Order order = orderRepository
                .findById(event.getOrderId())
                .orElseThrow();

//        if(order == null){
//            log.error( "Order not found for orderId={}", event.getOrderId());
//            return;
//        }

        order.setStatus(OrderStatus.CANCELLED);

        orderRepository.save(order);
    }
}
