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
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "compte_schema_comptable")
public class CompteSchemaComptableEntity {
    @Id
    @Column(name = "csc_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "csc_id_generator")
    @SequenceGenerator(name = "csc_id_generator", allocationSize = 1, initialValue = 1000, sequenceName = "csc_id_seq")
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "csc_sc_id")
    private SchemaComptableEntity schema;

    @ManyToOne(optional = false)
    @JoinColumn(name = "csc_pr_id")
    private ParametreRechercheEntity search;

    @ManyToOne
    @JoinColumn(name = "csc_tc_code")
    private CompteEntity account;
}
