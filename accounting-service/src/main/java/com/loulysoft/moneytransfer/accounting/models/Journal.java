package com.loulysoft.moneytransfer.accounting.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.loulysoft.moneytransfer.accounting.enums.DebitCredit;
import jakarta.persistence.Transient;
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
public class Journal {

    private Long id;

    private LocalDateTime createdAt;
    private Transaction transaction;

    @Builder.Default
    private BigDecimal montant = BigDecimal.ZERO;

    @Builder.Default
    private BigDecimal commissionHorsTaxe_0 = BigDecimal.ZERO;

    @Builder.Default
    private BigDecimal commissionTaxe_0 = BigDecimal.ZERO;

    @Builder.Default
    private BigDecimal commissionHorsTaxe_1 = BigDecimal.ZERO;

    @Builder.Default
    private BigDecimal commissionTaxe_1 = BigDecimal.ZERO;

    @Builder.Default
    private BigDecimal commissionHorsTaxe_2 = BigDecimal.ZERO;

    @Builder.Default
    private BigDecimal commissionTaxe_2 = BigDecimal.ZERO;

    @Builder.Default
    private BigDecimal commissionHorsTaxe_3 = BigDecimal.ZERO;

    @Builder.Default
    private BigDecimal commissionTaxe_3 = BigDecimal.ZERO;

    @Builder.Default
    private BigDecimal frais = BigDecimal.ZERO;

    @Builder.Default
    private BigDecimal timbre = BigDecimal.ZERO;

    @Builder.Default
    private BigDecimal taxes = BigDecimal.ZERO;

    @JsonIgnore
    private UniteOrganisational uniteOrganisational;

    private Devise devise;

    @JsonIgnore
    private DebitCredit typeOperation;

    @JsonIgnore
    private String comment;

    @JsonIgnore
    @Transient
    private Long usrId;

    @JsonIgnore
    @Transient
    private String usrPrenom;

    @JsonIgnore
    @Transient
    private String usrNom;

    @JsonIgnore
    @Transient
    private String usruniteOrganisational;

    @JsonIgnore
    @Transient
    private TypeUniteOrganisational typeUniteOrganisational;

    @JsonIgnore
    @Transient
    private String prenomSender;

    @JsonIgnore
    @Transient
    private String nomSender;

    @JsonIgnore
    @Transient
    private String telSender;

    @JsonIgnore
    @Transient
    private String nomBenef;

    @JsonIgnore
    @Transient
    private String prenomBenef;

    @JsonIgnore
    @Transient
    private String telBenef;

    @Transient
    @JsonProperty(value = "paysDestination")
    private Pays paysDest;

    @Transient
    // @JsonProperty(value = "paysSource")
    private Pays paysOrigine;
}
