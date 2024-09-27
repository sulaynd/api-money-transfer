package com.loulysoft.moneytransfer.accounting.models;

import java.time.LocalDateTime;
import java.util.HashSet;
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

    private Long userId;

    private Long schemaComptable;

    private Long entiteTierceId;

    private Long companyId;

    private String devise;

    private String paysDestination;

    private LocalDateTime createdAt;

    private Long transactionId;

    private String autreParametre;

    private Set<Operation> operations = new HashSet<>();

    private Long initialTransaction;

    private String natureService;

    private String paysSource;
}
