package com.softeams.poSystem.core.dtos;

import com.softeams.poSystem.core.entities.SaleItem;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record SaleRequest(
        String clientName,
        String vendorName,
        LocalDateTime saleDate,
        BigDecimal total,
        String state,
        List<SaleItemDto> items
) {
}
