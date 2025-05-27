package com.softeams.poSystem.core.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Sale {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
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
    @JsonIgnore
    @EqualsAndHashCode.Exclude
    private Set<SaleItem> items;
}
