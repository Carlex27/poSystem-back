package com.softeams.poSystem.core.controllers;

import com.softeams.poSystem.core.dtos.sales.SaleRequest;
import com.softeams.poSystem.core.entities.Sale;
import com.softeams.poSystem.core.mappers.SaleMapper;
import com.softeams.poSystem.core.services.SaleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@RestController
@RequestMapping("/api/sales")
@CrossOrigin
@Slf4j
@RequiredArgsConstructor
public class SaleController {
    private final SaleService saleService;
    private final SaleMapper saleMapper;

    //CRUD
    //CREATE
    @PostMapping("/create")
    public ResponseEntity<?> createSale(
            @Valid
            @RequestBody SaleRequest saleRequest,
            Authentication authentication
            ){
        log.info("[SaleController | CreateSale] Creating sale by: {}", authentication.getName());
        Sale sale;
        if(!saleRequest.isCreditSale()){
            sale = saleMapper.toEntity(saleRequest,authentication);
        }else {
            sale = saleMapper.toEntity(saleRequest, authentication, true);
        }

        return ResponseEntity.ok(saleService.creatingSale(sale));
    }

    //READ

    @GetMapping("/findAll")
    public ResponseEntity<?> getAllSales(
            Authentication authentication
    ) {
        log.info("[SaleController | GetAllSales] Fetching all sales by: {}", authentication.getName());
        return ResponseEntity.ok(saleMapper.toResponse(saleService.getAllSales()));
    }
    @GetMapping("/findById/{id}")
    public ResponseEntity<?> getSaleById(
            @PathVariable Long id,
            Authentication authentication
    ) {
        log.info("[SaleController | GetSaleById] Fetching sale by id: {} by: {}", id, authentication.getName());
        return ResponseEntity.ok(saleMapper.toResponse(saleService.getSaleById(id)));
    }

    @GetMapping("/by-date")
    public ResponseEntity<?> getSalesByDate(
            @RequestParam("date")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ){
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(LocalTime.MAX);
        return ResponseEntity.ok(saleMapper.toResponse(saleService.getSalesByDate(startOfDay, endOfDay)));
    }

    @GetMapping("/by-date-range")
    public ResponseEntity<?> getSalesByDateRange(
            @RequestParam("startDate")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam("endDate")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(LocalTime.MAX);
        return ResponseEntity.ok(saleMapper.toResponse(saleService.getSalesByDate(startDateTime, endDateTime)));
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

        return ResponseEntity.ok(saleMapper.toResponse(saleService.getSalesByDate(startDateTime, endDateTime)));
    }

    @GetMapping("/by-month/client")
    public ResponseEntity<?> getSalesByMonthAndClient(
            @RequestParam("year") int year,
            @RequestParam("month") int month,
            @RequestParam("clientId") Long clientId
    ) {
        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());

        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(LocalTime.MAX);

        return ResponseEntity.ok(saleMapper.toResponse(saleService.getSalesByDateAndClient(startDateTime, endDateTime, clientId)));
    }



    @GetMapping("/details/{id}")
    public ResponseEntity<?> getSaleDetailsById(
            @PathVariable Long id,
            Authentication authentication
    ) {
        log.info("[SaleController | GetSaleDetailsById] Fetching sale details by id: {} by: {}", id, authentication.getName());
        return ResponseEntity.ok(saleMapper.toSaleItemsResponse(saleService.getSaleItemsById(id)));
    }
    //UPDATE AND DELETE NO EXISTE
}
