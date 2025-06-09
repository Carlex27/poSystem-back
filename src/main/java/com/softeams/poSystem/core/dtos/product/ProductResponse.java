package com.softeams.poSystem.core.dtos.product;

import java.math.BigDecimal;

public record ProductResponse(
        String SKU,
        String nombre,
        BigDecimal precioCosto,
        BigDecimal precioVenta,
        BigDecimal precioMayoreo,
        Integer Stock,
        Integer stockMinimo,
        Integer minimoMayoreo
) {
}
