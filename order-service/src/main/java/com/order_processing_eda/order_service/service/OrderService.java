package com.order_processing_eda.order_service.service;

import com.order_processing_eda.order_service.dto.OrderItemRequest;
import com.order_processing_eda.order_service.dto.OrderRequest;
import com.order_processing_eda.order_service.dto.OrderResponse;
import com.order_processing_eda.order_service.entity.Order;
import com.order_processing_eda.order_service.entity.OrderItem;
import com.order_processing_eda.order_service.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class OrderService {

    private OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
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

        OrderResponse orderResponse = new OrderResponse(order.getOrderNumber(), order.getStatus(), order.getTotalAmount());

        return orderResponse;
    }
}
