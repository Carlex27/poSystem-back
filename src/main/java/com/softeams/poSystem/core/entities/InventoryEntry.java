package com.softeams.poSystem.core.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InventoryEntry {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime entryDate;

    @ManyToOne
    @JoinColumn(name = "producto_id", nullable = false)
    @JsonIgnore
    private Product producto;

    @Column(nullable = false)
    private Integer cajasCompradas;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal precioPorCaja;

    @Column(nullable = false)
    @JsonIgnore
    private Integer unidadesVendidas;

    public BigDecimal getTotalCompra() {
        return precioPorCaja.multiply(new BigDecimal(cajasCompradas));
    }

    public int getUnidadesAgregadas() {
        return cajasCompradas * producto.getUnidadesPorPresentacion();
    }

    public int getUnidadesDisponibles(){
        return getUnidadesAgregadas() - unidadesVendidas;
    }
}
