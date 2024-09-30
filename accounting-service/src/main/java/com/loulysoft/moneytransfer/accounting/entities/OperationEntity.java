package com.loulysoft.moneytransfer.accounting.entities;

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
import lombok.ToString;

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

    @Column(name = "op_direction")
    private Character direction;

    @Column(name = "op_sequence")
    private Integer sequence;

    @Column(name = "op_amount", scale = 4)
    private BigDecimal amount;

    @Column(name = "op_new_solde")
    private BigDecimal newSolde;

    @ManyToOne
    @JoinColumn(name = "op_trans_id")
    @ToString.Exclude
    private TransactionEntity transaction;

    @ManyToOne
    @JoinColumn(name = "op_cmp_id")
    private CompteEntity compte;

    @Column(name = "op_code")
    private String code;

    @ManyToOne
    @JoinColumn(name = "op_esc_id")
    private EcritureSchemaComptableEntity ecritureSchemaComptable;
}
