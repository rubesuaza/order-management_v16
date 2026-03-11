package com.example.ordermanagement.infrastructure.persistence;

import com.example.ordermanagement.application.ports.out.OrderRepository;
import com.example.ordermanagement.domain.model.Order;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryOrderRepository implements OrderRepository {

    private final Map<UUID, Order> storage = new ConcurrentHashMap<>();

    @Override
    public Order save(Order order) {
        storage.put(order.getId(), order);
        return order;
    }

    @Override
    public Optional<Order> findById(UUID id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public UUID nextIdentity() {
        return UUID.randomUUID();
    }
}

