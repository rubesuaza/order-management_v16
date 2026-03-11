package com.example.ordermanagement.application.ports.out;

import com.example.ordermanagement.domain.model.Order;

import java.util.Optional;
import java.util.UUID;

public interface OrderRepository {

    Order save(Order order);

    Optional<Order> findById(UUID id);

    UUID nextIdentity();
}

