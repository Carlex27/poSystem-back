package com.softeams.poSystem.core.repositories;

import com.softeams.poSystem.core.entities.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
    List<Client> findAllByIsActiveTrue();
    List<Client> findByNameContainingIgnoreCase(String nameTerm);


}
