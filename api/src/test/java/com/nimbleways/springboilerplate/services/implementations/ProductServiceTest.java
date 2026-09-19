package com.nimbleways.springboilerplate.services.implementations;

import com.nimbleways.springboilerplate.application.out.NotificationPort;
import com.nimbleways.springboilerplate.application.out.ProductRepositoryPort;
import com.nimbleways.springboilerplate.entities.Product;
import com.nimbleways.springboilerplate.fixture.ProductTestFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepositoryPort productRepository;

    @Mock
    private NotificationPort notificationPort;

    private ProductService productService;

    @BeforeEach
    void setUp() {
        productService = new ProductService(
                productRepository,
                notificationPort
        );
    }

    @Test
    void shouldNotifyDelay() {
        Product product = ProductTestFactory.normalProduct(0, 5);

        productService.notifyDelay(5, product);

        assertEquals(5, product.getLeadTime());

        verify(productRepository).save(product);
        verify(notificationPort)
                .sendDelayNotification(5, product.getName());
    }

    @Test
    void shouldNotifySeasonalProductAsOutOfStockWhenLeadTimeExceedsSeason() {
        LocalDate today = LocalDate.now();

        Product product = ProductTestFactory.seasonalProduct(
                0,
                10,
                today.minusDays(10),
                today.plusDays(5)
        );

        productService.handleSeasonalProduct(product);

        verify(notificationPort)
                .sendOutOfStockNotification(product.getName());

        assertEquals(0, product.getAvailable());

        verify(productRepository).save(product);
    }

    @Test
    void shouldNotifySeasonalProductAsOutOfStockWhenSeasonHasNotStarted() {
        LocalDate today = LocalDate.now();

        Product product = ProductTestFactory.seasonalProduct(
                0,
                5,
                today.plusDays(3),
                today.plusDays(10)
        );

        productService.handleSeasonalProduct(product);

        verify(notificationPort)
                .sendOutOfStockNotification(product.getName());

        verify(productRepository).save(product);
    }

    @Test
    void shouldNotifyDelayForSeasonalProductWhenLeadTimeFitsWithinSeason() {
        LocalDate today = LocalDate.now();

        Product product = ProductTestFactory.seasonalProduct(
                0,
                2,
                today.minusDays(5),
                today.plusDays(10)
        );

        productService.handleSeasonalProduct(product);

        verify(notificationPort)
                .sendDelayNotification(2, product.getName());

        verify(productRepository).save(product);
    }

    @Test
    void shouldDecreaseStockForValidExpirableProduct() {
        Product product = ProductTestFactory.expirableProduct(
                5,
                LocalDate.now().plusDays(10)
        );

        productService.handleExpiredProduct(product);

        assertEquals(4, product.getAvailable());

        verify(productRepository).save(product);
        verifyNoInteractions(notificationPort);
    }

    @Test
    void shouldNotifyExpirationWhenProductHasExpired() {
        Product product = ProductTestFactory.expirableProduct(
                5,
                LocalDate.now().minusDays(1)
        );

        productService.handleExpiredProduct(product);

        assertEquals(0, product.getAvailable());

        verify(notificationPort)
                .sendExpirationNotification(
                        product.getName(),
                        product.getExpiryDate()
                );

        verify(productRepository).save(product);
    }

    @Test
    void shouldNotifyExpirationWhenExpiredProductHasNoStock() {
        Product product = ProductTestFactory.expirableProduct(
                0,
                LocalDate.now().minusDays(1)
        );

        productService.handleExpiredProduct(product);

        assertEquals(0, product.getAvailable());

        verify(notificationPort)
                .sendExpirationNotification(
                        product.getName(),
                        product.getExpiryDate()
                );

        verify(productRepository).save(product);
    }
}