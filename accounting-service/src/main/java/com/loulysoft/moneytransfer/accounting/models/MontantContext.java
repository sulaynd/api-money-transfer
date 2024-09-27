package com.loulysoft.moneytransfer.accounting.models;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MontantContext {

    private Long transactionId;

    private Long montantSchemaId;

    private String paysDestination;

    private Long entiteTierceId;

    private Long companyId;

    private String paysSource;

    private BigDecimal montantDeBase;

    private TransactionContext transactionContext;
}
