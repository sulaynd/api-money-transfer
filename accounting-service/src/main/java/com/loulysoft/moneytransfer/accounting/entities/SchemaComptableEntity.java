package com.loulysoft.moneytransfer.accounting.entities;

import com.loulysoft.moneytransfer.accounting.enums.Variant;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
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
@Table(
        name = "schema_comptable",
        indexes = {@Index(name = "idx_type_company", columnList = "typeCompany")})
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

    @Enumerated(EnumType.STRING)
    @Column(name = "sc_variant")
    private Variant variant;

    @Column(name = "sc_description")
    private String description;

    // @ManyToOne(optional = false)
    //    @Column(name = "sc_ser_code")
    //    private String serviceCode;

    // @ManyToOne(optional = false)
    //    @Column(name = "sc_tuo_code")
    //    private String typeCompany;

    @ManyToOne
    @JoinColumn(name = "sc_ser_code")
    private TypeServiceEntity service;

    @ManyToOne
    @JoinColumn(name = "sc_tuo_code")
    private TypeUniteOrganisationalEntity type;
}
