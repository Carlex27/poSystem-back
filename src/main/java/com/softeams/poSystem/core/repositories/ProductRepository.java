package com.softeams.poSystem.core.repositories;

import com.softeams.poSystem.core.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {
    Optional<Product> findByNombre(String nombre);
     List<Product> findAllByMarca(String marca);
}
