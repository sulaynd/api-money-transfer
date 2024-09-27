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
@Table(name = "operation")
public class OperationEntity {
    @Id
    @Column(name = "op_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "op_id_generator")
    @SequenceGenerator(name = "op_id_generator", allocationSize = 1, initialValue = 1000, sequenceName = "op_id_seq")
    private Long id;

    @JoinColumn(name = "op_trans_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private TransactionEntity transaction;

    @JoinColumn(name = "op_msc_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private MontantSchemaComptableEntity montantSchema;

    @Column(name = "op_montant", scale = 4)
    private BigDecimal montant;
}
