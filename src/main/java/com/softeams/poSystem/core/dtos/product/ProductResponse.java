package com.softeams.poSystem.core.dtos.product;

import java.math.BigDecimal;

public record ProductResponse(
        String SKU,
        String nombre,
        String departamento,
        BigDecimal precioCosto,
        BigDecimal precioVenta,
        BigDecimal precioMayoreo,
        BigDecimal precioUnidadVenta,
        BigDecimal precioUnidadMayoreo,
        BigDecimal Stock,
        Integer stockMinimo,
        Integer minimoMayoreo
) {
}
