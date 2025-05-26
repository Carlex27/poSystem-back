package com.softeams.poSystem.core.dtos;

public record SaleItemDto(
        Long productId,
        String productName,
        Integer quantity
) {
}
