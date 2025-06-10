package com.softeams.poSystem.core.repositories;

import com.softeams.poSystem.core.entities.InventoryEntry;
import com.softeams.poSystem.core.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface InventoryEntryRepository extends JpaRepository<InventoryEntry, Long> {
    List<InventoryEntry> findByProducto(Product product);

    List<InventoryEntry> findByProductoOrderByEntryDateAsc(Product product);

    @Query("""
    SELECT SUM(
        e.precioPorCaja *
        ((e.cajasCompradas * e.producto.unidadesPorPresentacion - e.unidadesVendidas) / e.producto.unidadesPorPresentacion)
    )
    FROM InventoryEntry e
    WHERE e.producto.id = :productId
    """)
    BigDecimal calcularValorInventarioDisponiblePorProducto(@Param("productId") Long productId);


    @Query("""
    SELECT SUM(
        e.precioPorCaja *
        ((e.cajasCompradas * e.producto.unidadesPorPresentacion - e.unidadesVendidas) / e.producto.unidadesPorPresentacion)
    )
    FROM InventoryEntry e
    """)
    BigDecimal calcularValorInventarioDisponible();


}
