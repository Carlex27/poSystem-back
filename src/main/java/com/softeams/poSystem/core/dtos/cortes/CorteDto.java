package com.softeams.poSystem.core.dtos.cortes;

import com.softeams.poSystem.core.dtos.abono.AbonoResumenDto;

import java.math.BigDecimal;
import java.util.List;

public record CorteDto(
    BigDecimal ventasEfectivo,
    BigDecimal pagoClientes,
    BigDecimal pagoProveedores,
    List<ResumeDepartamentosDto> ventasPorDepartamento,
    BigDecimal dineroEnCaja,
    BigDecimal ventasTotales,
    BigDecimal gananciaDelDia,
    List<AbonoResumenDto> abonos
) {
}
