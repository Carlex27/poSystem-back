package com.softeams.poSystem.core.dtos.product;

import java.math.BigDecimal;

public record ProductResponse(
        Long id,
        String sku,
        String nombre,
        String departamento,
        BigDecimal precioCosto,
        BigDecimal precioVenta,
        BigDecimal precioMayoreo,
        BigDecimal precioUnidadVenta,
        BigDecimal precioUnidadMayoreo,
        Integer unidadesPorPresentacion,
        BigDecimal stock,
        Integer stockPorUnidad,
        Integer stockMinimo,
        Integer minimoMayoreo,
        String imagePath
) {
}
