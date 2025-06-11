package com.softeams.poSystem.core.dtos.client;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ClientDto(
        Long id,
        String name,
        String direction,
        String phoneNumber,
        BigDecimal balance,
        BigDecimal creditLimit,
        LocalDateTime lastAbonoDate
) {
}
