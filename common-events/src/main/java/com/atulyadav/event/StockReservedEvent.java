package com.atulyadav.event;

public class StockReservedEvent {
    private Long orderId;

    public StockReservedEvent() {
    }

    public StockReservedEvent(Long orderId) {
        this.orderId = orderId;
    }

    public Long getOrderId() {
        return orderId;
    }
}
