package com.nimbleways.springboilerplate.controllers;

import com.nimbleways.springboilerplate.application.in.ProcessOrderUseCase;
import com.nimbleways.springboilerplate.contollers.OrderController;
import com.nimbleways.springboilerplate.dto.product.ProcessOrderResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class) class OrderControllerTest {
    @Mock private ProcessOrderUseCase processOrderUseCase;
    private OrderController controller;
    @BeforeEach void setUp() {
        controller = new OrderController(processOrderUseCase);
    }
    @Test void shouldProcessOrder() {
        ProcessOrderResponse expectedResponse = new ProcessOrderResponse(10L);
        when(processOrderUseCase.processOrder(10L)).thenReturn(expectedResponse);
        ProcessOrderResponse response = controller.processOrder(10L);
        assertEquals(10L, response.id());
        verify(processOrderUseCase).processOrder(10L);
    }
}