package com.softeams.poSystem.core.dtos.sales;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

public record SaleResponse(
        Long id,
        String clientName,
        String vendorName,
        LocalDateTime saleDate,
        BigDecimal total,
        String state,
        boolean isCreditSale,
        Set<SaleItemResponse> items,
        Long itemCount
) {
}
