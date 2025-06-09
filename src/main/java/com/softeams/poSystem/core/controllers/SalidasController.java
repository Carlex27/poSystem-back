package com.softeams.poSystem.core.controllers;

import com.softeams.poSystem.core.dtos.salidas.SalidasDto;
import com.softeams.poSystem.core.entities.Salidas;
import com.softeams.poSystem.core.mappers.SalidasMapper;
import com.softeams.poSystem.core.services.SalidasService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/salidas")
public class SalidasController {
    private final SalidasService salidasService;
    private final SalidasMapper salidasMapper;
    //CRUD

    //CREATE
    @PostMapping("/create")
    public ResponseEntity<?> createSalida(
            @Valid
            @RequestBody
            SalidasDto salidasDto) {
        log.info("Creating salida with data: {}", salidasDto);
        Salidas salida = salidasService.createSalida(salidasMapper.toEntity(salidasDto));
        return ResponseEntity.ok(salidasMapper.toDto(salida));
    }
    @PostMapping("/createMany")
    public ResponseEntity<?> createSalidas(
            @Valid
            @RequestBody
            List<SalidasDto> salidasDtos) {
        log.info("Creating multiple salidas");
        List<Salidas> salidas = salidasService.createSalida(salidasMapper.toEntity(salidasDtos));
        return ResponseEntity.ok(salidasMapper.toDto(salidas));
    }

    //READ


    //UPDATE
    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateSalida(
            @PathVariable Long id,
            @Valid
            @RequestBody SalidasDto salidasDto) {
        log.info("Updating salida with id: {} and data: {}", id, salidasDto);
        Salidas updatedSalida = salidasService.updateSalida(id, salidasMapper.toEntity(salidasDto));
        return ResponseEntity.ok(salidasMapper.toDto(updatedSalida));
    }

    //DELETE
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteSalida(
            @PathVariable Long id) {
        log.info("Deleting salida with id: {}", id);
        salidasService.deleteSalida(id);
        return ResponseEntity.ok().build();
    }
}
