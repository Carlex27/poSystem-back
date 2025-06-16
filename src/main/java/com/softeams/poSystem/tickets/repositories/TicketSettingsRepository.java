package com.softeams.poSystem.tickets.repositories;

import com.softeams.poSystem.tickets.entities.TicketSettings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TicketSettingsRepository extends JpaRepository<TicketSettings, Long> {
    TicketSettings findFirstByOrderByIdDesc();
    Optional<TicketSettings> findFirstByOrderByIdAsc();
    @Query("SELECT ts.impresora FROM TicketSettings ts WHERE ts.id = (SELECT MIN(t.id) FROM TicketSettings t)")
    String getImpresora(); // Obtiene la última configuración de ticket
}
