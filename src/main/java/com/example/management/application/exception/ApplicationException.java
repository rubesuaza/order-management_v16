package com.example.management.application.exception;

/**
 * Base exception for application-layer errors. Infrastructure handles these for HTTP mapping.
 */
public class ApplicationException extends RuntimeException {

    public ApplicationException(String message) {
        super(message);
    }

    public ApplicationException(String message, Throwable cause) {
        super(message, cause);
    }
}
