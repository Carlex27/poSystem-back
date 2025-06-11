package com.softeams.poSystem.core.dtos.inventoryEntry;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record InventorEntryDto(
        Long id,
        int cajasCompradas,
        BigDecimal precioPorCaja,
        LocalDate entryDate
) {
}
