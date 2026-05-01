package com.order_processing_eda.order_service.controller;

import com.order_processing_eda.order_service.dto.OrderRequest;
import com.order_processing_eda.order_service.dto.OrderResponse;
import com.order_processing_eda.order_service.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(@RequestBody @Valid OrderRequest request){

       OrderResponse orderResponse =  orderService.createOrder(request);
       return ResponseEntity.status(201).body(orderResponse);
    }
}
