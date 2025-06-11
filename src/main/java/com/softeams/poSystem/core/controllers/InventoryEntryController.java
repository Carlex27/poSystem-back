package com.softeams.poSystem.core.controllers;

import com.softeams.poSystem.core.entities.InventoryEntry;
import com.softeams.poSystem.core.services.InventoryEntryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/inventory")
@CrossOrigin
@RequiredArgsConstructor
@Slf4j
public class InventoryEntryController {
    private final InventoryEntryService inventoryEntryService;

    @GetMapping("/entry/product")
    public ResponseEntity<?> entriesByProduct(
            @RequestParam Long id
    ){
        return ResponseEntity.ok(inventoryEntryService.getEntriesByProductIdAndEntryDateAsc(id));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateEntry(
            @PathVariable Long id,
            @RequestBody InventoryEntry entry
    ){
        log.info("SI SE ACTUALIZO");
        inventoryEntryService.update(id,entry);

        log.info("SI SE ACTUALIZO");
        return ResponseEntity.ok("Entrada actualizada exitosamente");
    }
}
