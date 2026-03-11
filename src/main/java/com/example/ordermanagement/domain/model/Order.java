package com.example.ordermanagement.domain.model;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public final class Order {

    private final UUID id;
    private final String customerId;
    private final List<OrderItem> items;
    private final Money total;

    private Order(UUID id, String customerId, List<OrderItem> items, Money total) {
        this.id = Objects.requireNonNull(id, "El identificador de la orden no puede ser nulo");
        this.customerId = validateCustomerId(customerId);
        this.items = validateItems(items);
        this.total = validateTotal(total);
    }

    public static Order create(UUID id, String customerId, List<OrderItem> items) {
        Money total = calculateTotal(items);
        return new Order(id, customerId, items, total);
    }

    private static String validateCustomerId(String customerId) {
        Objects.requireNonNull(customerId, "El identificador de cliente no puede ser nulo");
        if (customerId.isBlank()) {
            throw new IllegalArgumentException("El identificador de cliente no puede estar vacío");
        }
        return customerId;
    }

    private static List<OrderItem> validateItems(List<OrderItem> items) {
        Objects.requireNonNull(items, "La lista de ítems no puede ser nula");
        if (items.isEmpty()) {
            throw new IllegalArgumentException("La orden debe contener al menos un ítem");
        }
        if (items.stream().anyMatch(Objects::isNull)) {
            throw new IllegalArgumentException("La lista de ítems no puede contener valores nulos");
        }
        return List.copyOf(items);
    }

    private static Money validateTotal(Money total) {
        Objects.requireNonNull(total, "El total no puede ser nulo");
        if (!total.isGreaterOrEqualThanMinimum()) {
            throw new IllegalArgumentException("El total de la orden debe ser al menos 10 unidades monetarias");
        }
        return total;
    }

    private static Money calculateTotal(List<OrderItem> items) {
        Money total = Money.zero();
        for (OrderItem item : items) {
            total = total.plus(item.subTotal());
        }
        return total;
    }

    public UUID getId() {
        return id;
    }

    public String getCustomerId() {
        return customerId;
    }

    public List<OrderItem> getItems() {
        return Collections.unmodifiableList(items);
    }

    public Money getTotal() {
        return total;
    }
}

