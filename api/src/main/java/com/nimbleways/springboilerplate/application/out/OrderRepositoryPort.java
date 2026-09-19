package com.nimbleways.springboilerplate.application.out;

import com.nimbleways.springboilerplate.entities.Order;

import java.util.Optional;

public interface OrderRepositoryPort {
    Optional<Order> findById(Long orderId);
}
