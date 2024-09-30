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
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "operation_tmp")
public class OperationTmpEntity {
    @Id
    @Column(name = "ope_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ope_id_generator")
    @SequenceGenerator(name = "ope_id_generator", allocationSize = 1, initialValue = 1000, sequenceName = "ope_id_seq")
    private Long id;

    @JoinColumn(name = "ope_tra_id")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @ToString.Exclude
    private TransactionTmpEntity transaction;

    @JoinColumn(name = "ope_msc_id")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private MontantSchemaComptableEntity montantSchema;

    @Column(name = "ope_montant", scale = 4)
    private BigDecimal montant;
}
