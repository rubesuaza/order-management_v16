package com.example.management.domain.exception;

/**
 * Thrown when an order violates domain invariants (e.g. empty lines, invalid customer).
 */
public class InvalidOrderException extends RuntimeException {

    public InvalidOrderException(String message) {
        super(message);
    }
}
