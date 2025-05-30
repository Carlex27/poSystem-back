package com.softeams.poSystem.core.dtos;

import java.util.List;

public record ResumeDashboardDto(
        Integer totalProductos,
        Integer totalVentas,
        Integer ingresosTotales,
        Integer stockBajo,
        List<SaleResponse> ventasRecientes
) {
}
