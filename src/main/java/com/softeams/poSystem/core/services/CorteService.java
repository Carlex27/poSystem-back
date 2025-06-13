package com.softeams.poSystem.core.services;

import com.softeams.poSystem.core.dtos.cortes.CorteDto;
import com.softeams.poSystem.core.dtos.cortes.ResumeDepartamentosDto;
import com.softeams.poSystem.core.services.interfaces.IAbonoService;
import com.softeams.poSystem.core.services.interfaces.ISaleService;
import com.softeams.poSystem.core.services.interfaces.ISalidaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class CorteService {
    private final ISaleService saleService;
    private final ISalidaService salidaService;
    private final IAbonoService abonoService;
    private final SaleItemService saleItemService;

    public CorteDto crearCorte(LocalDateTime start, LocalDateTime finish){
        BigDecimal pagosClientes = abonoService.getTotalAbonosInRange(start, finish);
        BigDecimal pagosProveedores = salidaService.getTotalSalidasInRange(start, finish);
        BigDecimal ventas = saleService.getTotalVentasByDateRange(start, finish);
        List<ResumeDepartamentosDto> ventasPorDepartamentos = saleItemService.getTotalVentasDepartamentos(start, finish);

        BigDecimal dineroEnCaja = (ventas).subtract(pagosProveedores);
        BigDecimal ventasTotales = ventas.add(pagosClientes);
        BigDecimal gananciaPorDia = saleItemService.calcularGananciaPorFecha(start, finish);

        return new CorteDto(
                ventas,
                pagosClientes,
                pagosProveedores,
                ventasPorDepartamentos,
                dineroEnCaja,
                ventasTotales,
                gananciaPorDia
        );
    }

}
