package com.softeams.poSystem.core.dtos;

import java.math.BigDecimal;

public record SaleItemRequest(
        Long productId,
        Integer quantity

) {
}
