package com.softeams.poSystem.core.dtos.resumes;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record SaleDashboard(
        String nombreCliente,
        LocalDateTime fechaVenta,
        BigDecimal totalVenta,
        Long cantidadProductos
) {
}
