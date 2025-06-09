package com.softeams.poSystem.core.dtos.sales;

import java.math.BigDecimal;

public record SaleItemResponse(
        String sku,
        String nombre,
        String marca,
        Integer cantidad,
        BigDecimal precio
) {
}
