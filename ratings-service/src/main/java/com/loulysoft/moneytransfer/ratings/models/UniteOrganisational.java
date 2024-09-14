package com.loulysoft.moneytransfer.ratings.models;

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

    private String description;

    private Character status;

    private String code;

    private TypeUniteOrganisational type;

    private UniteOrganisational parent, root;

    private Pays pays;
}
