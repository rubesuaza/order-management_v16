package com.example.management.infrastructure.adapters.in.web;

import com.example.management.application.exception.InvalidOrderApplicationException;
import com.example.management.application.exception.OrderNotFoundApplicationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

/**
 * Maps application-layer exceptions to HTTP responses. Logs exceptions for observability.
 */
@RestControllerAdvice
public class OrderExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(OrderExceptionHandler.class);

    @ExceptionHandler(InvalidOrderApplicationException.class)
    public ResponseEntity<Map<String, String>> handleInvalidOrder(InvalidOrderApplicationException ex) {
        log.warn("Invalid order request: {}", ex.getMessage(), ex);
        return ResponseEntity.badRequest().body(Map.of("error", ex.getMessage()));
    }

    @ExceptionHandler(OrderNotFoundApplicationException.class)
    public ResponseEntity<Map<String, String>> handleOrderNotFound(OrderNotFoundApplicationException ex) {
        log.warn("Order not found: {}", ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", ex.getMessage()));
    }
}
