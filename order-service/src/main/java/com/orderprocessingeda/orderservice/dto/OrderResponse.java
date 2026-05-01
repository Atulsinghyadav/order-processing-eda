package com.orderprocessingeda.orderservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class OrderResponse {

    private String orderNumber;

    private String status;

    private BigDecimal totalAmount;
}
