package com.orderprocessingeda.orderservice.dto;

import com.orderprocessingeda.orderservice.entity.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class OrderResponse {

    private String orderNumber;

    private OrderStatus status;

    private BigDecimal totalAmount;
}
