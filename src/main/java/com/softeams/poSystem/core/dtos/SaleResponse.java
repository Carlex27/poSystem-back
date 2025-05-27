package com.softeams.poSystem.core.dtos;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record SaleResponse(
        Long id,
        String clientName,
        String vendorName,
        LocalDateTime saleDate,
        BigDecimal total,
        String state
) {
}
