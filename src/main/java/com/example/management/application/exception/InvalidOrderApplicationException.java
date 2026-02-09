package com.example.management.application.exception;

/**
 * Application-level exception for invalid order or order line input. Replaces domain InvalidOrderException
 * and InvalidOrderLineException at the port boundary.
 */
public class InvalidOrderApplicationException extends ApplicationException {

    public InvalidOrderApplicationException(String message) {
        super(message);
    }

    public InvalidOrderApplicationException(String message, Throwable cause) {
        super(message, cause);
    }
}
