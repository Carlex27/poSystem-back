package com.softeams.poSystem.core.entities;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class SaleItem {

    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "sale_id")
    @JsonIgnore
    @EqualsAndHashCode.Exclude
    private Sale sale;

    @ManyToOne(optional = false)
    @JoinColumn(name = "producto_id")
    //@JsonIgnore
    @EqualsAndHashCode.Include
    private Product product;

    @Column(nullable = false)
    @EqualsAndHashCode.Include
    private Integer quantity;

    @Column(nullable = false, precision = 10, scale = 2)
    @EqualsAndHashCode.Include
    private BigDecimal price;


}
