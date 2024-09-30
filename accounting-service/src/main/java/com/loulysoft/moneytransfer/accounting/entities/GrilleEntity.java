package com.loulysoft.moneytransfer.accounting.entities;

import com.loulysoft.moneytransfer.accounting.enums.TypeGrille;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "grille")
public class GrilleEntity {
    @Id
    @Column(name = "grid_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "grid_id_generator")
    @SequenceGenerator(
            name = "grid_id_generator",
            allocationSize = 1,
            initialValue = 1000,
            sequenceName = "grid_id_seq")
    private Long id;

    @Column(name = "grid_description")
    private String description;

    @Column(name = "grid_valeur", nullable = false)
    private String value;

    @Enumerated(EnumType.STRING)
    @Column(name = "grid_type")
    private TypeGrille type;

    // @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "grille")
    // private Set<GrilleItemEntity> paliers;
}
