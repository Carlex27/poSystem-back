package com.softeams.poSystem.tickets.services;

import com.softeams.poSystem.core.dtos.cortes.CorteDto;
import com.softeams.poSystem.core.entities.Client;
import com.softeams.poSystem.core.entities.Sale;
import com.softeams.poSystem.core.services.interfaces.ISaleService;
import com.softeams.poSystem.tickets.entities.TicketItem;
import com.softeams.poSystem.tickets.entities.TicketRequest;
import com.softeams.poSystem.tickets.entities.TicketSettings;
import com.softeams.poSystem.tickets.generators.TicketPDFGenerator;
import com.softeams.poSystem.tickets.repositories.TicketSettingsRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TicketService implements ITicketService {
    private final ISaleService saleService;
    private final TicketSettingsRepository ticketSettingsRepository;

    public File generarTicketVenta(Long saleId) throws Exception {
        Sale sale = saleService.getSaleById(saleId);

        TicketSettings settings = ticketSettingsRepository.findFirstByOrderByIdAsc()
                .orElseThrow(() -> new RuntimeException("Configuración de ticket no encontrada"));

        List<TicketItem> items = sale.getItems().stream()
                .map(si -> new TicketItem(
                        si.getProduct().getNombre(),
                        si.getQuantity().toString(),
                        si.getPrice().multiply(BigDecimal.valueOf(si.getQuantity())).doubleValue()
                ))
                .toList();

        TicketRequest request = TicketRequest.builder()
                .nombreNegocio(settings.getNombreNegocio())
                .direccion(settings.getDireccion())
                .telefono(settings.getTelefono())
                .rfc(settings.getRfc())
                .fecha(sale.getSaleDate().toLocalDate().toString())
                .hora(sale.getSaleDate().toLocalTime().format(DateTimeFormatter.ofPattern("hh:mm a")))
                .items(items)
                .total(sale.getTotal().doubleValue())
                .mensajeFinal(settings.getMensajeFinal())
                .url(settings.getUrl())
                .impresora(settings.getImpresora())
                .cajero(sale.getVendedorName())
                .clientName(sale.getClient().getName())
                .folio(sale.getId())
                .isCreditSale(sale.isCreditSale())
                .build();

        return TicketPDFGenerator.generarTicketPDF(request);
    }

    public void ImprimirTicket(File ticket) throws Exception {
        PrinterService.imprimirPDF(ticket, ticketSettingsRepository.getImpresora());
    }

    public File generarTicketCorte(CorteDto corteDto, LocalDateTime start, LocalDateTime finish) throws Exception{
        TicketSettings settings = ticketSettingsRepository.findFirstByOrderByIdAsc()
                .orElseThrow(() -> new RuntimeException("Configuración de ticket no encontrada"));
        log.info(corteDto.toString());
        return TicketPDFGenerator.generarTicketCortePDF(settings, corteDto, start,
                saleService.countSalesInRange(start,finish));
    }

    public File generarTicketSaldos(List<Client> clients) throws Exception{
        TicketSettings settings = ticketSettingsRepository.findFirstByOrderByIdAsc()
                .orElseThrow(() -> new RuntimeException("Configuración de ticket no encontrada"));

        return TicketPDFGenerator.generarTicketSaldosPDF(settings, clients);
    }

    //CRUD
    //CREATE TICKET SETTINGS
    public TicketSettings createTicketSettings(TicketSettings settings) {
        return ticketSettingsRepository.save(settings);
    }

    // READ TICKET SETTINGS
    public TicketSettings getTicketSettings() {
        return ticketSettingsRepository.findFirstByOrderByIdAsc()
                .orElseThrow(() -> new RuntimeException("Configuración de ticket no encontrada"));
    }
    // UPDATE TICKET SETTINGS
    @Transactional
    public TicketSettings updateTicketSettings(TicketSettings settings) {
        TicketSettings existingSettings = ticketSettingsRepository.findFirstByOrderByIdAsc()
                .orElseThrow(() -> new RuntimeException("Configuración de ticket no encontrada"));

        existingSettings.setNombreNegocio(settings.getNombreNegocio());
        existingSettings.setDireccion(settings.getDireccion());
        existingSettings.setTelefono(settings.getTelefono());
        existingSettings.setRfc(settings.getRfc());
        existingSettings.setMensajeFinal(settings.getMensajeFinal());
        existingSettings.setUrl(settings.getUrl());

        return ticketSettingsRepository.save(existingSettings);
    }

    @Transactional
    public String updateImpresora(String impresora){
        TicketSettings existingSettings = ticketSettingsRepository.findFirstByOrderByIdAsc()
                .orElseThrow(() -> new RuntimeException("Configuración de ticket no encontrada"));

        existingSettings.setImpresora(impresora);
        ticketSettingsRepository.save(existingSettings);
        return "Impresora actualizada exitosamente.";
    }


}
