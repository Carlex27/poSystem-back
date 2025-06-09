package com.softeams.poSystem.tickets.mappers;

import com.softeams.poSystem.tickets.dtos.TicketSettingsDto;
import com.softeams.poSystem.tickets.entities.TicketSettings;
import org.springframework.stereotype.Component;

@Component
public class TicketSettingsMapper {
    public TicketSettings toEntity(TicketSettingsDto ticketSettings) {
        if (ticketSettings == null) {
            return null;
        }
        return TicketSettings.builder()
                .nombreNegocio(ticketSettings.nombreNegocio())
                .direccion(ticketSettings.direccion())
                .telefono(ticketSettings.telefono())
                .rfc(ticketSettings.rfc())
                .mensajeFinal(ticketSettings.mensajeFinal())
                .url(ticketSettings.url())
                .build();
    }

    public TicketSettingsDto toDto(TicketSettings ticketSettings) {
        if (ticketSettings == null) {
            return null;
        }
        return new TicketSettingsDto(
                ticketSettings.getId(),
                ticketSettings.getNombreNegocio(),
                ticketSettings.getDireccion(),
                ticketSettings.getTelefono(),
                ticketSettings.getRfc(),
                ticketSettings.getMensajeFinal(),
                ticketSettings.getUrl()
        );
    }
}
