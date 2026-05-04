package com.orderprocessingeda.orderservice.service;

import com.atulyadav.event.OrderCreatedEvent;
import com.atulyadav.event.OrderItemEvent;
import com.orderprocessingeda.orderservice.dto.OrderItemRequest;
import com.orderprocessingeda.orderservice.dto.OrderRequest;
import com.orderprocessingeda.orderservice.dto.OrderResponse;
import com.orderprocessingeda.orderservice.entity.Order;
import com.orderprocessingeda.orderservice.entity.OrderItem;
import com.orderprocessingeda.orderservice.kafka.OrderProducer;
import com.orderprocessingeda.orderservice.repository.OrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class OrderService {

    private OrderRepository orderRepository;
    private OrderProducer orderProducer;
    private static final Logger log = LoggerFactory.getLogger(OrderService.class);

    public OrderService(OrderRepository orderRepository, OrderProducer orderProducer) {
        this.orderRepository = orderRepository;
        this.orderProducer = orderProducer;
    }

    @Transactional
    public OrderResponse createOrder(OrderRequest request){

        Order order = new Order();
        order.setUserId(request.getUserId());
        order.setStatus("PENDING");
        order.setCreatedAt(LocalDateTime.now());
        order.setUpdatedAt(LocalDateTime.now());
        order.setOrderNumber(UUID.randomUUID().toString());
        order.setIdempotencyKey(UUID.randomUUID().toString());
        order.setCurrency("INR");

        List<OrderItem> orderItems = new ArrayList<>();
        BigDecimal totalAmount = BigDecimal.ZERO;

        for(OrderItemRequest itemRequest: request.getItems()){

            OrderItem item = new OrderItem();
            item.setProductId(itemRequest.getProductId());
            item.setQuantity(itemRequest.getQuantity());
            item.setPrice(itemRequest.getPrice());

            item.setOrder(order);

            orderItems.add(item);

            totalAmount = totalAmount.add(item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())));
        }

        order.setItems(orderItems);
        order.setTotalAmount(totalAmount);
        orderRepository.save(order);
        log.info("Order created for userId = {}", order.getUserId());

        List<OrderItemEvent> items = request.getItems().stream().map(
                i -> new OrderItemEvent(i.getProductId(), i.getQuantity())).toList();

        orderProducer.send(new OrderCreatedEvent(order.getId(),items));

        OrderResponse orderResponse = new OrderResponse(order.getOrderNumber(), order.getStatus(), order.getTotalAmount());

        return orderResponse;
    }
}
