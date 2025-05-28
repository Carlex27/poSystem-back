package com.softeams.poSystem.core.controllers;

import com.softeams.poSystem.core.dtos.SaleRequest;
import com.softeams.poSystem.core.mappers.SaleMapper;
import com.softeams.poSystem.core.services.SaleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/sales")
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
        return ResponseEntity.ok(saleService.creatingSale(saleMapper.toEntity(saleRequest,authentication)));
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
