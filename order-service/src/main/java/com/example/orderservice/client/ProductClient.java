package com.example.orderservice.client;

import com.example.orderservice.dto.ProductDto;
import com.example.orderservice.exception.RemoteServiceException;
import com.example.orderservice.exception.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Component
public class ProductClient {

    private static final Logger log = LoggerFactory.getLogger(ProductClient.class);

    private final RestTemplate restTemplate;
    private final String productServiceUrl;

    public ProductClient(RestTemplate restTemplate,
                         @Value("${product.service.url:http://localhost:8081}") String productServiceUrl) {
        this.restTemplate = restTemplate;
        this.productServiceUrl = productServiceUrl;
    }

    public ProductDto getProduct(Long id) {
        String url = productServiceUrl + "/products/" + id;
        try {
            return restTemplate.getForObject(url, ProductDto.class);
        } catch (HttpClientErrorException.NotFound notFound) {
            throw new ResourceNotFoundException("Product not found with id " + id);
        } catch (RestClientException ex) {
            log.error("Failed to fetch product {} from Product Service", id, ex);
            throw new RemoteServiceException("Unable to reach Product Service", ex);
        }
    }
}

