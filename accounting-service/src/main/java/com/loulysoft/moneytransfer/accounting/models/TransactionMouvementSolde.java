package com.loulysoft.moneytransfer.accounting.models;

import com.loulysoft.moneytransfer.accounting.entities.TransactionMouvementSoldePK;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionMouvementSolde {

    private TransactionMouvementSoldePK id;

    private Character status;

    private Transaction transaction;

    private LocalDateTime createdAt;
}
