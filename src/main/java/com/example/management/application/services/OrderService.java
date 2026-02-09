package com.example.management.application.services;

import com.example.management.application.ports.in.OrderUseCase;
import com.example.management.application.ports.out.OrderRepository;
import com.example.management.domain.exception.OrderNotFoundException;
import com.example.management.domain.model.Order;
import com.example.management.domain.model.OrderLine;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Application service implementing order use cases.
 */
@Service
public class OrderService implements OrderUseCase {

    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public Order createOrder(UUID id, String customerId, List<CreateOrderLineCommand> lines) {
        List<OrderLine> domainLines = lines.stream()
                .map(c -> OrderLine.of(c.productId(), c.quantity(), c.unitPrice()))
                .toList();
        Order order = Order.create(id, customerId, domainLines);
        return orderRepository.save(order);
    }

    @Override
    public Optional<Order> getOrder(UUID id) {
        return orderRepository.findById(id);
    }

    @Override
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @Override
    public void confirmOrder(UUID id) {
        Order order = orderRepository.findById(id).orElseThrow(() -> new OrderNotFoundException(id));
        order.confirm();
        orderRepository.save(order);
    }

    @Override
    public void shipOrder(UUID id) {
        Order order = orderRepository.findById(id).orElseThrow(() -> new OrderNotFoundException(id));
        order.ship();
        orderRepository.save(order);
    }

    @Override
    public void cancelOrder(UUID id) {
        Order order = orderRepository.findById(id).orElseThrow(() -> new OrderNotFoundException(id));
        order.cancel();
        orderRepository.save(order);
    }
}
