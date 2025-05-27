package com.softeams.poSystem.core.dtos;

import java.math.BigDecimal;

public record ProductResponse(
        String SKU,
        String nombre,
        String marca,
        BigDecimal gradosAlcohol,
        String tamanio,
        BigDecimal precioNormal,
        BigDecimal precioMayoreo,
        Integer Stock
) {
}
