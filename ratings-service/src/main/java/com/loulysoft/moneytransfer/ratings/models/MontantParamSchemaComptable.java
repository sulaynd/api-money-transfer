package com.loulysoft.moneytransfer.ratings.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MontantParamSchemaComptable {

    private Long id;

    private ParametreRecherche parametreRecherche;

    private MontantSchemaComptable montantSchema;
}
