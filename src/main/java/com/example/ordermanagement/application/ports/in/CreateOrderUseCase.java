package com.example.ordermanagement.application.ports.in;

import com.example.ordermanagement.domain.model.Order;
import com.example.ordermanagement.domain.model.OrderItem;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

public interface CreateOrderUseCase {

    Order createOrder(CreateOrderCommand command);

    final class CreateOrderCommand {
        private final UUID id;
        private final String customerId;
        private final List<OrderItem> items;

        public CreateOrderCommand(UUID id, String customerId, List<OrderItem> items) {
            this.id = Objects.requireNonNull(id, "El identificador de la orden no puede ser nulo");
            this.customerId = Objects.requireNonNull(customerId, "El identificador de cliente no puede ser nulo");
            this.items = List.copyOf(Objects.requireNonNull(items, "La lista de ítems no puede ser nula"));
        }

        public UUID getId() {
            return id;
        }

        public String getCustomerId() {
            return customerId;
        }

        public List<OrderItem> getItems() {
            return items;
        }
    }
}

