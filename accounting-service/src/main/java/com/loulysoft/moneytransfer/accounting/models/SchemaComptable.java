package com.loulysoft.moneytransfer.accounting.models;

import com.loulysoft.moneytransfer.accounting.enums.Variant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SchemaComptable {
    private Long id;

    private Character status;

    private Integer version;

    private Variant variant;

    private String description;

    // private String serviceCode;

    // private String typeCompany;

    private TypeService service;

    private TypeUniteOrganisational type;
}
