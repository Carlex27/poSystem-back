package com.softeams.poSystem.core.dtos;

import java.math.BigDecimal;

public record ResumeVentasDto(
        Integer totalVentas,
        BigDecimal totalIngresos
) {
}
