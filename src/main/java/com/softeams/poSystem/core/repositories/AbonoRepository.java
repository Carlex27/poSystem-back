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
    List<Abono> findAllByIsActiveTrueAndClient(Long clientid);
    @Query("""
    SELECT SUM(a.montoAbono)
    FROM Abono a
    WHERE a.isActive = true
      AND a.fechaAbono BETWEEN :inicio AND :fin
    """)
    BigDecimal calcularTotalAbonosEntreFechas(
            @Param("inicio") LocalDateTime inicio,
            @Param("fin") LocalDateTime fin
    );



}
