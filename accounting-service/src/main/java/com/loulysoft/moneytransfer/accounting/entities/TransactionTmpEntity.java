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
@Table(name = "transaction_tmp")
public class TransactionTmpEntity {
    @Id
    @Column(name = "tra_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tra_id_generator")
    @SequenceGenerator(name = "tra_id_generator", allocationSize = 1, initialValue = 1000, sequenceName = "tra_id_seq")
    private Long id;

    @Column(name = "tra_user_id", nullable = false)
    private Long userId;

    @Column(name = "tra_schema_id", nullable = false)
    private Long schemaComptableId;

    @Column(name = "tra_entite_tierce_id")
    private Long entiteTierceId;

    @Column(name = "tra_company_id")
    private Long companyId;

    @Column(name = "tra_devise", nullable = false)
    private String devise;

    @Column(name = "tra_pays_destination")
    private String paysDestination;

    @Column(name = "tra_created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "tra_transaction_id")
    private Long transactionId;

    @Column(name = "tra_autre_parametre")
    private String autreParametre;

    @OneToMany(mappedBy = "transaction", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<OperationTmpEntity> operations = new HashSet<>();

    @Column(name = "tra_initial_transaction")
    private Long initialTransaction;

    @Column(name = "tra_nature_service")
    private String natureService;

    @Column(name = "tra_pays_source")
    private String paysSource;
}
