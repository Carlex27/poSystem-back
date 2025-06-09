package com.softeams.poSystem.core.dtos.sales;

import java.math.BigDecimal;

public record SaleItemResponse(
        String sku,
        String nombre,
        Integer cantidad,
        BigDecimal precio
) {
}
