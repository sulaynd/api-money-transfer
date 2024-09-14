package com.loulysoft.moneytransfer.ratings.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "schema_comptable")
public class SchemaComptableEntity {

    @Id
    @Column(name = "sc_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sc_id_generator")
    @SequenceGenerator(name = "sc_id_generator", allocationSize = 1, initialValue = 1000, sequenceName = "sc_id_seq")
    private Long id;

    @Column(name = "sc_status")
    private Character status;

    @Version
    @Column(name = "sc_version")
    private Integer version;

    @Column(name = "sc_variant")
    private String variant;

    @Column(name = "sc_description")
    private String description;

    @ManyToOne(optional = false)
    @JoinColumn(name = "sc_ser_code")
    private TypeServiceEntity service;

    @ManyToOne(optional = false)
    @JoinColumn(name = "sc_tuo_code")
    private TypeUniteOrganisationalEntity type;
}
