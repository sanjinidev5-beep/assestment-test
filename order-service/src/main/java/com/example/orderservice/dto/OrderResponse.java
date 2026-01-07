package com.example.orderservice.dto;

import com.example.orderservice.entity.OrderStatus;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class OrderResponse {
    private Long id;
    private Long productId;
    private Integer quantity;
    private BigDecimal totalPrice;
    private BigDecimal fxRateUsdToEur;
    private OrderStatus status;
    private LocalDateTime createdAt;

    public OrderResponse() {
    }

    public OrderResponse(Long id, Long productId, Integer quantity, BigDecimal totalPrice, BigDecimal fxRateUsdToEur, OrderStatus status, LocalDateTime createdAt) {
        this.id = id;
        this.productId = productId;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
        this.fxRateUsdToEur = fxRateUsdToEur;
        this.status = status;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public BigDecimal getFxRateUsdToEur() {
        return fxRateUsdToEur;
    }

    public void setFxRateUsdToEur(BigDecimal fxRateUsdToEur) {
        this.fxRateUsdToEur = fxRateUsdToEur;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}

