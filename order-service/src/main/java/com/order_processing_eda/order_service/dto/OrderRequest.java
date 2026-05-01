package com.order_processing_eda.order_service.dto;

import com.order_processing_eda.order_service.entity.OrderItem;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class OrderRequest {

    @NotNull
    @Positive
    private Long userId;

    @NotEmpty
    @Valid
    private List<OrderItemRequest> items;
}
