package com.loulysoft.moneytransfer.ratings.models;

import com.loulysoft.moneytransfer.ratings.utils.Niveau;
import com.loulysoft.moneytransfer.ratings.utils.NodeType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TypeUniteOrganisational {

    private String code;
    private String description;
    private String libelle;
    private Niveau niveau;
    private TypeUniteOrganisational parent;
    private NodeType nodeType;
}
