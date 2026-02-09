package com.example.management.infrastructure.adapters.out.persistence;

import com.example.management.application.ports.out.OrderRepository;
import com.example.management.domain.model.Order;
import com.example.management.domain.model.OrderLine;
import com.example.management.domain.model.OrderStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Contract tests for any OrderRepository implementation.
 */
@DisplayName("OrderRepository contract")
class OrderRepositoryContractTest {

    private OrderRepository repository;

    @BeforeEach
    void setUp() {
        repository = new InMemoryOrderRepository();
    }

    @Nested
    @DisplayName("save and findById")
    class SaveAndFind {

        @Test
        void save_persists_order_and_findById_returns_it() {
            Order order = Order.create(
                    UUID.randomUUID(),
                    "customer-1",
                    List.of(OrderLine.of("prod-1", 2, BigDecimal.TEN))
            );

            Order saved = repository.save(order);
            assertThat(saved).isSameAs(order);

            Optional<Order> found = repository.findById(order.getId());
            assertThat(found).isPresent().get().isEqualTo(order);
        }

        @Test
        void save_overwrites_existing_order() {
            UUID id = UUID.randomUUID();
            Order order = Order.create(id, "customer-1",
                    List.of(OrderLine.of("prod-1", 1, BigDecimal.ONE)));
            repository.save(order);
            order.confirm();

            repository.save(order);
            Optional<Order> found = repository.findById(id);
            assertThat(found).isPresent();
            assertThat(found.get().getStatus()).isEqualTo(OrderStatus.CONFIRMED);
        }

        @Test
        void findById_returns_empty_for_unknown_id() {
            Optional<Order> found = repository.findById(UUID.randomUUID());
            assertThat(found).isEmpty();
        }
    }

    @Nested
    @DisplayName("findAll")
    class FindAll {

        @Test
        void findAll_returns_empty_when_no_orders() {
            assertThat(repository.findAll()).isEmpty();
        }

        @Test
        void findAll_returns_all_saved_orders() {
            Order a = Order.create(UUID.randomUUID(), "c1", List.of(OrderLine.of("p1", 1, BigDecimal.ONE)));
            Order b = Order.create(UUID.randomUUID(), "c2", List.of(OrderLine.of("p2", 1, BigDecimal.ONE)));
            repository.save(a);
            repository.save(b);

            List<Order> all = repository.findAll();
            assertThat(all).hasSize(2).containsExactlyInAnyOrder(a, b);
        }
    }
}
