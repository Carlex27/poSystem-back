package com.softeams.poSystem.core.controllers;

import com.softeams.poSystem.core.dtos.caja.CajaDto;
import com.softeams.poSystem.core.entities.Caja;
import com.softeams.poSystem.core.mappers.CajaMapper;
import com.softeams.poSystem.core.services.CajaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.LocalTime;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/caja")
public class CajaController {
    private final CajaService cajaService;
    private final CajaMapper cajaMapper;

    //CRUD
    //CREATE
    @PostMapping("/create")
    public ResponseEntity<?> createCaja(
            @Valid
            @RequestBody
            CajaDto cajaDto
    ) {
        log.info("[CajaController | createCaja] Creating caja");
        LocalDateTime startOfDay = cajaDto.date().atStartOfDay();
        LocalDateTime endOfDay = cajaDto.date().atTime(LocalTime.MAX);
        Caja caja = cajaService.getCajaByDate(startOfDay, endOfDay);
        if (caja != null) {
            return ResponseEntity.ok(cajaService.updateCaja(caja.getId(), cajaMapper.toEntity(cajaDto)));
        }
        return ResponseEntity.ok(cajaService.createCaja(cajaMapper.toEntity(cajaDto)));
    }

    //READ
    @GetMapping("/findAll")
    public ResponseEntity<?> getAllCajas() {
        log.info("[CajaController | getAllCajas] Fetching all cajas");
        return ResponseEntity.ok(cajaMapper.toDto(cajaService.getAllCajas()));
    }

    //DELETE
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteCajaById(
            @PathVariable Long id
    ) {
        log.info("[CajaController | deleteCajaById] Deleting caja by id: {}", id);
        cajaService.deleteCaja(id);
        return ResponseEntity.ok("Caja deleted successfully");
    }
}
