package com.loulysoft.moneytransfer.accounting.repositories;

import com.loulysoft.moneytransfer.accounting.entities.OperationTmpEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OperationTmpRepository extends JpaRepository<OperationTmpEntity, Long> {

    Optional<OperationTmpEntity> findByTransactionIdAndMontantSchemaId(Long transactionId, Long montantSchemaId);

    List<OperationTmpEntity> findByTransactionId(Long transactionId);

    //    String ql = "select o from Operation o join o.ecritureSchemaComptable esc join o.compte c where
    // esc.codeEcriture.code =:code"
    //            + " and esc.direction =:direction and o.transaction.id =:transactionId and c.typeCompte.code
    // =:typeCompte";
    //
    //    @Query(
    //            """
    //            SELECT o FROM OperationEntity o
    //            JOIN o.ecritureSchemaComptable esc
    //            JOIN o.compte c
    //            WHERE esc.codeEcriture.code =:code"
    //            AND esc.direction =:direction
    //            AND o.transaction.id =:transactionId
    //            AND c.typeCompte.code =:typeCompte
    //        """)
}
