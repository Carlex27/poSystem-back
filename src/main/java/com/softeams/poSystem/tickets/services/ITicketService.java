package com.softeams.poSystem.tickets.services;

import com.softeams.poSystem.core.dtos.cortes.CorteDto;

import java.io.File;
import java.time.LocalDateTime;

public interface ITicketService {
    File generarTicketVenta(Long saleId) throws Exception;
    void ImprimirTicket(File ticket) throws Exception;
    File generarTicketCorte(CorteDto corteDto, LocalDateTime fecha, LocalDateTime fecha2) throws Exception;
}
