package com.loulysoft.moneytransfer.accounting.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OperationTmp {

    private Long id;

    @JsonIgnore
    private TransactionTmp transaction;

    private MontantSchemaComptable montantSchema;

    private BigDecimal montant;
}
