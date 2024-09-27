package com.loulysoft.moneytransfer.accounting.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UniteOrganisational {
    private Long id;

    private String libelle;

    private Character status;

    private String code;

    private TypeUniteOrganisational type;

    private UniteOrganisational parent;

    private UniteOrganisational root;

    private Pays pays;
}
