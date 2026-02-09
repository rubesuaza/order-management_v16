package com.example.management.application.ports.in;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

/**
 * Application-level DTO for order output. Keeps the application layer independent of domain model.
 */
public record OrderOutputDto(
        UUID id,
        String customerId,
        String status,
        List<OrderLineOutputDto> lines,
        BigDecimal total
) {}
