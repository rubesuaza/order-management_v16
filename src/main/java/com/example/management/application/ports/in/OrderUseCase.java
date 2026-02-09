package com.example.management.application.ports.in;

import com.example.management.domain.model.Order;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Input port for order use cases.
 */
public interface OrderUseCase {

    Order createOrder(UUID id, String customerId, List<CreateOrderLineCommand> lines);

    Optional<Order> getOrder(UUID id);

    List<Order> getAllOrders();

    void confirmOrder(UUID id);

    void shipOrder(UUID id);

    void cancelOrder(UUID id);

    /**
     * Command for a single order line in create order.
     */
    record CreateOrderLineCommand(String productId, int quantity, java.math.BigDecimal unitPrice) {}
}
