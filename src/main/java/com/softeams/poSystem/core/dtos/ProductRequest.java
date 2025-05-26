package com.softeams.poSystem.core.dtos;

import java.math.BigDecimal;

public record ProductRequest(
        String nombre,
        String marca,
        BigDecimal gradosAlcohol,
        String tamanio,
        BigDecimal precioNormal,
        BigDecimal precioMayoreo,
        Integer stock
) {
}
