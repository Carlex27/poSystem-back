package com.softeams.poSystem.core.repositories;

import com.softeams.poSystem.core.entities.Caja;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface CajaRepository extends JpaRepository<Caja, Long> {
    Optional<Caja> findByDateBetween(LocalDateTime startDate, LocalDateTime endDate);

}
