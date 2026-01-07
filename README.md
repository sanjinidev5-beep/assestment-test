# Product & Order Microservices (Spring Boot)

Two Spring Boot microservices demonstrate REST design, CRUD, inter‑service REST calls, DTO usage, validation, global error handling, and third‑party API integration.

## Services
- `product-service` (port 8081): CRUD for products, H2 in‑memory DB, validation + global exceptions. Swagger UI at `/swagger-ui.html`.
- `order-service` (port 8082): Creates/reads orders, fetches product data via REST, validates stock, calculates total price, stores a USD→EUR FX rate from the public ExchangeRate API, and handles remote failures gracefully. Swagger UI at `/swagger-ui.html`.
- `eureka-server` (port 8761): Service discovery (Eureka).
- `gateway` (port 8080): Spring Cloud Gateway routing to product & order services via discovery.

 Run locally (without Docker)


## URL
H2 consoles: `http://localhost:8081/h2-console` and `http://localhost:8082/h2-console` (JDBC URLs are in each `application.properties`).
Swagger: `http://localhost:8081/swagger-ui.html` and `http://localhost:8082/swagger-ui.html`.
Gateway entrypoint: `http://localhost:8080` (proxied `/products/**`, `/orders/**`).

## API samples
Create a product:
```bash
curl -X POST http://localhost:8081/products \
  -H "Content-Type: application/json" \
  -d '{ "name":"Laptop","description":"13 inch","price":1200.00,"availableQuantity":5 }'
```

List products:
```bash
curl http://localhost:8081/products
```

Create an order (calls Product Service + ExchangeRate API):
```bash
curl -X POST http://localhost:8082/orders \
  -H "Content-Type: application/json" \
  -d '{ "productId":1, "quantity":2 }'
```

Get order:
```bash
curl http://localhost:8082/orders/1
```
## Services Explanation

1.Product Service (Port 8081)

This service is responsible for managing products.

Features:

Create, read, update, and delete products

Uses H2 in-memory database for simplicity

Uses Bean Validation to validate input data

Handles errors using global exception handling

Provides Swagger UI to test APIs easily

Swagger UI:
http://localhost:8081/swagger-ui.html

2.Order Service (Port 8082)

This service is responsible for creating and viewing orders.

How it works:

Accepts product ID and quantity from the user

Calls Product Service to get product details

Checks if enough stock is available

Calculates total order price

Calls a public USD → EUR exchange rate API

Saves the order even if the FX API fails

Swagger UI:
http://localhost:8082/swagger-ui.html

3.Eureka Server (Port 8761)

Eureka Server is used for service discovery.

All services register themselves here

Helps services communicate without hardcoding URLs

Eureka Dashboard:
http://localhost:8761

4.API Gateway (Port 8080)

The API Gateway acts as a single entry point for all requests.

Routes requests to Product and Order services

Uses Eureka to locate services

Gateway routes:
/products/** → Product Service
/orders/** → Order Service

## Project layout
- `product-service/src/main/java/com/example/productservice` – controllers, DTOs, service, repository, exceptions.
- `order-service/src/main/java/com/example/orderservice` – controllers, DTOs, service, clients (Product + FX), repository, exceptions.
- `eureka-server` – standalone Eureka registry.
- `gateway` – Spring Cloud Gateway with discovery-based routing.

## Technology Used

Java 11
Spring Boot
Spring Web
Spring Data JPA
Hibernate
Bean Validation
H2 Database
Spring Cloud Eureka
Spring Cloud Gateway
Springdoc OpenAPI (Swagger)
Maven
Docker & Docker Compose (basic usage)

