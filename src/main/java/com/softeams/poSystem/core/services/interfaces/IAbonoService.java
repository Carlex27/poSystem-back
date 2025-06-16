package com.softeams.poSystem.core.services.interfaces;

import com.softeams.poSystem.core.dtos.abono.AbonoResumenDto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface IAbonoService {
    BigDecimal getTotalAbonosInRange(LocalDateTime start, LocalDateTime finish);
    List<AbonoResumenDto> getResumenAbonos(LocalDateTime start, LocalDateTime finish);
}
