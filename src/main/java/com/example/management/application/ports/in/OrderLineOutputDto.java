package com.example.management.application.ports.in;

import java.math.BigDecimal;

/**
 * Application-level DTO for an order line in output.
 */
public record OrderLineOutputDto(
        String productId,
        int quantity,
        BigDecimal unitPrice,
        BigDecimal lineTotal
) {}
