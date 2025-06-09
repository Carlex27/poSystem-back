package com.softeams.poSystem.tickets.controllers;

import com.softeams.poSystem.tickets.entities.TicketRequest;
import com.softeams.poSystem.tickets.generators.TicketPDFGenerator;
import com.softeams.poSystem.tickets.services.PrinterService;
import com.softeams.poSystem.tickets.services.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;

@RestController
@RequestMapping("/api/tickets")
@RequiredArgsConstructor
public class TicketController {
    private final TicketService ticketService;


    @PostMapping("/print")
    public ResponseEntity<String> printTicket(@RequestBody TicketRequest request) {
        try {
            File pdf = TicketPDFGenerator.generarTicketPDF(request);
            PrinterService.imprimirPDF(pdf, request.getImpresora());
            return ResponseEntity.ok("Ticket impreso en: " + request.getImpresora());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error: " + e.getMessage());
        }
    }


    @PostMapping("/print/{saleId}")
    public ResponseEntity<String> printTicket(
            @PathVariable Long saleId,
            @RequestParam String printerName) {

        try {
            ticketService.generarYImprimirTicket(saleId, printerName);
            return ResponseEntity.ok("Ticket impreso correctamente.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error: " + e.getMessage());
        }
    }
}
