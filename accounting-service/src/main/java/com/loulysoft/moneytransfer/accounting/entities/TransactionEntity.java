package com.loulysoft.moneytransfer.accounting.entities;

import com.loulysoft.moneytransfer.accounting.enums.OuiNon;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "transaction")
public class TransactionEntity {

    @Id
    @Column(name = "trans_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "trans_id_generator")
    @SequenceGenerator(
            name = "trans_id_generator",
            allocationSize = 1,
            initialValue = 1000,
            sequenceName = "trans_id_seq")
    private Long id;
    // private SessionUtilisateur sessionUtilisateur;

    @JoinColumn(name = "trans_sc_id")
    @ManyToOne
    private SchemaComptableEntity schemaComptable;

    @Column(name = "trans_created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "trans_status")
    private Character status;

    @Enumerated(EnumType.STRING)
    @Column(name = "trans_annulation")
    private OuiNon annulation;

    @Column(name = "trans_log")
    private String log;

    @Column(name = "trans_send_code")
    private String sendCode;

    @Column(name = "trans_retrieved_code")
    private String retrievedCode;

    //    @ManyToOne(fetch = FetchType.LAZY)
    //    @JoinColumn(name = "trans_tt_code")
    //    private TransactionType transactionType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trans_root_id")
    private UniteOrganisationalEntity root;

    //    @ManyToOne(fetch = FetchType.LAZY)
    //    @JoinColumn(name = "trans_ref_id")
    //    private TransactionEntity refered;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trans_uo_id")
    private UniteOrganisationalEntity launchEntity;
    // private UniteOrganisationalEntity destinationAgency;
    // private Utilisateur lauchUser;

    // private PaysEntity destinationCountry;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trans_dev_code")
    private DeviseEntity devise;
    // private TransactionReportCategory reportCategory;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "transaction")
    private Set<OperationEntity> operations = new HashSet<>();
    //    private Set<InterventionTransaction> interventions = new HashSet<InterventionTransaction>();
    //
    //    private Set<TransactionPropertyItem> rtProperties = new HashSet<TransactionPropertyItem>();
    //
    //    private Set<TransactionMouvementSolde> transactionMouvementSoldes = new HashSet<>();

    //    @ManyToMany(fetch = FetchType.LAZY)
    //    @JoinTable(name = "TRANSACTION_RELATION", joinColumns = {
    //            @JoinColumn(name = "TR_TRANS_ID") }, inverseJoinColumns = { @JoinColumn(name = "TR_REF_ID") })
    //    private Set<TransactionEntity> transactions = new HashSet<>();

    @Column(name = "trans_is_notify")
    private Integer isNotify;

    @Column(name = "trans_pickup_code")
    private String pickupCode;

    @Column(name = "trans_sender_id")
    private String senderId;

    @Column(name = "trans_receiver_id")
    private String receiverId;

    @Column(name = "trans_initial_id")
    private Long initialTransaction;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trans_ser_code")
    private TypeServiceEntity service;
    //
    //        @ManyToOne(fetch = FetchType.LAZY)
    //        @JoinColumn(name = "trans_nat_ser_code")
    //        private NatureServiceEntity natureservice;
}
