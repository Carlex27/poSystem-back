package com.softeams.poSystem.core.repositories;

import com.softeams.poSystem.core.entities.InventoryEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InventoryEntryRepository extends JpaRepository<InventoryEntry, Long> {

}
