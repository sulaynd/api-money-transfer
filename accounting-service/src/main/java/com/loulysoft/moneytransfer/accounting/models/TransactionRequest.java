package com.loulysoft.moneytransfer.accounting.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionRequest {
    private Long userId;

    private Long schemaComptableId;

    private Long entiteTierceId;

    private Long companyId;

    private String devise;

    private String paysDestination;

    private String autreParametre;

    private Long initialTransaction;

    private String natureService;

    private String paysSource;
}
