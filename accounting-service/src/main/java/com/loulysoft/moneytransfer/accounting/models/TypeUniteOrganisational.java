package com.loulysoft.moneytransfer.accounting.models;

import com.loulysoft.moneytransfer.accounting.enums.Niveau;
import com.loulysoft.moneytransfer.accounting.enums.NodeType;
import com.loulysoft.moneytransfer.accounting.enums.OuiNon;
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
public class TypeUniteOrganisational {

    private String code;

    private String description;

    private String libelle;

    private Niveau niveau;

    private TypeUniteOrganisational parent;

    private NodeType nodeType;

    private OuiNon autonome;

    private Integer gauche;

    private Integer droite;

    private Integer hauteur;

    Set<TypeCompte> typesCompte = new HashSet<>();
}
