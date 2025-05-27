package com.softeams.poSystem.core.repositories;

import com.softeams.poSystem.core.entities.Product;
import com.softeams.poSystem.core.entities.Sale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SaleRepository extends JpaRepository<Sale,Long> {
    List<Sale> findBySaleDate(String saleDate);
}
