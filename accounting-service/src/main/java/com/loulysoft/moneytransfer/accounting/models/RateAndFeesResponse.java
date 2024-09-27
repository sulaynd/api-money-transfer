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
public class RateAndFeesResponse {

    private String devisePayer;

    private BigDecimal exchangeRate;

    private BigDecimal principal;

    private BigDecimal commissions;

    private BigDecimal timbre;

    private BigDecimal taxes;

    private BigDecimal total;

    private BigDecimal totalReceived;
}
