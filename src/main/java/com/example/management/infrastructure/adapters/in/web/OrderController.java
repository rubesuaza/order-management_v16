package com.example.management.infrastructure.adapters.in.web;

import com.example.management.application.ports.in.OrderUseCase;
import com.example.management.infrastructure.adapters.in.web.dto.CreateOrderRequestDto;
import com.example.management.infrastructure.adapters.in.web.dto.OrderResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * REST adapter for order use cases. Maps application DTOs to HTTP response DTOs.
 */
@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderUseCase orderUseCase;

    public OrderController(OrderUseCase orderUseCase) {
        this.orderUseCase = orderUseCase;
    }

    @PostMapping
    public ResponseEntity<OrderResponseDto> createOrder(@RequestBody CreateOrderRequestDto request) {
        var output = orderUseCase.createOrder(
                request.id(),
                request.customerId(),
                request.toCommands()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(OrderResponseDto.from(output));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponseDto> getOrder(@PathVariable UUID id) {
        return orderUseCase.getOrder(id)
                .map(OrderResponseDto::from)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public List<OrderResponseDto> getAllOrders() {
        return orderUseCase.getAllOrders().stream()
                .map(OrderResponseDto::from)
                .toList();
    }

    @PostMapping("/{id}/confirm")
    public ResponseEntity<Void> confirmOrder(@PathVariable UUID id) {
        orderUseCase.confirmOrder(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/ship")
    public ResponseEntity<Void> shipOrder(@PathVariable UUID id) {
        orderUseCase.shipOrder(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/cancel")
    public ResponseEntity<Void> cancelOrder(@PathVariable UUID id) {
        orderUseCase.cancelOrder(id);
        return ResponseEntity.noContent().build();
    }
}
