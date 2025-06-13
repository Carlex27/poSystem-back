package com.softeams.poSystem.core.services.interfaces;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface ISalidaService {
    BigDecimal getTotalSalidasInRange(LocalDateTime start, LocalDateTime finish);
}
