package com.orderprocessingeda.orderservice.service;

import com.orderprocessingeda.orderservice.client.InventoryClient;
import com.orderprocessingeda.orderservice.client.InventoryServiceWrapper;
import com.orderprocessingeda.orderservice.dto.OrderItemRequest;
import com.orderprocessingeda.orderservice.dto.OrderRequest;
import com.orderprocessingeda.orderservice.dto.OrderResponse;
import com.orderprocessingeda.orderservice.entity.Order;
import com.orderprocessingeda.orderservice.entity.OrderItem;
import com.orderprocessingeda.orderservice.exception.InsufficientStockException;
import com.orderprocessingeda.orderservice.exception.InventoryUnavailableException;
import com.orderprocessingeda.orderservice.repository.OrderRepository;
import feign.FeignException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class OrderService {

    private InventoryServiceWrapper inventoryServiceWrapper;
    private OrderRepository orderRepository;
    private InventoryClient inventoryClient;
    private static final Logger log = LoggerFactory.getLogger(OrderService.class);

    public OrderService(OrderRepository orderRepository, InventoryClient inventoryClient, InventoryServiceWrapper inventoryServiceWrapper) {
        this.orderRepository = orderRepository;
        this.inventoryClient = inventoryClient;
        this.inventoryServiceWrapper = inventoryServiceWrapper;
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

            boolean available = inventoryServiceWrapper.checkInventory(itemRequest.getProductId(), itemRequest.getQuantity());
            if(!available)  {
                log.warn("Stock not available for productId = {}, quantity = {}", itemRequest.getProductId(), itemRequest.getQuantity());
                throw new InsufficientStockException("Insufficient stock");
            }

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

        OrderResponse orderResponse = new OrderResponse(order.getOrderNumber(), order.getStatus(), order.getTotalAmount());

        return orderResponse;
    }


}
