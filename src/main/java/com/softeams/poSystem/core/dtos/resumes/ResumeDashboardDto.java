package com.softeams.poSystem.core.dtos.resumes;

import java.math.BigDecimal;
import java.util.List;

public record ResumeDashboardDto(
        Long totalProductos,
        Long totalVentas,
        BigDecimal ingresosTotales,
        Long stockBajo,
        List<SaleDashboard> ventasRecientes
) {
}
