package com.softeams.poSystem.core.repositories;

import com.softeams.poSystem.core.entities.SaleItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SaleItemRepository extends JpaRepository<SaleItem,Long> {
}
