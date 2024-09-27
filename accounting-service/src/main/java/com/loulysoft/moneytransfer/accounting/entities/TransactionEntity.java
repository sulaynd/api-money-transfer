package com.loulysoft.moneytransfer.accounting.entities;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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

    @Column(name = "trans_user_id", nullable = false)
    private Long userId;

    @Column(name = "trans_schema_id", nullable = false)
    private Long schemaComptable;

    @Column(name = "trans_entite_tierce_id")
    private Long entiteTierceId;

    @Column(name = "trans_company_id")
    private Long companyId;

    @Column(name = "trans_devise", nullable = false)
    private String devise;

    @Column(name = "trans_pays_destination")
    private String paysDestination;

    @Column(name = "trans_date", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "trans_transaction_id")
    private Long transactionId;

    @Column(name = "trans_autre_parametre")
    private String autreParametre;

    @OneToMany(mappedBy = "transaction", cascade = CascadeType.ALL)
    private Set<OperationEntity> operations = new HashSet<>();
    // add 13082019
    @Column(name = "trans_initial_transaction")
    private Long initialTransaction;

    @Column(name = "trans_nature_service")
    private String natureService;

    @Column(name = "trans_pays_source")
    private String paysSource;
}
