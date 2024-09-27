package com.loulysoft.moneytransfer.accounting.models;

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
public class Compte {

    private Long id;

    private BigDecimal solde;

    private BigDecimal minSolde;

    private BigDecimal maxSolde;

    private TypeCompte typeCompte;

    private UniteOrganisational owner;

    private LocalDateTime dernierMouvement;
}
