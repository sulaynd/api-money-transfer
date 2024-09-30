package com.loulysoft.moneytransfer.accounting.models;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Operations {

    private Long id;
    private Character direction;
    private Integer sequence;
    private BigDecimal amount, newSolde;
    private TransactionTmp transaction;
    private Compte compte;
    private String code;
    private EcritureSchemaComptable ecritureSchemaComptable;
}
