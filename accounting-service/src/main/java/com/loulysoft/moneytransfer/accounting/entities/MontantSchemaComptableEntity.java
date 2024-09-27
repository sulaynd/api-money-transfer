package com.loulysoft.moneytransfer.accounting.entities;

import com.loulysoft.moneytransfer.accounting.enums.Round;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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

    @Enumerated(EnumType.STRING)
    @Column(name = "msc_round")
    private Round round;

    @JoinColumn(name = "msc_sc_id")
    @ManyToOne(optional = false)
    private SchemaComptableEntity schema;

    @JoinColumn(name = "msc_param_code")
    @ManyToOne(optional = false)
    private ParametreEntity param;

    @ManyToMany
    @JoinTable(
            name = "base_montant_schema_comptable",
            schema = "",
            joinColumns = {@JoinColumn(name = "bmsc_msc_id", nullable = false, updatable = false, insertable = false)},
            inverseJoinColumns = {
                @JoinColumn(name = "bmsc_base_id", nullable = false, updatable = false, insertable = false)
            })
    private Set<MontantSchemaComptableEntity> montantSchemaComptables = new HashSet<>(0);
}
