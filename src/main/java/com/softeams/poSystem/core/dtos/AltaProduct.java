package com.softeams.poSystem.core.dtos;

public record AltaProduct(
        String sku,
        Integer stock,
        String nombre,
        Integer cantidad
) {
}
