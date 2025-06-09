package com.softeams.poSystem.core.services;

import com.softeams.poSystem.core.repositories.InventoryEntryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class InventoryEntryService {
    private final InventoryEntryRepository inventoryEntryRepository;
}
