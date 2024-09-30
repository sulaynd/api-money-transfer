package com.loulysoft.moneytransfer.accounting.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.loulysoft.moneytransfer.accounting.enums.Niveau;
import com.loulysoft.moneytransfer.accounting.enums.NodeType;
import com.loulysoft.moneytransfer.accounting.enums.OuiNon;
import java.util.Set;
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

    @JsonIgnore
    private TypeUniteOrganisational parent;

    private NodeType nodeType;

    private OuiNon autonome;

    private Integer gauche;

    private Integer droite;

    private Integer hauteur;

    @JsonIgnore
    Set<TypeCompte> typesCompte;
}
