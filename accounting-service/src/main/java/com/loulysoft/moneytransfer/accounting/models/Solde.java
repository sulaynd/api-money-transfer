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
public class Solde {

    private Long compteId;

    private BigDecimal valeur;

    private BigDecimal disponible;

    private String devise;

    private BigDecimal commissionJour;

    private String reference;

    private BigDecimal decouvert;

    private boolean visible;

    public Solde(Long compteId, BigDecimal valeur, BigDecimal disponible, String devise) {
        super();
        this.compteId = compteId;
        this.valeur = valeur;
        this.disponible = disponible;
        this.devise = devise;
    }
}
