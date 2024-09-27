package com.loulysoft.moneytransfer.accounting.repositories;

import com.loulysoft.moneytransfer.accounting.entities.MontantSchemaComptableEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MontantSchemaComptableRepository extends JpaRepository<MontantSchemaComptableEntity, Long> {

    List<MontantSchemaComptableEntity> findBySchemaIdOrderByRangAsc(Long schemaId);
}
