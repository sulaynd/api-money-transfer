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
public class Operation {

    private Long id;

    private Transaction transaction;

    private MontantSchemaComptable montantSchema;

    private BigDecimal montant;
}
