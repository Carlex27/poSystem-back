package com.softeams.poSystem.core.repositories;

import com.softeams.poSystem.core.entities.Salidas;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SalidasRepository extends JpaRepository<Salidas,Long> {
}
