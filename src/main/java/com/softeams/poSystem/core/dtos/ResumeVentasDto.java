package com.softeams.poSystem.core.dtos;

import java.math.BigDecimal;

public record ResumeVentasDto(
        Long totalVentas,
        BigDecimal totalIngresos
) {
}
