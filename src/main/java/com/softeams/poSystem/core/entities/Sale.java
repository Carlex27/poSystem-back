package com.softeams.poSystem.core.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Sale {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String clientName;

    private String vendedorName;

    @Column(nullable = false)
    private LocalDateTime saleDate;

    @Column(nullable = false)
    private BigDecimal total;

    @Column(nullable = false)
    private String state; // 'Mayoreo' or 'Menudeo'

    @OneToMany(mappedBy = "sale", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<SaleItem> items;
}
