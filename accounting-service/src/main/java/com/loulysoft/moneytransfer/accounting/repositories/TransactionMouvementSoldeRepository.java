package com.loulysoft.moneytransfer.accounting.repositories;

import com.loulysoft.moneytransfer.accounting.entities.TransactionMouvementSoldeEntity;
import com.loulysoft.moneytransfer.accounting.entities.TransactionMouvementSoldePK;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionMouvementSoldeRepository
        extends JpaRepository<TransactionMouvementSoldeEntity, TransactionMouvementSoldePK> {}
