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
public class CalculMontantSchemaComptable {

    private Long id;

    private MontantSchemaComptable montantSchema;

    private BigDecimal constanteParam;

    private MontantSchemaComptable montantParam;
    // private Long montantParam;

    private OperateurArithmetique operateurArithmetique;

    private Integer rang;
}
