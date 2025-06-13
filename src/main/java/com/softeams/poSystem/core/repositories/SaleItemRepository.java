package com.softeams.poSystem.core.repositories;

import com.softeams.poSystem.core.dtos.cortes.ResumeDepartamentosDto;
import com.softeams.poSystem.core.entities.SaleItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SaleItemRepository extends JpaRepository<SaleItem, Long> {
    @Query("""
                SELECT p.department.name AS nombreDepartamento,
                       SUM(si.quantity * si.price) AS totalVentas
                FROM SaleItem si
                JOIN si.sale s
                JOIN si.product p
                WHERE s.saleDate BETWEEN :startDate AND :endDate
                  AND s.isCreditSale = false
                  AND p.isActive = true
                  AND p.department.isActive = true
                GROUP BY p.department.name
                ORDER BY totalVentas DESC
            """)
    List<ResumeDepartamentosDto> obtenerVentasPorDepartamentoEnRango(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );

    @Query("""
                SELECT SUM(
                    (si.price - (p.precioCosto / p.unidadesPorPresentacion)) * si.quantity
                )
                FROM SaleItem si
                JOIN si.product p
                JOIN si.sale s
                WHERE s.saleDate BETWEEN :start AND :end
                  AND s.isCreditSale = false
            """)
    BigDecimal calcularGananciaPorFecha(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );

}
