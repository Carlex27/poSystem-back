package com.softeams.poSystem.tickets.controllers;

import com.softeams.poSystem.core.services.interfaces.IClientService;
import com.softeams.poSystem.tickets.entities.TicketRequest;
import com.softeams.poSystem.tickets.generators.TicketPDFGenerator;
import com.softeams.poSystem.tickets.services.PrinterService;
import com.softeams.poSystem.tickets.services.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileInputStream;

@RestController
@RequestMapping("/api/tickets")
@RequiredArgsConstructor
public class TicketController {
    private final TicketService ticketService;
    private final IClientService clientService;


    @GetMapping("/print")
    public ResponseEntity<String> printTicket(@RequestBody TicketRequest request) {
        try {
            File pdf = TicketPDFGenerator.generarTicketPDF(request);
            PrinterService.imprimirPDF(pdf, request.getImpresora());
            return ResponseEntity.ok("Ticket impreso en: " + request.getImpresora());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/print/{saleId}")
    public ResponseEntity<String> printTicket(
            @PathVariable Long saleId) {
        File ticket = null;
        try {
            ticket = ticketService.generarTicketVenta(saleId);
            ticketService.ImprimirTicket(ticket);
            return ResponseEntity.ok("Ticket impreso correctamente.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error: " + e.getMessage());
        }finally {
            if (ticket != null && ticket.exists()) {
                ticket.delete();
            }
        }
    }

    @GetMapping("/pdf/sale/{saleId}")
    public ResponseEntity<?> getTicketPdf(@PathVariable Long saleId) {
        File ticket = null;

        try {
            ticket = ticketService.generarTicketVenta(saleId);
            InputStreamResource resource = new InputStreamResource(new FileInputStream(ticket));

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=" + ticket.getName())
                    .contentLength(ticket.length())
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(resource);

        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error: " + e.getMessage());
        } finally {
            if (ticket != null && ticket.exists()) {
                ticket.delete();
            }
        }
    }

    @GetMapping("/pdf/saldos")
    public ResponseEntity<?> getSaldosPdf() {
        File ticket = null;

        try {
            ticket = ticketService.generarTicketSaldos(clientService.getAllClients());
            InputStreamResource resource = new InputStreamResource(new FileInputStream(ticket));

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=" + ticket.getName())
                    .contentLength(ticket.length())
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(resource);

        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error: " + e.getMessage());
        } finally {
            if (ticket != null && ticket.exists()) {
                ticket.delete();
            }
        }
    }

    @GetMapping("/print/saldos")
    public ResponseEntity<?> printSaldos() {
        File ticket = null;

        try {
            ticket = ticketService.generarTicketSaldos(clientService.getAllClients());
            ticketService.ImprimirTicket(ticket);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error: " + e.getMessage());
        } finally {
            if (ticket != null && ticket.exists()) {
                ticket.delete();
            }
        }
        return ResponseEntity.ok("Ticket impreso correctamente");
    }

}
