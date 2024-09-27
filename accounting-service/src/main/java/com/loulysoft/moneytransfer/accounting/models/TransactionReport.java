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
public class TransactionReport {
    //    private String message;
    //
    //    private Character status;

    private Long transactionId;

    //    private LocalDateTime createdAt;
    //
    //    private TypeService service;

    private BigDecimal montant = BigDecimal.ZERO;

    private BigDecimal montantAPayer = BigDecimal.ZERO;

    private BigDecimal frais = BigDecimal.ZERO;

    private BigDecimal timbre = BigDecimal.ZERO;

    private BigDecimal taxes = BigDecimal.ZERO;

    private BigDecimal principal = BigDecimal.ZERO;

    private BigDecimal commissions = BigDecimal.ZERO;

    private BigDecimal total = BigDecimal.ZERO;

    private BigDecimal rate = BigDecimal.ZERO;

    private String devise = null;

    private String devisePayer = null;

    //    private Solde solde = null;
    //
    //    private Journal journal;
    //
    //    private String uoName;
    //
    //    private String uoParentName;
    //
    //    private String uoRootName;
    //
    //    private boolean cancelable = false, updatable = false;
}
