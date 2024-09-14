package com.loulysoft.moneytransfer.ratings.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "pays")
public class PaysEntity {
    @Id
    @Column(name = "ps_code", nullable = false, unique = true)
    @NotEmpty(message = "Country code is required")
    private String code;

    @Column(name = "ps_libelle", nullable = false)
    @NotEmpty(message = "Country name is required")
    private String libelle;

    @Column(name = "ps_statut")
    private Character statut;

    @Column(name = "ps_indicatif")
    private String indicatif;

    @Column(name = "ps_language")
    private String language;

    @ManyToOne(optional = false)
    @JoinColumn(name = "ps_dev_code")
    private DeviseEntity devise;
}
