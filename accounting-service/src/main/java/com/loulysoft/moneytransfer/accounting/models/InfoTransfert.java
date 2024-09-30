package com.loulysoft.moneytransfer.accounting.models;

import com.loulysoft.moneytransfer.accounting.enums.OuiNon;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
// @Builder
public class InfoTransfert extends Journal {

    //    private String prenomEnvoyeur;
    //    private String nomEnvoyeur;
    //    private String telephoneEnvoyeur;
    //    private String prenomBeneficiaire;
    //    private String nomBeneficiaire;
    //    private String telephoneBeneficiaire;
    //    private String paysPiece;
    //    private String typePiece;
    //    private String numeroPiece;
    //    private LocalDate dateDelivrance;
    //    private LocalDate dateExpiration;
    private String code;
    private BigDecimal montantRecu;
    private OuiNon codePrinted;
    private Devise deviseDestination;
    private String receiverCode;
    private Pays paysDestination;
    private Pays paysOrigine;
}
