package com.softeams.poSystem.core.repositories;

import com.softeams.poSystem.core.entities.Caja;
import com.softeams.poSystem.core.entities.Salidas;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface SalidasRepository extends JpaRepository<Salidas,Long> {
    @Query("""
    SELECT SUM(s.amount)
    FROM Salidas s
    WHERE s.date BETWEEN :inicio AND :fin
    """)
    BigDecimal calcularTotalSalidasEntreFechas(
            @Param("inicio") LocalDateTime inicio,
            @Param("fin") LocalDateTime fin
    );

    List<Salidas> findByDateBetween(LocalDateTime startDate, LocalDateTime endDate);



}
