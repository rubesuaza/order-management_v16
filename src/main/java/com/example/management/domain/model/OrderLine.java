package com.example.management.domain.model;

import com.example.management.domain.exception.InvalidOrderLineException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

/**
 * Value object representing a single line in an order.
 * Invariants: quantity > 0, unitPrice >= 0, productId not null/blank.
 */
public final class OrderLine {

    private static final int CURRENCY_SCALE = 2;

    private final String productId;
    private final int quantity;
    private final BigDecimal unitPrice;
    private final BigDecimal lineTotal;

    private OrderLine(String productId, int quantity, BigDecimal unitPrice) {
        this.productId = productId;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.lineTotal = unitPrice.multiply(BigDecimal.valueOf(quantity)).setScale(CURRENCY_SCALE, RoundingMode.HALF_UP);
    }

    public static OrderLine of(String productId, int quantity, BigDecimal unitPrice) {
        if (productId == null || productId.isBlank()) {
            throw new InvalidOrderLineException("Product id cannot be null or blank");
        }
        if (quantity <= 0) {
            throw new InvalidOrderLineException("Quantity must be positive");
        }
        if (unitPrice == null) {
            throw new InvalidOrderLineException("Unit price cannot be null");
        }
        if (unitPrice.compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidOrderLineException("Unit price cannot be negative");
        }
        return new OrderLine(productId, quantity, unitPrice.setScale(CURRENCY_SCALE, RoundingMode.HALF_UP));
    }

    public String getProductId() {
        return productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public BigDecimal getLineTotal() {
        return lineTotal;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderLine orderLine = (OrderLine) o;
        return quantity == orderLine.quantity
                && Objects.equals(productId, orderLine.productId)
                && Objects.equals(unitPrice, orderLine.unitPrice);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productId, quantity, unitPrice);
    }
}
