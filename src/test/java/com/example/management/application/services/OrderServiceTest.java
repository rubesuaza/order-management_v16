package com.example.management.application.services;

import com.example.management.application.exception.InvalidOrderApplicationException;
import com.example.management.application.exception.OrderNotFoundApplicationException;
import com.example.management.application.ports.in.OrderOutputDto;
import com.example.management.application.ports.in.OrderUseCase;
import com.example.management.application.ports.out.OrderRepository;
import com.example.management.domain.model.Order;
import com.example.management.domain.model.OrderLine;
import com.example.management.domain.model.OrderStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("OrderService")
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    private OrderService orderService;

    @BeforeEach
    void setUp() {
        orderService = new OrderService(orderRepository);
    }

    @Nested
    @DisplayName("createOrder")
    class CreateOrder {

        @Test
        @DisplayName("saves and returns order when valid")
        void createsAndSavesOrder() {
            UUID id = UUID.randomUUID();
            String customerId = "customer-1";
            List<OrderUseCase.CreateOrderLineCommand> lines = List.of(
                    new OrderUseCase.CreateOrderLineCommand("prod-1", 2, new BigDecimal("10.00")),
                    new OrderUseCase.CreateOrderLineCommand("prod-2", 1, new BigDecimal("5.50"))
            );

            Order savedOrder = Order.create(
                    id,
                    customerId,
                    List.of(
                            OrderLine.of("prod-1", 2, new BigDecimal("10.00")),
                            OrderLine.of("prod-2", 1, new BigDecimal("5.50"))
                    )
            );
            when(orderRepository.save(any(Order.class))).thenAnswer(inv -> inv.getArgument(0));

            OrderOutputDto result = orderService.createOrder(id, customerId, lines);

            assertThat(result).isNotNull();
            assertThat(result.id()).isEqualTo(id);
            assertThat(result.customerId()).isEqualTo(customerId);
            assertThat(result.total()).isEqualByComparingTo(new BigDecimal("25.50"));
            assertThat(result.status()).isEqualTo(OrderStatus.DRAFT.name());

            ArgumentCaptor<Order> captor = ArgumentCaptor.forClass(Order.class);
            verify(orderRepository).save(captor.capture());
            assertThat(captor.getValue().getTotal()).isEqualByComparingTo(new BigDecimal("25.50"));
        }

        @Test
        @DisplayName("throws InvalidOrderApplicationException when line has invalid data")
        void throwsWhenLineInvalid() {
            UUID id = UUID.randomUUID();
            List<OrderUseCase.CreateOrderLineCommand> lines = List.of(
                    new OrderUseCase.CreateOrderLineCommand("prod-1", -1, new BigDecimal("10.00"))
            );

            assertThatThrownBy(() -> orderService.createOrder(id, "customer-1", lines))
                    .isInstanceOf(InvalidOrderApplicationException.class);
            // save is never called because exception is thrown during line mapping
        }

        @Test
        @DisplayName("throws InvalidOrderApplicationException when customerId blank")
        void throwsWhenCustomerIdBlank() {
            UUID id = UUID.randomUUID();
            List<OrderUseCase.CreateOrderLineCommand> lines = List.of(
                    new OrderUseCase.CreateOrderLineCommand("prod-1", 1, new BigDecimal("10.00"))
            );

            assertThatThrownBy(() -> orderService.createOrder(id, "", lines))
                    .isInstanceOf(InvalidOrderApplicationException.class);
        }

        @Test
        @DisplayName("throws InvalidOrderApplicationException when lines empty")
        void throwsWhenLinesEmpty() {
            UUID id = UUID.randomUUID();

            assertThatThrownBy(() -> orderService.createOrder(id, "customer-1", List.of()))
                    .isInstanceOf(InvalidOrderApplicationException.class);
        }
    }

    @Nested
    @DisplayName("getOrder")
    class GetOrder {

        @Test
        @DisplayName("returns order when found")
        void returnsOrderWhenFound() {
            UUID id = UUID.randomUUID();
            Order order = Order.create(id, "c1", List.of(OrderLine.of("p1", 1, BigDecimal.ONE)));
            when(orderRepository.findById(id)).thenReturn(Optional.of(order));

            Optional<OrderOutputDto> result = orderService.getOrder(id);

            assertThat(result).isPresent();
            assertThat(result.get().id()).isEqualTo(id);
            assertThat(result.get().customerId()).isEqualTo("c1");
            assertThat(result.get().total()).isEqualByComparingTo(BigDecimal.ONE);
            verify(orderRepository).findById(id);
        }

        @Test
        @DisplayName("returns empty when not found")
        void returnsEmptyWhenNotFound() {
            UUID id = UUID.randomUUID();
            when(orderRepository.findById(id)).thenReturn(Optional.empty());

            Optional<OrderOutputDto> result = orderService.getOrder(id);

            assertThat(result).isEmpty();
            verify(orderRepository).findById(id);
        }
    }

    @Nested
    @DisplayName("getAllOrders")
    class GetAllOrders {

        @Test
        @DisplayName("returns all orders from repository")
        void returnsAllOrders() {
            Order a = Order.create(UUID.randomUUID(), "c1", List.of(OrderLine.of("p1", 1, BigDecimal.ONE)));
            Order b = Order.create(UUID.randomUUID(), "c2", List.of(OrderLine.of("p2", 1, BigDecimal.ONE)));
            when(orderRepository.findAll()).thenReturn(List.of(a, b));

            List<OrderOutputDto> result = orderService.getAllOrders();

            assertThat(result).hasSize(2);
            assertThat(result.get(0).customerId()).isEqualTo("c1");
            assertThat(result.get(1).customerId()).isEqualTo("c2");
            verify(orderRepository).findAll();
        }

        @Test
        @DisplayName("returns empty list when no orders")
        void returnsEmptyWhenNoOrders() {
            when(orderRepository.findAll()).thenReturn(List.of());

            List<OrderOutputDto> result = orderService.getAllOrders();

            assertThat(result).isEmpty();
            verify(orderRepository).findAll();
        }
    }

    @Nested
    @DisplayName("confirmOrder")
    class ConfirmOrder {

        @Test
        @DisplayName("confirms and saves order when found")
        void confirmsAndSavesOrder() {
            UUID id = UUID.randomUUID();
            Order order = Order.create(id, "c1", List.of(OrderLine.of("p1", 1, BigDecimal.ONE)));
            when(orderRepository.findById(id)).thenReturn(Optional.of(order));
            when(orderRepository.save(any(Order.class))).thenAnswer(inv -> inv.getArgument(0));

            orderService.confirmOrder(id);

            assertThat(order.getStatus()).isEqualTo(OrderStatus.CONFIRMED);
            verify(orderRepository).findById(id);
            verify(orderRepository).save(order);
        }

        @Test
        @DisplayName("throws OrderNotFoundApplicationException when order not found")
        void throwsWhenNotFound() {
            UUID id = UUID.randomUUID();
            when(orderRepository.findById(id)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> orderService.confirmOrder(id))
                    .isInstanceOf(OrderNotFoundApplicationException.class)
                    .hasMessageContaining(id.toString());
            verify(orderRepository).findById(id);
        }
    }

    @Nested
    @DisplayName("shipOrder")
    class ShipOrder {

        @Test
        @DisplayName("ships and saves order when found")
        void shipsAndSavesOrder() {
            UUID id = UUID.randomUUID();
            Order order = Order.create(id, "c1", List.of(OrderLine.of("p1", 1, BigDecimal.ONE)));
            order.confirm();
            when(orderRepository.findById(id)).thenReturn(Optional.of(order));
            when(orderRepository.save(any(Order.class))).thenAnswer(inv -> inv.getArgument(0));

            orderService.shipOrder(id);

            assertThat(order.getStatus()).isEqualTo(OrderStatus.SHIPPED);
            verify(orderRepository).findById(id);
            verify(orderRepository).save(order);
        }

        @Test
        @DisplayName("throws OrderNotFoundApplicationException when order not found")
        void throwsWhenNotFound() {
            UUID id = UUID.randomUUID();
            when(orderRepository.findById(id)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> orderService.shipOrder(id))
                    .isInstanceOf(OrderNotFoundApplicationException.class)
                    .hasMessageContaining(id.toString());
            verify(orderRepository).findById(id);
        }
    }

    @Nested
    @DisplayName("cancelOrder")
    class CancelOrder {

        @Test
        @DisplayName("cancels and saves order when found")
        void cancelsAndSavesOrder() {
            UUID id = UUID.randomUUID();
            Order order = Order.create(id, "c1", List.of(OrderLine.of("p1", 1, BigDecimal.ONE)));
            when(orderRepository.findById(id)).thenReturn(Optional.of(order));
            when(orderRepository.save(any(Order.class))).thenAnswer(inv -> inv.getArgument(0));

            orderService.cancelOrder(id);

            assertThat(order.getStatus()).isEqualTo(OrderStatus.CANCELLED);
            verify(orderRepository).findById(id);
            verify(orderRepository).save(order);
        }

        @Test
        @DisplayName("throws OrderNotFoundApplicationException when order not found")
        void throwsWhenNotFound() {
            UUID id = UUID.randomUUID();
            when(orderRepository.findById(id)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> orderService.cancelOrder(id))
                    .isInstanceOf(OrderNotFoundApplicationException.class)
                    .hasMessageContaining(id.toString());
            verify(orderRepository).findById(id);
        }
    }
}
