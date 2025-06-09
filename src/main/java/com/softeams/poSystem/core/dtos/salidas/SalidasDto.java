package com.softeams.poSystem.core.dtos.salidas;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record SalidasDto(
        Long id,
        String description,
        BigDecimal amount,
        LocalDateTime date
) {
}
