package com.softeams.poSystem.core.services.interfaces;


import com.softeams.poSystem.core.entities.Sale;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface ISaleService {
    List<Sale> getTop3SalesByDateRange(LocalDateTime startDate, LocalDateTime endDate);
    long countSalesInRange(LocalDateTime start, LocalDateTime end);
    BigDecimal getTotalVentas(LocalDateTime start, LocalDateTime end);
    Sale getSaleById(Long id);
    BigDecimal getTotalVentasByDateRange(LocalDateTime startDate, LocalDateTime endDate);

}
