package com.softeams.poSystem.core.repositories;

import com.softeams.poSystem.core.entities.Abono;
import com.softeams.poSystem.core.entities.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AbonoRepository extends JpaRepository<Abono, Long> {
    List<Abono> findAllByIsActiveTrueAndClientId(Long clientId);

    @Query(value = """
        SELECT COALESCE(SUM(monto_abono), 0)
        FROM abono
        WHERE fecha_abono BETWEEN :startDate AND :endDate
        AND is_active = true
    """, nativeQuery = true)
    BigDecimal getTotalAbonosInRangeAndIsActive(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );


}
