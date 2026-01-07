package com.example.orderservice.dto;

public class ReduceQuantityRequest {
    private Integer quantity;

    public ReduceQuantityRequest() {
    }

    public ReduceQuantityRequest(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}

