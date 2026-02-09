package com.example.management.infrastructure.adapters.in.web.dto;

import com.example.management.domain.model.Order;
import com.example.management.domain.model.OrderStatus;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

/**
 * DTO for order in API responses.
 */
public record OrderResponseDto(
        UUID id,
        String customerId,
        OrderStatus status,
        List<OrderLineDto> lines,
        BigDecimal total
) {
    public static OrderResponseDto from(Order order) {
        List<OrderLineDto> lineDtos = order.getLines().stream()
                .map(l -> OrderLineDto.of(
                        l.getProductId(),
                        l.getQuantity(),
                        l.getUnitPrice(),
                        l.getLineTotal()))
                .toList();
        return new OrderResponseDto(
                order.getId(),
                order.getCustomerId(),
                order.getStatus(),
                lineDtos,
                order.getTotal()
        );
    }
}
