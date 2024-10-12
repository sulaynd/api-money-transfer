package com.loulysoft.moneytransfer.accounting.entities;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "TRANSACTION_MOUVEMENT_SOLDE")
public class TransactionMouvementSoldeEntity {

    @EmbeddedId
    private TransactionMouvementSoldePK id;

    @Column(name = "TMS_STATUS", nullable = false)
    private Character status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TMS_TRANS_ID", insertable = false, updatable = false)
    private TransactionEntity transaction;

    @Column(name = "TMS_DATE")
    private LocalDateTime createdAt;
}
