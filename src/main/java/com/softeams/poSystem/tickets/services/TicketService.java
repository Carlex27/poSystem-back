package com.softeams.poSystem.tickets.services;

import com.softeams.poSystem.core.entities.Sale;
import com.softeams.poSystem.core.services.interfaces.ISaleService;
import com.softeams.poSystem.tickets.entities.TicketItem;
import com.softeams.poSystem.tickets.entities.TicketRequest;
import com.softeams.poSystem.tickets.entities.TicketSettings;
import com.softeams.poSystem.tickets.generators.TicketPDFGenerator;
import com.softeams.poSystem.tickets.repositories.TicketSettingsRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.File;
import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TicketService {
    private final ISaleService saleService;
    private final TicketSettingsRepository ticketSettingsRepository;

    public void generarYImprimirTicket(Long saleId, String printerName) throws Exception {
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
                .impresora(printerName)
                .build();

        File pdf = TicketPDFGenerator.generarTicketPDF(request);
        PrinterService.imprimirPDF(pdf, printerName);
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


}
