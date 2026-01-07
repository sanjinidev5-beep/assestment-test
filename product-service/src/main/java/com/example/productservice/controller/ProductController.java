package com.example.productservice.controller;

import com.example.productservice.dto.ProductRequest;
import com.example.productservice.dto.ProductResponse;
import com.example.productservice.dto.ReduceQuantityRequest;
import com.example.productservice.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/products")
@Tag(name = "Product Management", description = "APIs for managing products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    @Operation(summary = "Create a new product", description = "Creates a new product with the provided details")
    @ResponseStatus(HttpStatus.CREATED)
    public ProductResponse create(@Valid @RequestBody ProductRequest request) {
        return productService.create(request);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get product by ID", description = "Retrieves a product by its unique identifier")
    public ProductResponse getById(
            @Parameter(description = "Product ID", required = true, example = "1")
            @PathVariable("id") Long id) {
        return productService.findById(id);
    }

    @GetMapping
    @Operation(summary = "List all products", description = "Retrieves a list of all available products")
    public List<ProductResponse> listAll() {
        return productService.findAll();
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update product", description = "Updates an existing product with the provided details")
    public ProductResponse update(
            @Parameter(description = "Product ID", required = true, example = "1")
            @PathVariable("id") Long id, 
            @Valid @RequestBody ProductRequest request) {
        return productService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete product", description = "Deletes a product by its unique identifier")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(
            @Parameter(description = "Product ID", required = true, example = "1")
            @PathVariable("id") Long id) {
        productService.delete(id);
    }

    @PutMapping("/{id}/reduce-quantity")
    @Operation(summary = "Reduce product quantity", description = "Reduces the available quantity of a product")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void reduceQuantity(
            @Parameter(description = "Product ID", required = true, example = "1")
            @PathVariable("id") Long id,
            @Valid @RequestBody ReduceQuantityRequest request) {
        productService.reduceQuantity(id, request.getQuantity());
    }
}

