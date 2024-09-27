package com.loulysoft.moneytransfer.accounting.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
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
@Table(name = "montant_param_schema_comptable")
public class MontantParamSchemaComptableEntity {
    @Id
    @Column(name = "mpsc_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "mpsc_id_generator")
    @SequenceGenerator(
            name = "mpsc_id_generator",
            allocationSize = 1,
            initialValue = 1000,
            sequenceName = "mpsc_id_seq")
    private Long id;

    @JoinColumn(name = "mpsc_pr_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private ParametreRechercheEntity search;

    @JoinColumn(name = "mpsc_msc_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private MontantSchemaComptableEntity montantSchema;
}
