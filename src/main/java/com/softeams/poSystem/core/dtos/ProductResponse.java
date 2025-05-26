package com.softeams.poSystem.core.dtos;

import java.math.BigDecimal;

public record ProductResponse(
        Long id,
        String nombre,
        String marca,
        BigDecimal gradosAlcohol,
        String tamanio,
        BigDecimal precioNormal,
        BigDecimal precioMayoreo,
        Integer Stock
) {
}
