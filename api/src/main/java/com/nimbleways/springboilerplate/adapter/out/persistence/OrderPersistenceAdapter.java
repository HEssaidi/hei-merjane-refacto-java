package com.nimbleways.springboilerplate.adapter.out.persistence;

import com.nimbleways.springboilerplate.application.out.OrderRepositoryPort;
import com.nimbleways.springboilerplate.entities.Order;
import com.nimbleways.springboilerplate.repositories.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class OrderPersistenceAdapter implements OrderRepositoryPort {

    private final OrderRepository orderRepository;

    @Override
    public Optional<Order> findById(Long orderId) {
        return orderRepository.findById(orderId);
    }
}
