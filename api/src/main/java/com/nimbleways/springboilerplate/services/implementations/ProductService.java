package com.nimbleways.springboilerplate.services.implementations;

import java.time.LocalDate;

import com.nimbleways.springboilerplate.application.out.NotificationPort;
import com.nimbleways.springboilerplate.application.out.ProductRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nimbleways.springboilerplate.entities.Product;
import com.nimbleways.springboilerplate.repositories.ProductRepository;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepositoryPort productRepository;
    private final NotificationPort notificationPort;

    public void notifyDelay(int leadTime, Product p) {
        p.setLeadTime(leadTime);
        productRepository.save(p);
        notificationPort.sendDelayNotification(leadTime,p.getName());
    }

    public void handleSeasonalProduct(Product p) {

        if (LocalDate.now()
                .plusDays(p.getLeadTime())
                .isAfter(p.getSeasonEndDate())) {
            notificationPort.sendOutOfStockNotification(p.getName());
            p.setAvailable(0);
            productRepository.save(p);

        } else if (p.getSeasonStartDate().isAfter(LocalDate.now())) {
            notificationPort.sendOutOfStockNotification(p.getName());
            productRepository.save(p);
        } else {
            notifyDelay(p.getLeadTime(), p);
        }
    }

    public void handleExpiredProduct(Product p) {
        if (p.getAvailable() > 0 && p.getExpiryDate().isAfter(LocalDate.now())) {
            p.setAvailable(p.getAvailable() - 1);
            productRepository.save(p);
        } else {
            notificationPort.sendExpirationNotification(p.getName(), p.getExpiryDate());
            p.setAvailable(0);
            productRepository.save(p);
        }
    }
}