package com.nimbleways.springboilerplate.application.service;

import com.nimbleways.springboilerplate.application.in.ProcessOrderUseCase;
import com.nimbleways.springboilerplate.application.out.OrderRepositoryPort;
import com.nimbleways.springboilerplate.application.out.ProductRepositoryPort;
import com.nimbleways.springboilerplate.dto.product.ProcessOrderResponse;
import com.nimbleways.springboilerplate.entities.Order;
import com.nimbleways.springboilerplate.entities.Product;
import com.nimbleways.springboilerplate.services.implementations.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class OrderService implements ProcessOrderUseCase {

    private final OrderRepositoryPort orderRepositoryPort;
    private final ProductRepositoryPort productRepositoryPort;
    private final ProductService productService;

    @Override
    public ProcessOrderResponse processOrder(Long orderId) {

        Order order = orderRepositoryPort.findById(orderId).orElseThrow();

        for (Product product : order.getItems()) {
            processProduct(product);
        }

        return new ProcessOrderResponse(order.getId());
    }

    private void processProduct(Product product) {

        if ("NORMAL".equals(product.getType())) {

            if (product.getAvailable() > 0) {
                product.setAvailable(product.getAvailable() - 1);
                productRepositoryPort.save(product);
            } else if (product.getLeadTime() > 0) {
                productService.notifyDelay(product.getLeadTime(), product);
            }

        } else if ("SEASONAL".equals(product.getType())) {

            if (LocalDate.now().isAfter(product.getSeasonStartDate())
                    && LocalDate.now().isBefore(product.getSeasonEndDate())
                    && product.getAvailable() > 0) {

                product.setAvailable(product.getAvailable() - 1);
                productRepositoryPort.save(product);

            } else {
                productService.handleSeasonalProduct(product);
            }

        } else if ("EXPIRABLE".equals(product.getType())) {

            if (product.getAvailable() > 0
                    && product.getExpiryDate().isAfter(LocalDate.now())) {

                product.setAvailable(product.getAvailable() - 1);
                productRepositoryPort.save(product);

            } else {
                productService.handleExpiredProduct(product);
            }
        }
    }
}
