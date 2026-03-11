package com.example.ordermanagement.domain.model;

import java.math.BigDecimal;
import java.util.Objects;

public final class Money {

    private static final BigDecimal MINIMUM_ORDER_AMOUNT = BigDecimal.TEN;

    private final BigDecimal amount;

    private Money(BigDecimal amount) {
        this.amount = validate(amount);
    }

    public static Money of(BigDecimal amount) {
        return new Money(amount);
    }

    private BigDecimal validate(BigDecimal amount) {
        Objects.requireNonNull(amount, "El monto no puede ser nulo");
        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("El monto no puede ser negativo");
        }
        return amount;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public Money plus(Money other) {
        Objects.requireNonNull(other, "El dinero a sumar no puede ser nulo");
        return new Money(this.amount.add(other.amount));
    }

    public boolean isGreaterOrEqualThanMinimum() {
        return amount.compareTo(MINIMUM_ORDER_AMOUNT) >= 0;
    }

    public static Money zero() {
        return new Money(BigDecimal.ZERO);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Money money = (Money) o;
        return amount.compareTo(money.amount) == 0;
    }

    @Override
    public int hashCode() {
        return amount.stripTrailingZeros().hashCode();
    }

    @Override
    public String toString() {
        return "Money{" +
                "amount=" + amount +
                '}';
    }
}

