package com.softeams.poSystem.core.repositories;

import com.softeams.poSystem.core.entities.Abono;
import com.softeams.poSystem.core.entities.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AbonoRepository extends JpaRepository<Abono, Long> {
    List<Abono> findAllByIsActiveTrueAndClient(Client client);

}
