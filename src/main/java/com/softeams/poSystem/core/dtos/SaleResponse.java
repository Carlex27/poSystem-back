package com.softeams.poSystem.core.dtos;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public record SaleResponse(
        Long id,
        String clientName,
        String vendorName,
        LocalDateTime saleDate,
        BigDecimal total,
        String state,
        Set<SaleItemResponse> items,
        Long itemCount
) {
}
