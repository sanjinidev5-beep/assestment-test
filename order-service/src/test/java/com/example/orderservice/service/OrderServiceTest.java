package com.example.orderservice.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.example.orderservice.client.ExchangeRateClient;
import com.example.orderservice.client.ProductFeignClient;
import com.example.orderservice.dto.OrderRequest;
import com.example.orderservice.dto.ProductDto;
import com.example.orderservice.dto.ReduceQuantityRequest;
import com.example.orderservice.entity.Order;
import com.example.orderservice.exception.ResourceNotFoundException;
import com.example.orderservice.repository.OrderRepository;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;
    @Mock
    private ProductFeignClient productFeignClient;
    @Mock
    private ExchangeRateClient exchangeRateClient;

    private OrderService orderService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        orderService = new OrderService(orderRepository, productFeignClient, exchangeRateClient);
    }

    @Test
    void create_shouldComputeTotalAndPersist() {
        ProductDto product = new ProductDto();
        product.setId(1L);
        product.setAvailableQuantity(10);
        product.setPrice(new BigDecimal("5.00"));
        when(productFeignClient.getProduct(1L)).thenReturn(product);
        when(exchangeRateClient.fetchUsdToEurRate()).thenReturn(new BigDecimal("0.9"));
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> {
            Order o = invocation.getArgument(0);
            o.setId(1L);
            return o;
        });

        OrderRequest request = new OrderRequest();
        request.setProductId(1L);
        request.setQuantity(2);

        var response = orderService.create(request);

        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getTotalPrice()).isEqualByComparingTo("10.00");
        assertThat(response.getFxRateUsdToEur()).isEqualByComparingTo("0.9");
    }

    @Test
    void create_shouldRejectWhenInsufficientStock() {
        ProductDto product = new ProductDto();
        product.setId(1L);
        product.setAvailableQuantity(1);
        product.setPrice(new BigDecimal("5.00"));
        when(productFeignClient.getProduct(1L)).thenReturn(product);

        OrderRequest request = new OrderRequest();
        request.setProductId(1L);
        request.setQuantity(3);

        assertThatThrownBy(() -> orderService.create(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void findById_shouldThrowWhenMissing() {
        when(orderRepository.findById(99L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> orderService.findById(99L))
                .isInstanceOf(ResourceNotFoundException.class);
    }
}

