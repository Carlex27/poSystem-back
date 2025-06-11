package com.softeams.poSystem.core.repositories;

import com.softeams.poSystem.core.entities.Product;
import com.softeams.poSystem.core.entities.Sale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {
    Optional<Product> findByNombre(String nombre);


    @Query("""
SELECT p FROM Product p
WHERE p.isActive = true
AND (LOWER(p.nombre) LIKE LOWER(CONCAT('%', :term, '%'))
     OR LOWER(p.SKU) LIKE LOWER(CONCAT('%', :term, '%')))
""")
    List<Product> buscarActivosPorNombreOSku(@Param("term") String term);

    List<Product> findByIsActiveTrue();
    Optional<Product> findById(Long id);
    Product findBySKU(String sku);
    Long countByIsActiveTrue();
    Long countByStockLessThanAndIsActiveTrue(int threshold);
    @Query("SELECT SUM(p.stock) FROM Product p" +
            " WHERE p.isActive = true")
    BigDecimal calcularStockTotal();
    @Query("SELECT SUM(p.stockPorUnidades) FROM Product p" +
            " WHERE p.isActive = true")
    Integer calcularStockTotalUnidades();
}
