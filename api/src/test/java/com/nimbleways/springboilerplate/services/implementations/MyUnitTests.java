package com.nimbleways.springboilerplate.services.implementations;

import com.nimbleways.springboilerplate.application.out.NotificationPort;
import com.nimbleways.springboilerplate.application.out.ProductRepositoryPort;
import com.nimbleways.springboilerplate.entities.Product;
import com.nimbleways.springboilerplate.utils.Annotations.UnitTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@UnitTest
public class MyUnitTests {

    @Mock
    private NotificationPort notificationPort;

    @Mock
    private ProductRepositoryPort productRepository;

    @InjectMocks
    private ProductService productService;

    @Test
    public void test() {
        // GIVEN
        Product product = new Product(
                null,
                15,
                0,
                "NORMAL",
                "RJ45 Cable",
                null,
                null,
                null
        );

        Mockito.when(productRepository.save(product)).thenReturn(product);

        // WHEN
        productService.notifyDelay(product.getLeadTime(), product);

        // THEN
        assertEquals(0, product.getAvailable());
        assertEquals(15, product.getLeadTime());

        Mockito.verify(productRepository, Mockito.times(1))
                .save(product);

        Mockito.verify(notificationPort, Mockito.times(1))
                .sendDelayNotification(
                        product.getLeadTime(),
                        product.getName()
                );
    }
}