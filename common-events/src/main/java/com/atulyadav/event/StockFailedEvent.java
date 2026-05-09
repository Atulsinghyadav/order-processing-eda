package com.atulyadav.event;

public class StockFailedEvent {

    private Long orderId;
    private String reason;

    public StockFailedEvent() {
    }

    public StockFailedEvent(Long orderId, String reason) {
        this.orderId = orderId;
        this.reason = reason;
    }

    public Long getOrderId() {
        return orderId;
    }

    public String getReason() {
        return reason;
    }

}
