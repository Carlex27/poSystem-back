package com.softeams.poSystem.core.repositories;

import com.softeams.poSystem.core.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SaleRepository extends JpaRepository<Product,Long> {

}
