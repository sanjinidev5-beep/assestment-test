package com.example.orderservice.client;

import com.example.orderservice.dto.ProductDto;
import com.example.orderservice.dto.ReduceQuantityRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "product-service", url = "${product.service.url:http://localhost:8081}", path = "/products")
public interface ProductFeignClient {

    @GetMapping("/{id}")
    ProductDto getProduct(@PathVariable("id") Long id);

    @PutMapping("/{id}/reduce-quantity")
    void reduceQuantity(@PathVariable("id") Long id, @RequestBody ReduceQuantityRequest request);
}

