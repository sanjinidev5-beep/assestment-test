# Product & Order Microservices (Spring Boot)

Two Spring Boot microservices demonstrate REST design, CRUD, inter‑service REST calls, DTO usage, validation, global error handling, and third‑party API integration.

## Services
- `product-service` (port 8081): CRUD for products, H2 in‑memory DB, validation + global exceptions. Swagger UI at `/swagger-ui.html`.
- `order-service` (port 8082): Creates/reads orders, fetches product data via REST, validates stock, calculates total price, stores a USD→EUR FX rate from the public ExchangeRate API, and handles remote failures gracefully. Swagger UI at `/swagger-ui.html`.
- `eureka-server` (port 8761): Service discovery (Eureka).
- `gateway` (port 8080): Spring Cloud Gateway routing to product & order services via discovery.

## Tech
Spring Boot 3.5.9, Java 21, Spring Web, JPA/Hibernate, Bean Validation, H2, RestTemplate, SLF4J, Spring Cloud Gateway, Eureka, Springdoc OpenAPI.

## Run locally (without Docker)
Open two terminals:
```bash
# Terminal 1 - Eureka
cd eureka-server
mvn spring-boot:run

# Terminal 2 - Product Service
cd product-service
mvn spring-boot:run

# Terminal 3 - Order Service
cd order-service
mvn spring-boot:run

# Terminal 4 - Gateway
cd gateway
mvn spring-boot:run
```

H2 consoles: `http://localhost:8081/h2-console` and `http://localhost:8082/h2-console` (JDBC URLs are in each `application.properties`).
Swagger: `http://localhost:8081/swagger-ui.html` and `http://localhost:8082/swagger-ui.html`.
Gateway entrypoint: `http://localhost:8080` (proxied `/products/**`, `/orders/**`).

## Run with Docker Compose
```bash
docker-compose build
docker-compose up
```
Services: gateway (8080), product (8081), order (8082), eureka (8761).

Images use `openjdk:21-jdk` as the runtime base. Ensure you `mvn clean package -DskipTests` (or let the Docker build do it) so `target/*.jar` exists for the COPY step.

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

## Configuration knobs
- Product service base URL (used by order-service): `product.service.url` (default `http://localhost:8081`) or env `PRODUCT_SERVICE_URL`
- FX API endpoint: `fx.api.url` (default `https://api.exchangerate.host/latest?base=USD&symbols=EUR`) or env `FX_API_URL`
- Eureka server URL: `eureka.client.service-url.defaultZone` (default `http://localhost:8761/eureka`)

If the FX API call fails, the order is still created and `fxRateUsdToEur` is `null`.

## Project layout
- `product-service/src/main/java/com/example/productservice` – controllers, DTOs, service, repository, exceptions.
- `order-service/src/main/java/com/example/orderservice` – controllers, DTOs, service, clients (Product + FX), repository, exceptions.
- `eureka-server` – standalone Eureka registry.
- `gateway` – Spring Cloud Gateway with discovery-based routing.

## Next ideas (optional)
- Add persistence to Postgres, integrate authentication, or add more e2e tests.

