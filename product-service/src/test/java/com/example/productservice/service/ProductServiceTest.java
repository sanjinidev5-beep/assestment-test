package com.example.productservice.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.example.productservice.dto.ProductRequest;
import com.example.productservice.entity.Product;
import com.example.productservice.exception.ResourceNotFoundException;
import com.example.productservice.repository.ProductRepository;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    private ProductService productService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        productService = new ProductService(productRepository);
    }

    @Test
    void create_shouldPersistProduct() {
        ProductRequest request = new ProductRequest();
        request.setName("Test");
        request.setDescription("Desc");
        request.setPrice(new BigDecimal("10.00"));
        request.setAvailableQuantity(5);

        when(productRepository.save(any(Product.class))).thenAnswer(invocation -> {
            Product p = invocation.getArgument(0);
            p.setId(1L);
            return p;
        });

        var response = productService.create(request);

        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getName()).isEqualTo("Test");
        assertThat(response.getPrice()).isEqualByComparingTo("10.00");
    }

    @Test
    void findById_shouldThrowWhenMissing() {
        when(productRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> productService.findById(99L))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void update_shouldMutateExistingProduct() {
        Product existing = new Product("Old", "Old", new BigDecimal("5.00"), 1, LocalDateTime.now());
        existing.setId(1L);
        when(productRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(productRepository.save(any(Product.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ProductRequest request = new ProductRequest();
        request.setName("New");
        request.setDescription("New desc");
        request.setPrice(new BigDecimal("9.99"));
        request.setAvailableQuantity(3);

        var response = productService.update(1L, request);

        assertThat(response.getName()).isEqualTo("New");
        assertThat(response.getPrice()).isEqualByComparingTo("9.99");
        assertThat(response.getAvailableQuantity()).isEqualTo(3);
    }
}

