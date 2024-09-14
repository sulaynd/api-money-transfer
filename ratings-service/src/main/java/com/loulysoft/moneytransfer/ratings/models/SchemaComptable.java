package com.loulysoft.moneytransfer.ratings.models;

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
    private String variant;
    private String description;
    private TypeService service;
    private TypeUniteOrganisational type;
}
