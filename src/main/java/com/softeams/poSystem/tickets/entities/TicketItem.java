package com.softeams.poSystem.tickets.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TicketItem {
    private String description;
    private String cantidad;
    private double importe;
}
