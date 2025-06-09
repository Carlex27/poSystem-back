package com.softeams.poSystem.tickets.dtos;

public record TicketSettingsDto(
        Long id,
        String nombreNegocio,
        String direccion,
        String telefono,
        String rfc,
        String mensajeFinal,
        String url
) {
}
