package com.loulysoft.moneytransfer.accounting.repositories;

import com.loulysoft.moneytransfer.accounting.entities.CalculMontantSchemaComptableEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CalculMontantSchemaComptableRepository
        extends JpaRepository<CalculMontantSchemaComptableEntity, Long> {
    //    @Query(
    //            """
    //              SELECT cmsc
    //              FROM CalculMontantSchemaComptableEntity cmsc
    //              WHERE cmsc.montantSchema.id =:montantSchemaId
    //              ORDER BY cmsc.rang
    //        """)
    List<CalculMontantSchemaComptableEntity> findByMontantSchemaIdOrderByRang(Long montantSchemaId);
}
