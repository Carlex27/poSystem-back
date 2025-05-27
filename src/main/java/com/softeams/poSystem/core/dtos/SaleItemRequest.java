package com.softeams.poSystem.core.dtos;

public record SaleItemRequest(
        Long productId,
        String productName,
        Integer quantity
) {
}
