package com.softeams.poSystem.core.controllers;

import com.softeams.poSystem.core.services.SaleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/sales")
@Slf4j
@RequiredArgsConstructor
public class SaleController {
    private final SaleService saleService;
}
