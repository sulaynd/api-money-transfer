package com.loulysoft.moneytransfer.accounting.models;

import com.loulysoft.moneytransfer.accounting.enums.Round;
import java.util.HashSet;
import java.util.Set;
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

    private Round round;

    private SchemaComptable schema;

    private Parametre param;

    private Set<MontantSchemaComptable> montantSchemaComptables = new HashSet<>(0);
}
