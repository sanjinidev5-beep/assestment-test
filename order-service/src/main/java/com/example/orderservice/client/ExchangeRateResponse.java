package com.example.orderservice.client;

import java.math.BigDecimal;
import java.util.Map;

public class ExchangeRateResponse {
    private Map<String, BigDecimal> rates;
    private boolean success = true;

    public Map<String, BigDecimal> getRates() {
        return rates;
    }

    public void setRates(Map<String, BigDecimal> rates) {
        this.rates = rates;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}

