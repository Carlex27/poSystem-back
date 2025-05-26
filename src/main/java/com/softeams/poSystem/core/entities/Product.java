package com.softeams.poSystem.core.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Product {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(nullable = false, length = 50)
    private String marca;

    @Column(name = "grados_alcohol", nullable = false, precision = 4, scale = 2)
    private BigDecimal gradosAlcohol;

    @Column(nullable = false, length = 20)
    private String tamanio; // e.g. "350ml", "1L"

    @Column(name = "precio_normal", nullable = false, precision = 10, scale = 2)
    private BigDecimal precioNormal;

    @Column(name = "precio_mayoreo", nullable = false, precision = 10, scale = 2)
    private BigDecimal precioMayoreo;

    @Column(nullable = false)
    private Integer Stock;
}
