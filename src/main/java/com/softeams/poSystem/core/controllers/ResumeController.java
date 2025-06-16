package com.softeams.poSystem.core.controllers;

import com.softeams.poSystem.core.mappers.interfaces.ISaleMapper;
import com.softeams.poSystem.core.services.CorteService;
import com.softeams.poSystem.core.services.interfaces.IProductService;
import com.softeams.poSystem.core.services.interfaces.ISaleService;
import com.softeams.poSystem.tickets.services.ITicketService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.InputStreamResource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileInputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@RestController
@Slf4j
@RequestMapping("/api/resume")
@CrossOrigin
@RequiredArgsConstructor
public class ResumeController {
    private final ISaleMapper saleMapper;
    private final IProductService productService;
    private final ISaleService saleService;
    private final int LOW_STOCK_THRESHOLD = 2;
    private final CorteService corteService;
    private final ITicketService ticketService;

    @GetMapping("/ResumeVentas")
    public ResponseEntity<?> getResumeVentas(
            @RequestParam("date")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ){
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(LocalTime.MAX);
        return ResponseEntity.ok(saleMapper.toResumeVentasDto(
                saleService.countSalesInRange(startOfDay, endOfDay),
                saleService.getTotalVentas(startOfDay, endOfDay)
        ));
    }

    @GetMapping("/corte/day")
    public ResponseEntity<?> getCorteByDay(
            @RequestParam("date")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ){
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(LocalTime.MAX);
        return ResponseEntity.ok(corteService.crearCorte(startOfDay, endOfDay));
    }

    @GetMapping("/pdf/corte")
    public ResponseEntity<?> getCortePdf(
            @RequestParam("date")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        File ticket = null;
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(LocalTime.MAX);
        try {

            ticket = ticketService.generarTicketCorte(corteService.crearCorte(startOfDay, endOfDay), startOfDay, endOfDay);

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

    @GetMapping("/print/corte")
    public ResponseEntity<?> printCorte(
            @RequestParam("date")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        File ticket = null;
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(LocalTime.MAX);
        try {

            ticket = ticketService.generarTicketCorte(corteService.crearCorte(startOfDay, endOfDay), startOfDay, endOfDay);
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

    @GetMapping("/ResumeVentasByMonth")
    public ResponseEntity<?> getResumeVentasByMonth(
            @RequestParam("year") int year,
            @RequestParam("month") int month
    ) {
        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());

        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(LocalTime.MAX);

        return ResponseEntity.ok(saleMapper.toResumeVentasDto(
                saleService.countSalesInRange(startDateTime, endDateTime),
                saleService.getTotalVentas(startDateTime, endDateTime)
        ));
    }

    @GetMapping("/ResumeDashboard")
    public ResponseEntity<?> getResumeDashboard(
            @RequestParam("date")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ){
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(LocalTime.MAX);
        return ResponseEntity.ok(saleMapper.toResumeDashboardDto(
                productService.getProductCount(),
                saleService.countSalesInRange(startOfDay, endOfDay),
                saleService.getTotalVentas(startOfDay, endOfDay),
                productService.getLowStockCount(LOW_STOCK_THRESHOLD),
                saleService.getTop3SalesByDateRange(startOfDay, endOfDay)
        ));
    }

    @GetMapping("/ResumeDashboardByMonth")
    public ResponseEntity<?> getResumeDashboardByMonth(
            @RequestParam("year") int year,
            @RequestParam("month") int month
    ) {
        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());

        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(LocalTime.MAX);

        return ResponseEntity.ok(saleMapper.toResumeDashboardDto(
                productService.getProductCount(),
                saleService.countSalesInRange(startDateTime, endDateTime),
                saleService.getTotalVentas(startDateTime, endDateTime),
                productService.getLowStockCount(LOW_STOCK_THRESHOLD),
                saleService.getTop3SalesByDateRange(startDateTime, endDateTime)
        ));
    }
}
