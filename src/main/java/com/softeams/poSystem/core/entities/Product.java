package com.softeams.poSystem.core.entities;

import jakarta.persistence.*;
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

    @Column(nullable = false, unique = true)
    private String SKU;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(name = "precio_costo", nullable = false, precision = 10, scale = 2)
    private BigDecimal precioCosto;

    @Column(name = "precio_venta", nullable = false, precision = 10, scale = 2)
    private BigDecimal precioVenta;

    @Column(name = "precio_pieza", nullable = false, precision = 10, scale = 2)
    private BigDecimal precioPiezaVenta;

    @Column(name = "precio_pieza_mayoreo", nullable = false, precision = 10, scale = 2)
    private BigDecimal precioPiezaMayoreo;

    @Column(name = "precio_mayoreo", nullable = false, precision = 10, scale = 2)
    private BigDecimal precioMayoreo;

    @Column(nullable = false)
    private Integer minimoMayoreo;

    @Column(nullable = false)
    private BigDecimal stock;

    private Integer stockMinimo;

    @Column(length = 50)
    private String unidadCompra;

    @Column(nullable = false)
    private Integer unidadesPorPresentacion;

    @ManyToOne
    @JoinColumn(name = "departamento_id", nullable = false)
    private Department department;

    @Column(nullable = false)
    private Boolean isActive;

    private String imagePath; // Optional field for product image path
}
