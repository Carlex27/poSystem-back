package com.softeams.poSystem.core.mappers;

import com.softeams.poSystem.core.dtos.ResumeDashboardDto;
import com.softeams.poSystem.core.dtos.ResumeVentasDto;
import com.softeams.poSystem.core.entities.Sale;

import java.math.BigDecimal;
import java.util.List;

public interface ISaleMapper {
    ResumeDashboardDto toResumeDashboardDto(
            Long totalProductos,
            Long totalVentas,
            BigDecimal ingresosTotales,
            Long stockBajo,
            List<Sale> ventasRecientes
    );
    ResumeVentasDto toResumeVentasDto(Long totalVentas, BigDecimal totalAmount);
}
