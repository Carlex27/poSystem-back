package com.softeams.poSystem.core.dtos.sales;

import java.time.LocalDateTime;
import java.util.List;

public record SaleRequest(
        String clientName,
        LocalDateTime saleDate,
        List<SaleItemRequest> items
) {
}
