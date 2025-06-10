package com.softeams.poSystem.tickets.controllers;

import com.softeams.poSystem.tickets.dtos.TicketSettingsDto;
import com.softeams.poSystem.tickets.mappers.TicketSettingsMapper;
import com.softeams.poSystem.tickets.services.TicketService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/ticketSettings")
public class TicketSettingsController {
    private final TicketService ticketService;
    private final TicketSettingsMapper ticketSettingsMapper;
    //CRUD
    //CREATE
    @PostMapping("/create")
    public ResponseEntity<?> createTicketSettings(
            @Valid
            @RequestBody
            TicketSettingsDto ticketSettingsDto
    ) {
      log.info("[TicketSettingsController | createTicketSettings] Creating ticket settings: {}", ticketSettingsDto);
        return ResponseEntity.ok(ticketService.createTicketSettings(ticketSettingsMapper.toEntity(ticketSettingsDto)));
    }
    //READ
    @GetMapping("/findAll")
    public ResponseEntity<?> getAllTicketSettings() {
        log.info("[TicketSettingsController | getAllTicketSettings] Fetching all ticket settings");
        return ResponseEntity.ok(ticketSettingsMapper.toDto(ticketService.getTicketSettings()));
    }
    //UPDATE
    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateTicketSettings(
            @Valid
            @RequestBody
            TicketSettingsDto ticketSettingsDto
    ) {
        log.info("[TicketSettingsController | updateTicketSettings] Updating ticket settings: {}", ticketSettingsDto);
        return ResponseEntity.ok(ticketService.updateTicketSettings(ticketSettingsMapper.toEntity(ticketSettingsDto)));
    }
}
