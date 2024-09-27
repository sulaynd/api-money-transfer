package com.loulysoft.moneytransfer.accounting.models;

import com.loulysoft.moneytransfer.accounting.enums.DebitCredit;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Journal {
    // private Transaction transaction;
    private BigDecimal montant = BigDecimal.ZERO;

    private BigDecimal commissionHorsTax_0 = BigDecimal.ZERO;

    private BigDecimal commissionTax_0 = BigDecimal.ZERO;

    private BigDecimal commissionHorsTax_1 = BigDecimal.ZERO;

    private BigDecimal commissionTax_1 = BigDecimal.ZERO;

    private BigDecimal commissionHorsTax_2 = BigDecimal.ZERO;

    private BigDecimal commissionTax_2 = BigDecimal.ZERO;

    private BigDecimal commissionHorsTax_3 = BigDecimal.ZERO;

    private BigDecimal commissionTax_3 = BigDecimal.ZERO;

    private BigDecimal fees = BigDecimal.ZERO;

    private BigDecimal timbre = BigDecimal.ZERO;

    private BigDecimal taxes = BigDecimal.ZERO;

    private UniteOrganisational uniteOrganisational;

    private TypeUniteOrganisational typeUniteOrganisational;

    private Devise devise;

    private DebitCredit typeOperation;

    private String comment;

    //    private Long usrId;
    //    private String usrPrenom;
    //    private String usrNom;
    //    private String usrUniteOrganisationnelle;

    //    private String prenomSender;
    //    private String nomSender;
    //    private String telSender;
    //    private String nomBenef;
    //    private String prenomBenef;
    //    private String telBenef;
    //    private Pays paysDest;
}
