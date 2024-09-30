package com.loulysoft.moneytransfer.accounting.models;

import com.loulysoft.moneytransfer.accounting.entities.MontantSchemaComptableEntity;
import com.loulysoft.moneytransfer.accounting.entities.TransactionTmpEntity;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OperationRequest {
    private TransactionTmpEntity transaction;

    private MontantSchemaComptableEntity montantSchema;

    private BigDecimal montant;
}
