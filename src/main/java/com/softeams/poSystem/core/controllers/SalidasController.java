package com.softeams.poSystem.core.controllers;

import com.softeams.poSystem.core.dtos.salidas.SalidasDto;
import com.softeams.poSystem.core.entities.Salidas;
import com.softeams.poSystem.core.mappers.SalidasMapper;
import com.softeams.poSystem.core.services.SalidasService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
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
    @GetMapping("/findAll")
    public ResponseEntity<?> findAllSalidas() {
        log.info("Finding all salidas");
        List<Salidas> salidas = salidasService.getAllSalidas();
        return ResponseEntity.ok(salidasMapper.toDto(salidas));
    }
    @GetMapping("/by-date")
    public ResponseEntity<?> getSalesByDate(
            @RequestParam("date")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ){
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(LocalTime.MAX);
        return ResponseEntity.ok(salidasMapper.toDto(salidasService.getSalidasByDate(startOfDay, endOfDay)));
    }

    @GetMapping("/by-month")
    public ResponseEntity<?> getSalesByMonth(
            @RequestParam("year") int year,
            @RequestParam("month") int month
    ) {
        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());

        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(LocalTime.MAX);

        return ResponseEntity.ok(salidasMapper.toDto(salidasService.getSalidasByDate(startDateTime, endDateTime)));
    }



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
