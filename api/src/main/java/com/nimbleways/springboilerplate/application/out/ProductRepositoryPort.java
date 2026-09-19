package com.nimbleways.springboilerplate.application.out;

import com.nimbleways.springboilerplate.entities.Product;

public interface ProductRepositoryPort {
    Product save(Product product);
}
