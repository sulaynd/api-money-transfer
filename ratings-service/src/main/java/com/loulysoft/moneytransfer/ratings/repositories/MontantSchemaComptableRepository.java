package com.loulysoft.moneytransfer.ratings.repositories;

import com.loulysoft.moneytransfer.ratings.entities.MontantSchemaComptableEntity;
import com.loulysoft.moneytransfer.ratings.models.MontantSchemaComptable;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MontantSchemaComptableRepository extends JpaRepository<MontantSchemaComptableEntity, Long> {

    Optional<MontantSchemaComptable> findBySchemaId(Long schemaComptableId);
}
