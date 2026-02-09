package com.example.management.application.services;

import com.example.management.application.exception.InvalidOrderApplicationException;
import com.example.management.application.exception.OrderNotFoundApplicationException;
import com.example.management.application.ports.in.OrderLineOutputDto;
import com.example.management.application.ports.in.OrderOutputDto;
import com.example.management.application.ports.in.OrderUseCase;
import com.example.management.application.ports.in.OrderUseCase.CreateOrderLineCommand;
import com.example.management.application.ports.out.OrderRepository;
import com.example.management.domain.exception.InvalidOrderException;
import com.example.management.domain.exception.InvalidOrderLineException;
import com.example.management.domain.exception.OrderNotFoundException;
import com.example.management.domain.model.Order;
import com.example.management.domain.model.OrderLine;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Application service implementing order use cases. Plain Java class; wiring is done in infrastructure.
 */
public class OrderService implements OrderUseCase {

    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public OrderOutputDto createOrder(UUID id, String customerId, List<CreateOrderLineCommand> lines) {
        try {
            List<OrderLine> domainLines = lines.stream()
                    .map(c -> OrderLine.of(c.productId(), c.quantity(), c.unitPrice()))
                    .toList();
            Order order = Order.create(id, customerId, domainLines);
            Order saved = orderRepository.save(order);
            return toOutputDto(saved);
        } catch (InvalidOrderException | InvalidOrderLineException e) {
            throw new InvalidOrderApplicationException(e.getMessage(), e);
        }
    }

    @Override
    public Optional<OrderOutputDto> getOrder(UUID id) {
        return orderRepository.findById(id).map(this::toOutputDto);
    }

    @Override
    public List<OrderOutputDto> getAllOrders() {
        return orderRepository.findAll().stream().map(this::toOutputDto).toList();
    }

    @Override
    public void confirmOrder(UUID id) {
        Order order = findOrThrow(id);
        order.confirm();
        orderRepository.save(order);
    }

    @Override
    public void shipOrder(UUID id) {
        Order order = findOrThrow(id);
        order.ship();
        orderRepository.save(order);
    }

    @Override
    public void cancelOrder(UUID id) {
        Order order = findOrThrow(id);
        order.cancel();
        orderRepository.save(order);
    }

    private Order findOrThrow(UUID id) {
        try {
            return orderRepository.findById(id).orElseThrow(() -> new OrderNotFoundException(id));
        } catch (OrderNotFoundException e) {
            throw new OrderNotFoundApplicationException(e.getMessage(), e);
        }
    }

    private OrderOutputDto toOutputDto(Order order) {
        List<OrderLineOutputDto> lineDtos = order.getLines().stream()
                .map(l -> new OrderLineOutputDto(
                        l.getProductId(),
                        l.getQuantity(),
                        l.getUnitPrice(),
                        l.getLineTotal()))
                .toList();
        return new OrderOutputDto(
                order.getId(),
                order.getCustomerId(),
                order.getStatus().name(),
                lineDtos,
                order.getTotal()
        );
    }
}
