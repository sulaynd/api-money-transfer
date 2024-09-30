package com.loulysoft.moneytransfer.accounting.models;

import java.math.BigDecimal;

public record OperationTemp(TransactionTmp transaction, MontantSchemaComptable montantSchema, BigDecimal montant) {}
