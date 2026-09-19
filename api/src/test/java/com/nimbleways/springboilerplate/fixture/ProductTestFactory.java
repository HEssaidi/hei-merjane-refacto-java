package com.nimbleways.springboilerplate.fixture;

import com.nimbleways.springboilerplate.entities.Product;

import java.time.LocalDate;

public final class ProductTestFactory {

    private ProductTestFactory() {
    }

    public static Product normalProduct(int available, int leadTime) {
        return product(
                "NORMAL",
                available,
                leadTime,
                "Normal product"
        );
    }

    public static Product seasonalProduct(
            int available,
            int leadTime,
            LocalDate seasonStart,
            LocalDate seasonEnd
    ) {
        Product product = product(
                "SEASONAL",
                available,
                leadTime,
                "Seasonal product"
        );

        product.setSeasonStartDate(seasonStart);
        product.setSeasonEndDate(seasonEnd);

        return product;
    }

    public static Product expirableProduct(
            int available,
            LocalDate expiryDate
    ) {
        Product product = product(
                "EXPIRABLE",
                available,
                0,
                "Expirable product"
        );

        product.setExpiryDate(expiryDate);

        return product;
    }

    private static Product product(
            String type,
            int available,
            int leadTime,
            String name
    ) {
        Product product = new Product();
        product.setId(1L);
        product.setType(type);
        product.setAvailable(available);
        product.setLeadTime(leadTime);
        product.setName(name);

        return product;
    }
}
