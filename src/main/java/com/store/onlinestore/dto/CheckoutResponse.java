package com.store.onlinestore.dto;

import lombok.Builder;

@Builder
public class CheckoutResponse {

    private Long orderId;
    private Double totalPrice;
    private String message;

    public CheckoutResponse() {
    }

    public CheckoutResponse(Long orderId, Double totalPrice, String message) {
        this.orderId = orderId;
        this.totalPrice = totalPrice;
        this.message = message;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
