package com.example.orderservice.client;

import java.math.BigDecimal;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Component
public class ExchangeRateClient {

    private static final Logger log = LoggerFactory.getLogger(ExchangeRateClient.class);

    private final RestTemplate restTemplate;
    private final String fxUrl;

    public ExchangeRateClient(RestTemplate restTemplate,
                              @Value("${fx.api.url:https://api.exchangerate.host/latest?base=USD&symbols=EUR}") String fxUrl) {
        this.restTemplate = restTemplate;
        this.fxUrl = fxUrl;
    }

    public BigDecimal fetchUsdToEurRate() {
        try {
            ExchangeRateResponse response = restTemplate.getForObject(fxUrl, ExchangeRateResponse.class);
            if (response != null && Boolean.TRUE.equals(response.isSuccess())) {
                Map<String, BigDecimal> rates = response.getRates();
                if (rates != null) {
                    return rates.get("EUR");
                }
            }
        } catch (RestClientException ex) {
            log.warn("Exchange rate API call failed, continuing without FX rate", ex);
        }
        return null;
    }
}

