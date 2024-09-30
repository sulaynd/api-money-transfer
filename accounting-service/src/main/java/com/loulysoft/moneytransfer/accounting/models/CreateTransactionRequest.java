package com.loulysoft.moneytransfer.accounting.models;

import com.loulysoft.moneytransfer.accounting.enums.OuiNon;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

public record CreateTransactionRequest(
        @Valid @NotEmpty(message = "Status cannot be empty") Character status,
        @Valid @NotEmpty(message = "Accounting schema cannot be empty") SchemaComptable schemaComptable,
        @Valid @NotEmpty(message = "Attribute annulation cannot be empty") OuiNon annulation,
        @Valid @NotEmpty(message = "Root company  cannot be empty") UniteOrganisational root,
        @Valid @NotEmpty(message = "Company cannot be empty") UniteOrganisational launchEntity,
        @Valid @NotEmpty(message = "Service cannot be empty") TypeService service,
        @Valid @NotEmpty(message = "Currency cannot be empty") Devise devise,
        /*@Valid @NotEmpty(message = "Currency cannot be empty")*/ String pickupCode,
        /*@Valid @NotEmpty(message = "Currency cannot be empty")*/ Long initialTransaction) {}
