package com.softeams.poSystem.core.repositories;

import com.softeams.poSystem.core.entities.Product;
import com.softeams.poSystem.core.entities.Sale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {
    Optional<Product> findByNombre(String nombre);
    List<Product> findByNombreContainingIgnoreCaseOrMarcaContainingIgnoreCaseOrSKUContainingIgnoreCase(String nombreTerm, String marcaTerm, String SkuTerm);
    List<Product> findAllByMarca(String marca);
    List<Product> findByIsActiveTrue();
    Optional<Product> findById(Long id);
    Product findBySKU(String sku);

}
