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

    private BigDecimal precioNormal;
    private BigDecimal precioMayoreo;
    private Integer stock;
    private MultipartFile imagen;
}
