package com.loulysoft.moneytransfer.accounting.entities;

import com.loulysoft.moneytransfer.accounting.enums.Niveau;
import com.loulysoft.moneytransfer.accounting.enums.NodeType;
import com.loulysoft.moneytransfer.accounting.enums.OuiNon;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.HashSet;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "type_unite_organisational")
public class TypeUniteOrganisationalEntity {

    @Id
    @Column(name = "tuo_code", nullable = false, unique = true)
    private String code;

    @Column(name = "tuo_description")
    private String description;

    @Column(name = "tuo_libelle")
    private String libelle;

    @Enumerated(EnumType.STRING)
    @Column(name = "tuo_niveau")
    private Niveau niveau;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tuo_parent_code")
    private TypeUniteOrganisationalEntity parent;

    @Enumerated(EnumType.STRING)
    @Column(name = "tuo_node_type")
    private NodeType nodeType;

    @Enumerated(EnumType.STRING)
    @Column(name = "tuo_autonome")
    private OuiNon autonome;

    @Column(name = "tuo_gauche")
    private Integer gauche;

    @Column(name = "tuo_droite")
    private Integer droite;

    @Column(name = "tuo_hauteur")
    private Integer hauteur;

    @ManyToMany(mappedBy = "typeUniteOrganisationals")
    Set<TypeCompteEntity> typesCompte = new HashSet<>();
}
