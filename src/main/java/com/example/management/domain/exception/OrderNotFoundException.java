package com.example.management.domain.exception;

import java.util.UUID;

/**
 * Thrown when an order is not found by id.
 */
public class OrderNotFoundException extends RuntimeException {

    public OrderNotFoundException(UUID id) {
        super("Order not found: " + id);
    }
}
