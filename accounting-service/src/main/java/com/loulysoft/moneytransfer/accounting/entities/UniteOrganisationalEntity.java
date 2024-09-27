package com.loulysoft.moneytransfer.accounting.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "unite_organisational")
public class UniteOrganisationalEntity {

    @Id
    @Column(name = "uo_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "uo_id_generator")
    @SequenceGenerator(name = "uo_id_generator", allocationSize = 1, initialValue = 1000, sequenceName = "uo_id_seq")
    private Long id;

    @Column(name = "uo_code", nullable = false, unique = true)
    private String code;

    @Column(name = "uo_libelle", length = 128)
    private String libelle;

    @Column(name = "uo_statut", length = 1)
    private Character status;

    @ManyToOne
    @JoinColumn(name = "uo_tuo_code")
    private TypeUniteOrganisationalEntity type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "uo_parent_id")
    private UniteOrganisationalEntity parent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "uo_root_id")
    private UniteOrganisationalEntity root;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinTable(
            name = "uo_pays",
            joinColumns = {@JoinColumn(name = "uop_uo_id")},
            inverseJoinColumns = {@JoinColumn(name = "uop_ps_code")})
    private PaysEntity pays;
}
