package com.loulysoft.moneytransfer.accounting.models;

import com.loulysoft.moneytransfer.accounting.enums.DebitCredit;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EcritureSchemaComptable {

    private Long id;

    private CodeEcriture writer;

    private SchemaComptable schema;

    private CompteSchemaComptable account;

    private MontantSchemaComptable montantSchema;

    private DebitCredit direction;

    private Integer rang;
}
