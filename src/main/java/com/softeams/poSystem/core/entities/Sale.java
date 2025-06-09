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

    @ManyToOne(optional = true)
    @JoinColumn(name = "client_id", nullable = true)
    private Client client;

    private String vendedorName;

    @Column(nullable = false)
    @EqualsAndHashCode.Include
    private LocalDateTime saleDate;

    @Column(nullable = false)
    @EqualsAndHashCode.Include
    private BigDecimal total;

    @Column(nullable = false)
    @EqualsAndHashCode.Include
    private String state; // 'Mayoreo' or 'Menudeo'

    private Long itemCount;

    @OneToMany(mappedBy = "sale", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    @EqualsAndHashCode.Exclude
    private Set<SaleItem> items;

    private boolean isCreditSale; // Indicates if the sale is a credit sale
}
