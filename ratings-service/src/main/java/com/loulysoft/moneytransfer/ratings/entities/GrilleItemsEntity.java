package com.loulysoft.moneytransfer.ratings.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "grille_items")
public class GrilleItemsEntity {

    @Id
    @Column(name = "item_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "grid_item_id_generator")
    @SequenceGenerator(
            name = "grid_item_id_generator",
            allocationSize = 1,
            initialValue = 1000,
            sequenceName = "grid_item_id_seq")
    private Long id;

    @Column(name = "item_sequence")
    private Integer sequence;

    @Column(name = "item_borneInf", columnDefinition = "NUMERIC(19,2)")
    private BigDecimal borneInf;

    @Column(name = "item_borneSup", columnDefinition = "NUMERIC(19,2)")
    private BigDecimal borneSup;

    @Column(name = "item_value", columnDefinition = "NUMERIC(19,2)")
    private BigDecimal value;

    @Column(name = "item_marge", columnDefinition = "NUMERIC(19,2)")
    private BigDecimal marge;

    @Column(name = "item_pourcentage")
    private Character pourcentage;

    @ManyToOne
    @JoinColumn(name = "item_grid_id")
    private GrilleEntity grille;
}
