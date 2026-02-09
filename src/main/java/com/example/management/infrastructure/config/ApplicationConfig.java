package com.example.management.infrastructure.config;

import com.example.management.application.ports.in.OrderUseCase;
import com.example.management.application.ports.out.OrderRepository;
import com.example.management.application.services.OrderService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Wires application-layer components as Spring beans. Keeps the application layer free of framework annotations.
 */
@Configuration
public class ApplicationConfig {

    @Bean
    public OrderUseCase orderUseCase(OrderRepository orderRepository) {
        return new OrderService(orderRepository);
    }
}
