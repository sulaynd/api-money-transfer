package com.loulysoft.moneytransfer.ratings.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import java.util.HashSet;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "montant_schema_comptable")
public class MontantSchemaComptableEntity {

    @Id
    @Column(name = "msc_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "msc_id_generator")
    @SequenceGenerator(name = "msc_id_generator", allocationSize = 1, initialValue = 1000, sequenceName = "msc_id_seq")
    private Long id;

    @Column(name = "msc_nom")
    private String nom;

    @Column(name = "msc_rang")
    private Integer rang;

    @Column(name = "msc_round")
    private String round;

    @JoinColumn(name = "msc_sc_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private SchemaComptableEntity schema;

    @JoinColumn(name = "msc_param_code")
    @ManyToOne(fetch = FetchType.LAZY)
    private ParametreEntity param;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "base_montant_schema_comptable",
            schema = "",
            joinColumns = {@JoinColumn(name = "bmsc_msc_id", nullable = false, updatable = false, insertable = false)},
            inverseJoinColumns = {
                @JoinColumn(name = "bmsc_base_id", nullable = false, updatable = false, insertable = false)
            })
    private Set<MontantSchemaComptableEntity> montantSchemas = new HashSet<>(0);
}
