package com.softeams.poSystem.core.dtos;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record SaleRequest(
        String clientName,
        LocalDateTime saleDate,
        List<SaleItemRequest> items
) {
}
