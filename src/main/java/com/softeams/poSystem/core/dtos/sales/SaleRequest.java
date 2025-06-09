package com.softeams.poSystem.core.dtos.sales;

import java.time.LocalDateTime;
import java.util.List;

public record SaleRequest(
        Long clientId,
        boolean isCreditSale,
        LocalDateTime saleDate,
        List<SaleItemRequest> items
) {
}
