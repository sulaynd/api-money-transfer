package com.loulysoft.moneytransfer.accounting.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionReport {
    @JsonIgnore
    private String message;

    @JsonIgnore
    private Character status;

    private Long reference;

    private LocalDateTime createdAt;

    private Long transactionId;

    @JsonIgnore
    private TypeService service;

    @Builder.Default
    private BigDecimal montant = BigDecimal.ZERO;

    @Builder.Default
    private BigDecimal montantAPayer = BigDecimal.ZERO;

    @Builder.Default
    private BigDecimal frais = BigDecimal.ZERO;

    @Builder.Default
    private BigDecimal timbre = BigDecimal.ZERO;

    @Builder.Default
    private BigDecimal taxes = BigDecimal.ZERO;

    @Builder.Default
    private BigDecimal principal = BigDecimal.ZERO;

    @Builder.Default
    private BigDecimal commissions = BigDecimal.ZERO;

    @Builder.Default
    private BigDecimal total = BigDecimal.ZERO;

    @Builder.Default
    private BigDecimal rate = BigDecimal.ZERO;

    @Builder.Default
    private String devise = null;

    @Builder.Default
    private String devisePayer = null;

    @Builder.Default
    private Solde solde = null;

    private Journal journal;

    private String uoName;

    private String uoParentName;

    private String uoRootName;

    @Builder.Default
    private boolean cancelable = false, updatable = false;
}
