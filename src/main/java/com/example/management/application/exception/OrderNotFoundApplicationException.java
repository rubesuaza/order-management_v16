package com.example.management.application.exception;

import java.util.UUID;

/**
 * Application-level exception when an order is not found. Replaces domain OrderNotFoundException at the port boundary.
 */
public class OrderNotFoundApplicationException extends ApplicationException {

    public OrderNotFoundApplicationException(UUID id) {
        super("Order not found: " + id);
    }

    public OrderNotFoundApplicationException(String message, Throwable cause) {
        super(message, cause);
    }
}
