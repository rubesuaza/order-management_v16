package com.example.management.domain.model;

import com.example.management.domain.exception.InvalidOrderException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Order")
class OrderTest {

    @Nested
    @DisplayName("creation")
    class Creation {
        @Test
        @DisplayName("creates order with lines and total equals sum of line totals")
        void createsOrderWithCorrectTotal() {
            OrderLine line1 = OrderLine.of("p1", 2, new BigDecimal("10.00"));
            OrderLine line2 = OrderLine.of("p2", 1, new BigDecimal("5.50"));
            Order order = Order.create(UUID.randomUUID(), "customer-1", List.of(line1, line2));

            assertEquals(new BigDecimal("25.50"), order.getTotal());
            assertEquals(2, order.getLines().size());
            assertEquals(OrderStatus.DRAFT, order.getStatus());
        }

        @Test
        @DisplayName("throws when lines is null")
        void throwsWhenLinesNull() {
            assertThrows(InvalidOrderException.class,
                    () -> Order.create(UUID.randomUUID(), "customer-1", null));
        }

        @Test
        @DisplayName("throws when lines is empty")
        void throwsWhenLinesEmpty() {
            assertThrows(InvalidOrderException.class,
                    () -> Order.create(UUID.randomUUID(), "customer-1", List.of()));
        }

        @Test
        @DisplayName("throws when customerId is null or blank")
        void throwsWhenCustomerIdInvalid() {
            OrderLine line = OrderLine.of("p1", 1, new BigDecimal("10.00"));
            assertThrows(InvalidOrderException.class,
                    () -> Order.create(UUID.randomUUID(), null, List.of(line)));
            assertThrows(InvalidOrderException.class,
                    () -> Order.create(UUID.randomUUID(), "", List.of(line)));
        }

        @Test
        @DisplayName("throws when id is null")
        void throwsWhenIdNull() {
            OrderLine line = OrderLine.of("p1", 1, new BigDecimal("10.00"));
            assertThrows(InvalidOrderException.class,
                    () -> Order.create(null, "customer-1", List.of(line)));
        }
    }

    @Nested
    @DisplayName("total")
    class Total {
        @Test
        @DisplayName("order total must equal sum of line totals")
        void totalEqualsSumOfLines() {
            OrderLine a = OrderLine.of("p1", 2, new BigDecimal("3.00"));
            OrderLine b = OrderLine.of("p2", 1, new BigDecimal("4.50"));
            Order order = Order.create(UUID.randomUUID(), "c1", List.of(a, b));
            assertEquals(new BigDecimal("10.50"), order.getTotal());
        }

        @Test
        @DisplayName("single line order total equals that line total")
        void singleLineTotal() {
            OrderLine line = OrderLine.of("p1", 5, new BigDecimal("2.00"));
            Order order = Order.create(UUID.randomUUID(), "c1", List.of(line));
            assertEquals(new BigDecimal("10.00"), order.getTotal());
        }
    }

    @Nested
    @DisplayName("status")
    class Status {
        @Test
        @DisplayName("new order has DRAFT status")
        void newOrderIsDraft() {
            Order order = Order.create(UUID.randomUUID(), "c1",
                    List.of(OrderLine.of("p1", 1, new BigDecimal("1.00"))));
            assertEquals(OrderStatus.DRAFT, order.getStatus());
        }

        @Test
        @DisplayName("confirm changes status to CONFIRMED")
        void confirmChangesStatus() {
            Order order = Order.create(UUID.randomUUID(), "c1",
                    List.of(OrderLine.of("p1", 1, new BigDecimal("1.00"))));
            order.confirm();
            assertEquals(OrderStatus.CONFIRMED, order.getStatus());
        }

        @Test
        @DisplayName("ship changes status to SHIPPED")
        void shipChangesStatus() {
            Order order = Order.create(UUID.randomUUID(), "c1",
                    List.of(OrderLine.of("p1", 1, new BigDecimal("1.00"))));
            order.confirm();
            order.ship();
            assertEquals(OrderStatus.SHIPPED, order.getStatus());
        }

        @Test
        @DisplayName("cancel changes status to CANCELLED")
        void cancelChangesStatus() {
            Order order = Order.create(UUID.randomUUID(), "c1",
                    List.of(OrderLine.of("p1", 1, new BigDecimal("1.00"))));
            order.cancel();
            assertEquals(OrderStatus.CANCELLED, order.getStatus());
        }
    }

    @Nested
    @DisplayName("immutability of lines")
    class Immutability {
        @Test
        @DisplayName("getLines returns unmodifiable list")
        void getLinesIsUnmodifiable() {
            Order order = Order.create(UUID.randomUUID(), "c1",
                    List.of(OrderLine.of("p1", 1, new BigDecimal("1.00"))));
            assertThrows(UnsupportedOperationException.class,
                    () -> order.getLines().add(OrderLine.of("p2", 1, new BigDecimal("1.00"))));
        }
    }
}
