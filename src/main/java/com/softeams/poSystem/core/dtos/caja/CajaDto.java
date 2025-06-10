package com.softeams.poSystem.core.dtos.caja;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record CajaDto(
        Long id,
        BigDecimal montoInicial,
        BigDecimal montoFinal,
        LocalDate date
) {
}
