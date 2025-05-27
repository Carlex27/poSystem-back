package com.softeams.poSystem.core.dtos;

import java.math.BigDecimal;

public record SaleItemRequest(
        Long productId,
        String productName,
        Integer quantity,
        BigDecimal price
) {
}
