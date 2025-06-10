package com.softeams.poSystem.core.dtos.product;

import java.math.BigDecimal;

public record AltaProduct(
        String sku,
        Integer stock,
        BigDecimal precioCosto,
        String nombre,
        Integer cantidad
) {
}
