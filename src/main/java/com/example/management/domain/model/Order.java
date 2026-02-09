package com.example.management.domain.model;

import com.example.management.domain.exception.InvalidOrderException;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * Aggregate root for order management.
 * Invariants: at least one line; total = sum of line totals; valid customer and id.
 */
public class Order {

    private final UUID id;
    private final String customerId;
    private OrderStatus status;
    private final List<OrderLine> lines;
    private final BigDecimal total;

    private Order(UUID id, String customerId, List<OrderLine> lines) {
        this.id = id;
        this.customerId = customerId;
        this.lines = List.copyOf(lines);
        this.total = computeTotal(lines);
        this.status = OrderStatus.DRAFT;
    }

    private static BigDecimal computeTotal(List<OrderLine> lines) {
        return lines.stream()
                .map(OrderLine::getLineTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public static Order create(UUID id, String customerId, List<OrderLine> lines) {
        if (id == null) {
            throw new InvalidOrderException("Order id cannot be null");
        }
        if (customerId == null || customerId.isBlank()) {
            throw new InvalidOrderException("Customer id cannot be null or blank");
        }
        if (lines == null || lines.isEmpty()) {
            throw new InvalidOrderException("Order must have at least one line");
        }
        return new Order(id, customerId, lines);
    }

    public UUID getId() {
        return id;
    }

    public String getCustomerId() {
        return customerId;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public List<OrderLine> getLines() {
        return Collections.unmodifiableList(lines);
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void confirm() {
        this.status = OrderStatus.CONFIRMED;
    }

    public void ship() {
        this.status = OrderStatus.SHIPPED;
    }

    public void cancel() {
        this.status = OrderStatus.CANCELLED;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return Objects.equals(id, order.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
