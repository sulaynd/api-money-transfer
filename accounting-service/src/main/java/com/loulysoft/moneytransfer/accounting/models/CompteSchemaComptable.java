package com.loulysoft.moneytransfer.accounting.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompteSchemaComptable {

    private Long id;

    private SchemaComptable schema;

    private ParametreRecherche search;

    private TypeCompte typeCompte;
    ;
}
