package com.loulysoft.moneytransfer.accounting.entities;

import com.loulysoft.moneytransfer.accounting.enums.DebitCredit;
import com.loulysoft.moneytransfer.accounting.enums.UniteOrganisationalType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "journal")
public class JournalEntity {

    @Id
    @Column(name = "jou_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "jou_id_generator")
    @SequenceGenerator(name = "jou_id_generator", allocationSize = 1, initialValue = 1000, sequenceName = "jou_id_seq")
    private Long id;

    @Column(name = "jou_created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "jou_trans_id")
    private TransactionEntity transaction;

    @Column(name = "jou_montant")
    private BigDecimal montant = BigDecimal.ZERO;

    @Column(name = "jou_com_ht0")
    private BigDecimal commissionHorsTaxe_0 = BigDecimal.ZERO;

    @Column(name = "jou_com_tax0")
    private BigDecimal commissionTaxe_0 = BigDecimal.ZERO;

    @Column(name = "jou_com_ht1")
    private BigDecimal commissionHorsTaxe_1 = BigDecimal.ZERO;

    @Column(name = "jou_com_tax1")
    private BigDecimal commissionTaxe_1 = BigDecimal.ZERO;

    @Column(name = "jou_com_ht2")
    private BigDecimal commissionHorsTaxe_2 = BigDecimal.ZERO;

    @Column(name = "jou_com_tax2")
    private BigDecimal commissionTaxe_2 = BigDecimal.ZERO;

    @Column(name = "jou_com_ht3")
    private BigDecimal commissionHorsTaxe_3 = BigDecimal.ZERO;

    @Column(name = "jou_com_tax3")
    private BigDecimal commissionTaxe_3 = BigDecimal.ZERO;

    @Column(name = "jou_frais")
    private BigDecimal frais = BigDecimal.ZERO;

    @Column(name = "jou_timbre")
    private BigDecimal timbre = BigDecimal.ZERO;

    @Column(name = "jou_taxes")
    private BigDecimal taxes = BigDecimal.ZERO;

    @ManyToOne
    @JoinColumn(name = "jou_uo_id")
    private UniteOrganisationalEntity uniteOrganisational;

    @ManyToOne
    @JoinColumn(name = "jou_dev_code")
    private DeviseEntity devise;

    @Enumerated(EnumType.STRING)
    @Column(name = "jou_type_operation")
    private DebitCredit typeOperation;

    @Column(name = "jou_comment")
    private String comment;

    @Transient
    private Long usrId;

    @Transient
    private String usrPrenom;

    @Transient
    private String usrNom;

    @Transient
    private String usrUniteOrganisationnelle;

    @Transient
    private UniteOrganisationalType uniteOrganisationalType;

    @Transient
    private String prenomSender;

    @Transient
    private String nomSender;

    @Transient
    private String telSender;

    @Transient
    private String nomBenef;

    @Transient
    private String prenomBenef;

    @Transient
    private String telBenef;

    @Transient
    private PaysEntity paysDest;
}
