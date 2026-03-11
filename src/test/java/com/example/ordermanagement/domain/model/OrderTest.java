package com.example.ordermanagement.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class OrderTest {

    @Test
    @DisplayName("Debe crear una orden válida cuando cumple las reglas de negocio")
    void shouldCreateValidOrder() {
        UUID id = UUID.randomUUID();
        String customerId = "CUSTOMER-123";
        OrderItem item = new OrderItem("PRODUCT-1", 2, Money.of(BigDecimal.valueOf(10)));

        Executable executable = () -> Order.create(id, customerId, List.of(item));

        assertDoesNotThrow(executable);
        Order order = Order.create(id, customerId, List.of(item));
        assertEquals(id, order.getId());
        assertEquals(customerId, order.getCustomerId());
        assertEquals(1, order.getItems().size());
        assertEquals(BigDecimal.valueOf(20), order.getTotal().getAmount());
    }

    @Test
    @DisplayName("Debe fallar cuando el total de la orden es menor al mínimo permitido")
    void shouldFailWhenTotalIsBelowMinimum() {
        UUID id = UUID.randomUUID();
        String customerId = "CUSTOMER-123";
        OrderItem item = new OrderItem("PRODUCT-1", 1, Money.of(BigDecimal.valueOf(5)));

        Executable executable = () -> Order.create(id, customerId, List.of(item));

        assertThrows(IllegalArgumentException.class, executable);
    }

    @Test
    @DisplayName("Debe fallar cuando la orden no tiene ítems")
    void shouldFailWhenOrderHasNoItems() {
        UUID id = UUID.randomUUID();
        String customerId = "CUSTOMER-123";

        Executable executable = () -> Order.create(id, customerId, List.of());

        assertThrows(IllegalArgumentException.class, executable);
    }

    @Test
    @DisplayName("Debe fallar cuando el identificador de cliente es inválido")
    void shouldFailWhenCustomerIdIsInvalid() {
        UUID id = UUID.randomUUID();
        OrderItem item = new OrderItem("PRODUCT-1", 2, Money.of(BigDecimal.valueOf(10)));

        Executable nullCustomerId = () -> Order.create(id, null, List.of(item));
        Executable blankCustomerId = () -> Order.create(id, "   ", List.of(item));

        assertThrows(NullPointerException.class, nullCustomerId);
        assertThrows(IllegalArgumentException.class, blankCustomerId);
    }
}

