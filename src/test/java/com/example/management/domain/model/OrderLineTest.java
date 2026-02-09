package com.example.management.domain.model;

import com.example.management.domain.exception.InvalidOrderLineException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("OrderLine")
class OrderLineTest {

    @Nested
    @DisplayName("creation")
    class Creation {
        @Test
        @DisplayName("creates valid line with positive quantity and non-negative price")
        void createsValidLine() {
            OrderLine line = OrderLine.of("product-1", 2, new BigDecimal("10.50"));
            assertEquals("product-1", line.getProductId());
            assertEquals(2, line.getQuantity());
            assertEquals(new BigDecimal("10.50"), line.getUnitPrice());
            assertEquals(new BigDecimal("21.00"), line.getLineTotal());
        }

        @Test
        @DisplayName("throws when quantity is zero")
        void throwsWhenQuantityZero() {
            assertThrows(InvalidOrderLineException.class,
                    () -> OrderLine.of("product-1", 0, new BigDecimal("10.00")));
        }

        @Test
        @DisplayName("throws when quantity is negative")
        void throwsWhenQuantityNegative() {
            assertThrows(InvalidOrderLineException.class,
                    () -> OrderLine.of("product-1", -1, new BigDecimal("10.00")));
        }

        @Test
        @DisplayName("throws when unit price is negative")
        void throwsWhenUnitPriceNegative() {
            assertThrows(InvalidOrderLineException.class,
                    () -> OrderLine.of("product-1", 1, new BigDecimal("-5.00")));
        }

        @Test
        @DisplayName("throws when productId is null or blank")
        void throwsWhenProductIdInvalid() {
            assertThrows(InvalidOrderLineException.class,
                    () -> OrderLine.of(null, 1, new BigDecimal("10.00")));
            assertThrows(InvalidOrderLineException.class,
                    () -> OrderLine.of("", 1, new BigDecimal("10.00")));
        }

        @Test
        @DisplayName("throws when unitPrice is null")
        void throwsWhenUnitPriceNull() {
            assertThrows(InvalidOrderLineException.class,
                    () -> OrderLine.of("product-1", 1, null));
        }
    }

    @Nested
    @DisplayName("line total")
    class LineTotal {
        @Test
        @DisplayName("line total is quantity times unit price")
        void lineTotalIsQuantityTimesUnitPrice() {
            OrderLine line = OrderLine.of("p1", 3, new BigDecimal("2.50"));
            assertEquals(new BigDecimal("7.50"), line.getLineTotal());
        }

        @Test
        @DisplayName("line total uses scale 2 for currency")
        void lineTotalScaleTwo() {
            OrderLine line = OrderLine.of("p1", 3, new BigDecimal("3.33"));
            assertEquals(2, line.getLineTotal().scale());
            assertEquals(new BigDecimal("9.99"), line.getLineTotal());
        }
    }
}
