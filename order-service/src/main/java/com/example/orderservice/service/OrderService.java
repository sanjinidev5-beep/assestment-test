package com.example.orderservice.service;

import com.example.orderservice.client.ExchangeRateClient;
import com.example.orderservice.client.ProductFeignClient;
import com.example.orderservice.dto.OrderRequest;
import com.example.orderservice.dto.OrderResponse;
import com.example.orderservice.dto.ProductDto;
import com.example.orderservice.dto.ReduceQuantityRequest;
import com.example.orderservice.entity.Order;
import com.example.orderservice.entity.OrderStatus;
import com.example.orderservice.exception.ResourceNotFoundException;
import com.example.orderservice.repository.OrderRepository;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductFeignClient productFeignClient;
    private final ExchangeRateClient exchangeRateClient;

    public OrderService(OrderRepository orderRepository, ProductFeignClient productFeignClient, ExchangeRateClient exchangeRateClient) {
        this.orderRepository = orderRepository;
        this.productFeignClient = productFeignClient;
        this.exchangeRateClient = exchangeRateClient;
    }

    @Transactional
    public OrderResponse create(OrderRequest request) {
        // Fetch product details using Feign client
        ProductDto product = productFeignClient.getProduct(request.getProductId());
        if (product == null) {
            throw new ResourceNotFoundException("Product not found with id " + request.getProductId());
        }
        if (product.getAvailableQuantity() < request.getQuantity()) {
            throw new IllegalArgumentException("Insufficient product quantity");
        }

        // Calculate total price
        BigDecimal totalPrice = product.getPrice().multiply(BigDecimal.valueOf(request.getQuantity()));
        
        // Fetch exchange rate
        BigDecimal fxRate = exchangeRateClient.fetchUsdToEurRate();

        // Create order
        Order order = new Order(
                request.getProductId(),
                request.getQuantity(),
                totalPrice,
                fxRate,
                OrderStatus.CREATED,
                LocalDateTime.now()
        );

        Order saved = orderRepository.save(order);
        
        // Reduce product quantity using Feign client
        productFeignClient.reduceQuantity(request.getProductId(), new ReduceQuantityRequest(request.getQuantity()));
        
        return toResponse(saved);
    }

    public OrderResponse findById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id " + id));
        return toResponse(order);
    }

    public List<OrderResponse> findAll() {
        return orderRepository.findAll().stream().map(this::toResponse).collect(Collectors.toList());
    }

    private OrderResponse toResponse(Order order) {
        return new OrderResponse(
                order.getId(),
                order.getProductId(),
                order.getQuantity(),
                order.getTotalPrice(),
                order.getFxRateUsdToEur(),
                order.getStatus(),
                order.getCreatedAt()
        );
    }
}

