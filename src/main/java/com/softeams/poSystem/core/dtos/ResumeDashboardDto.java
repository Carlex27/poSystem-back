package com.softeams.poSystem.core.dtos;

import java.math.BigDecimal;
import java.util.List;

public record ResumeDashboardDto(
        Integer totalProductos,
        Integer totalVentas,
        BigDecimal ingresosTotales,
        Integer stockBajo,
        List<SaleResponse> ventasRecientes
) {
}
