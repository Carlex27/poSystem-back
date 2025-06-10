package com.softeams.poSystem.core.dtos.product;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductRequest {
    private String sku;
    private String nombre;
    private BigDecimal precioCosto;
    private BigDecimal precioVenta;
    private BigDecimal precioMayoreo;
    private BigDecimal precioUnidadVenta;
    private BigDecimal stock;
    private Integer unidadesPorPresentacion;
    private String departamento;
    private Integer stockMinimo;
    private Integer minimoMayoreo;
    private MultipartFile imagen;
}
