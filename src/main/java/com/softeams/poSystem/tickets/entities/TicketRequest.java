package com.softeams.poSystem.tickets.entities;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class TicketRequest {
    private String nombreNegocio;
    private String direccion;
    private String telefono;
    private String rfc;
    private String fecha;
    private String hora;
    private List<TicketItem> items;
    private double total;
    private String mensajeFinal;
    private String url;
    private String impresora;
}
