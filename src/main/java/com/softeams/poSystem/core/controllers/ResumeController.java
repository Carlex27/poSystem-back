package com.softeams.poSystem.core.controllers;

import com.softeams.poSystem.core.mappers.ISaleMapper;
import com.softeams.poSystem.core.services.interfaces.IProductService;
import com.softeams.poSystem.core.services.interfaces.ISaleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    private final int LOW_STOCK_THRESHOLD = 20;

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
