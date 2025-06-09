package com.softeams.poSystem.core.dtos.resumes;

import java.math.BigDecimal;

public record ResumeVentasDto(
        Long totalVentas,
        BigDecimal totalIngresos
) {
}
