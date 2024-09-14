package com.loulysoft.moneytransfer.ratings.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MontantSchemaComptable {

    private Long id;

    private String nom;

    private Integer rang;

    private String round;

    private SchemaComptable schema;

    private Parametre param;
}
