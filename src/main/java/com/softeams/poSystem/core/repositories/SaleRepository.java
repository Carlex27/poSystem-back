package com.softeams.poSystem.core.repositories;

import com.softeams.poSystem.core.entities.Sale;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface SaleRepository extends JpaRepository<Sale,Long> {
    Optional<Sale> findById(Long id);
    List<Sale> findBySaleDateBetween(LocalDateTime startDate, LocalDateTime endDate);

    List<Sale> findByClientIdAndSaleDateBetween(Long clientId, LocalDateTime startDate, LocalDateTime endDate);


    @Query("SELECT SUM(s.total) FROM Sale s WHERE s.saleDate >= :start AND s.saleDate < :end")
    BigDecimal getTotalVentas(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query("SELECT COUNT(s) FROM Sale s WHERE s.saleDate >= :start AND s.saleDate < :end")
    Long countSalesInRange(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query("SELECT s FROM Sale s WHERE s.saleDate BETWEEN :startDate AND :endDate ORDER BY s.saleDate DESC")
    List<Sale> findTop3BySaleDateRange(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            Pageable pageable
    );

    @Query(value = """
    SELECT COALESCE(SUM(total), 0)
    FROM sale
    WHERE sale_date BETWEEN :startDate AND :endDate
    AND is_credit_sale = false
""", nativeQuery = true)
    BigDecimal getTotalSalesInRangeAndIsNotCredit(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );


}
