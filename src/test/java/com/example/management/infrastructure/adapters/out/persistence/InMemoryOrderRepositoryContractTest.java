package com.example.management.infrastructure.adapters.out.persistence;

import com.example.management.application.ports.out.OrderRepository;
import com.example.management.application.ports.out.AbstractOrderRepositoryContractTest;
import org.junit.jupiter.api.DisplayName;

@DisplayName("InMemoryOrderRepository contract")
class InMemoryOrderRepositoryContractTest extends AbstractOrderRepositoryContractTest {

    @Override
    protected OrderRepository createRepository() {
        return new InMemoryOrderRepository();
    }
}
