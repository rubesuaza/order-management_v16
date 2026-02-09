package com.example.management.infrastructure.adapters.in.web;

import com.example.management.domain.exception.InvalidOrderException;
import com.example.management.domain.exception.InvalidOrderLineException;
import com.example.management.domain.exception.OrderNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

/**
 * Maps domain exceptions to HTTP responses.
 */
@RestControllerAdvice
public class OrderExceptionHandler {

    @ExceptionHandler(InvalidOrderException.class)
    public ResponseEntity<Map<String, String>> handleInvalidOrder(InvalidOrderException ex) {
        return ResponseEntity.badRequest().body(Map.of("error", ex.getMessage()));
    }

    @ExceptionHandler(InvalidOrderLineException.class)
    public ResponseEntity<Map<String, String>> handleInvalidOrderLine(InvalidOrderLineException ex) {
        return ResponseEntity.badRequest().body(Map.of("error", ex.getMessage()));
    }

    @ExceptionHandler(OrderNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleOrderNotFound(OrderNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", ex.getMessage()));
    }
}
