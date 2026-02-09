package com.example.management.application.ports.in;

import com.example.management.application.exception.InvalidOrderApplicationException;
import com.example.management.application.exception.OrderNotFoundApplicationException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Input port for order use cases. Returns application DTOs and throws application-level exceptions.
 */
public interface OrderUseCase {

    OrderOutputDto createOrder(UUID id, String customerId, List<CreateOrderLineCommand> lines)
            throws InvalidOrderApplicationException;

    Optional<OrderOutputDto> getOrder(UUID id);

    List<OrderOutputDto> getAllOrders();

    void confirmOrder(UUID id) throws OrderNotFoundApplicationException;

    void shipOrder(UUID id) throws OrderNotFoundApplicationException;

    void cancelOrder(UUID id) throws OrderNotFoundApplicationException;

    /**
     * Command for a single order line in create order.
     */
    record CreateOrderLineCommand(String productId, int quantity, java.math.BigDecimal unitPrice) {}
}
