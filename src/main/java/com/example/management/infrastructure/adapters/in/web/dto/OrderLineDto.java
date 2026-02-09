package com.example.management.infrastructure.adapters.in.web.dto;

import java.math.BigDecimal;

/**
 * DTO for an order line in API requests/responses.
 */
public record OrderLineDto(String productId, int quantity, BigDecimal unitPrice, BigDecimal lineTotal) {

    public static OrderLineDto of(String productId, int quantity, BigDecimal unitPrice, BigDecimal lineTotal) {
        return new OrderLineDto(productId, quantity, unitPrice, lineTotal);
    }
}
