package com.loulysoft.moneytransfer.accounting.repositories;

import com.loulysoft.moneytransfer.accounting.entities.CompteSchemaComptableEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompteSchemaComptableRepository extends JpaRepository<CompteSchemaComptableEntity, Long> {
    List<CompteSchemaComptableEntity> findBySchemaId(Long schemaId);
}
