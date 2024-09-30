package com.loulysoft.moneytransfer.accounting.repositories;

import com.loulysoft.moneytransfer.accounting.entities.TransactionTmpEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TransactionTmpRepository extends JpaRepository<TransactionTmpEntity, Long> {

    @Query(
            """
        select distinct t
        from TransactionTmpEntity t left join fetch t.operations
        where t.id = :transactionId
        """)
    Optional<TransactionTmpEntity> findByTransactionId(Long transactionId);
}
