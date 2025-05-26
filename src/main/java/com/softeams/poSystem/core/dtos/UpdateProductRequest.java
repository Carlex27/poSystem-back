package com.softeams.poSystem.core.dtos;

import jakarta.persistence.Column;

import java.math.BigDecimal;

public record UpdateProductRequest(
        String nombre,
        String marca,
        BigDecimal gradosAlcohol,
        String tamanio,
        BigDecimal precioNormal,
        BigDecimal precioMayoreo,
        Integer Stock
) {
}
