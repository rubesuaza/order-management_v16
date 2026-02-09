package com.example.management.domain.exception;

/**
 * Thrown when an order line violates domain invariants (e.g. negative quantity or price).
 */
public class InvalidOrderLineException extends RuntimeException {

    public InvalidOrderLineException(String message) {
        super(message);
    }
}
