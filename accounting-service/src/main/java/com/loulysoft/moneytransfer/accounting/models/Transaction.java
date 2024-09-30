package com.loulysoft.moneytransfer.accounting.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.loulysoft.moneytransfer.accounting.enums.OuiNon;
import java.time.LocalDateTime;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Transaction {

    private Long id;

    @JsonIgnore
    private SchemaComptable schemaComptable;

    private LocalDateTime createdAt;

    @JsonIgnore
    private Character status;

    @JsonIgnore
    private OuiNon annulation;

    @JsonIgnore
    private String log;

    private String sendCode;

    private String retrievedCode;

    @JsonIgnore
    private UniteOrganisational root;

    @JsonIgnore
    private UniteOrganisational launchEntity;

    @JsonIgnore
    private Devise devise;

    @JsonIgnore
    private Set<Operation> operations;

    @JsonIgnore
    private Integer isNotify;

    private String pickupCode;

    @JsonIgnore
    private TypeService service;

    @JsonIgnore
    private NatureService natureservice;

    private Long initialTransaction;
}
