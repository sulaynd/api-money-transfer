package com.loulysoft.moneytransfer.accounting.repositories;

import com.loulysoft.moneytransfer.accounting.entities.OperationEntity;
import com.loulysoft.moneytransfer.accounting.enums.Code;
import com.loulysoft.moneytransfer.accounting.enums.DebitCredit;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface OperationRepository extends JpaRepository<OperationEntity, Long> {

    @Query(
            """
           SELECT o FROM OperationEntity o
           JOIN o.ecritureSchemaComptable esc
           JOIN o.compte c
           WHERE esc.writer.code = :code
           AND esc.direction = :direction
           AND o.transaction.id = :transactionId
           AND c.typeCompte.code = :typeCompte
        """)
    Optional<OperationEntity> findOperationByCodeAndDirectionAndTransactionIdAndTypeCompte(
            Code code, DebitCredit direction, Long transactionId, String typeCompte);
}
