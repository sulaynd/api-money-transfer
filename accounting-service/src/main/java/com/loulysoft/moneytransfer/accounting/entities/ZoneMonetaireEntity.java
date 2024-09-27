package com.loulysoft.moneytransfer.accounting.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "zone_monetaire")
public class ZoneMonetaireEntity {

    @Id
    @Column(name = "zm_code", nullable = false, unique = true)
    private String code;

    @Column(name = "zm_libelle")
    private String libelle;

    @ManyToOne
    @JoinColumn(name = "zm_dev_code")
    private DeviseEntity devise;

    @ManyToOne
    @JoinColumn(name = "zm_uo_id", unique = true)
    UniteOrganisationalEntity uniteOrganisational;
}
