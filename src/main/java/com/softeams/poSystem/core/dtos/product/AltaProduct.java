package com.softeams.poSystem.core.dtos.product;

public record AltaProduct(
        String sku,
        Integer stock,
        String nombre,
        Integer cantidad
) {
}
