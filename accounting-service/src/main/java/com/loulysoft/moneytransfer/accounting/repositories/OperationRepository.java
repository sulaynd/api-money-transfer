package com.loulysoft.moneytransfer.accounting.repositories;

import com.loulysoft.moneytransfer.accounting.entities.OperationEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OperationRepository extends JpaRepository<OperationEntity, Long> {

    Optional<OperationEntity> findByTransactionIdAndMontantSchemaId(Long transactionId, Long montantSchemaId);
}
