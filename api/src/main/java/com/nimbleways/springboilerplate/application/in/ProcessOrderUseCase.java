package com.nimbleways.springboilerplate.application.in;

import com.nimbleways.springboilerplate.dto.product.ProcessOrderResponse;

public interface ProcessOrderUseCase {
    ProcessOrderResponse processOrder(Long orderId);
}
