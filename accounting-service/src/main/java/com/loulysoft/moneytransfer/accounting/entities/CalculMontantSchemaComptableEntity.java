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
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "calcul_montant_schema_comptable")
public class CalculMontantSchemaComptableEntity {

    @Id
    @Column(name = "cmsc_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cmsc_id_generator")
    @SequenceGenerator(
            name = "cmsc_id_generator",
            allocationSize = 1,
            initialValue = 1000,
            sequenceName = "cmsc_id_seq")
    private Long id;

    @JoinColumn(name = "cmsc_msc_id", referencedColumnName = "msc_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private MontantSchemaComptableEntity montantSchema;

    @Column(name = "cmsc_constante_param")
    private BigDecimal constanteParam;

    @JoinColumn(name = "cmsc_montant_param")
    @ManyToOne(fetch = FetchType.LAZY)
    private MontantSchemaComptableEntity montantParam;
    //    @Column(name = "cmsc_montant_param")
    //    private Long montantParam;

    @JoinColumn(name = "cmsc_oa_operateur")
    @ManyToOne(fetch = FetchType.LAZY)
    private OperateurArithmetiqueEntity operateurArithmetique;

    @Column(name = "cmsc_rang", nullable = false)
    private Integer rang;
}
