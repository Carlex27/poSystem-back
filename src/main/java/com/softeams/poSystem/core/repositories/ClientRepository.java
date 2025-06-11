package com.softeams.poSystem.core.repositories;

import com.softeams.poSystem.core.entities.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
    List<Client> findAllByIsActiveTrue();
    List<Client> findByIsActiveTrueAndNameContainingIgnoreCase(String nameTerm);
    @Query("SELECT SUM(c.balance) FROM Client c WHERE c.isActive = true")
    BigDecimal calcularBalanceTotalClientesActivos();


}
