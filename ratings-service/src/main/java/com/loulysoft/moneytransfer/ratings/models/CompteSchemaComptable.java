package com.loulysoft.moneytransfer.ratings.models;

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

    private Compte account;
}
