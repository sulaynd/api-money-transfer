package com.loulysoft.moneytransfer.accounting.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
public class TransactionTmp {

    private Long id;

    private Long userId;

    @JsonIgnore
    private Long schemaComptableId;

    private Long entiteTierceId;

    private Long companyId;

    private String devise;

    private String paysDestination;

    private LocalDateTime createdAt;

    private Long transactionId;

    private String autreParametre;

    @JsonIgnore
    private Set<OperationTmp> operations;

    private Long initialTransaction;

    @JsonIgnore
    private String natureService;

    private String paysSource;
    //    public void setOperations(Set<OperationTmp> ops) {
    //        this.operations.clear();
    //        if (ops != null) {
    //            this.operations.addAll(ops);
    //        }
    //    }
}
