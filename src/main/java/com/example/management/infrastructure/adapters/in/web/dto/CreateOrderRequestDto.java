package com.example.management.infrastructure.adapters.in.web.dto;

import com.example.management.application.ports.in.OrderUseCase;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

/**
 * DTO for create order API request.
 */
public record CreateOrderRequestDto(
        UUID id,
        String customerId,
        List<OrderLineRequestDto> lines
) {
    public List<OrderUseCase.CreateOrderLineCommand> toCommands() {
        return lines.stream()
                .map(l -> new OrderUseCase.CreateOrderLineCommand(l.productId(), l.quantity(), l.unitPrice()))
                .toList();
    }

    public record OrderLineRequestDto(String productId, int quantity, BigDecimal unitPrice) {}
}
