package com.example.ordermanagement.domain.model;

import java.math.BigDecimal;
import java.util.Objects;

public final class OrderItem {

    private final String productId;
    private final int quantity;
    private final Money unitPrice;

    public OrderItem(String productId, int quantity, Money unitPrice) {
        this.productId = Objects.requireNonNull(productId, "El identificador de producto no puede ser nulo");
        this.unitPrice = Objects.requireNonNull(unitPrice, "El precio unitario no puede ser nulo");
        if (productId.isBlank()) {
            throw new IllegalArgumentException("El identificador de producto no puede estar vacío");
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor que cero");
        }
        this.quantity = quantity;
    }

    public String getProductId() {
        return productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public Money getUnitPrice() {
        return unitPrice;
    }

    public Money subTotal() {
        return Money.of(unitPrice.getAmount().multiply(BigDecimal.valueOf(quantity)));
    }
}

