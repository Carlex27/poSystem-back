package com.softeams.poSystem.core.services;

import com.softeams.poSystem.core.dtos.cortes.ResumeDepartamentosDto;
import com.softeams.poSystem.core.repositories.SaleItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class SaleItemService {
    private final SaleItemRepository saleItemRepository;

    public List<ResumeDepartamentosDto> getTotalVentasDepartamentos(LocalDateTime start, LocalDateTime finish){
        return saleItemRepository.obtenerVentasPorDepartamentoEnRango(start,finish);
    }

    public BigDecimal calcularGananciaPorFecha(LocalDateTime start, LocalDateTime end){
        return saleItemRepository.calcularGananciaPorFecha(start,end);
    }
}
