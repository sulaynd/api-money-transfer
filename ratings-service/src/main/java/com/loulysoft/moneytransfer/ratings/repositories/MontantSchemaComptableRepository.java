package com.loulysoft.moneytransfer.ratings.repositories;

import com.loulysoft.moneytransfer.ratings.entities.MontantSchemaComptableEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MontantSchemaComptableRepository extends JpaRepository<MontantSchemaComptableEntity, Long> {

    Optional<MontantSchemaComptableEntity> findBySchemaIdOrderByRangAsc(Long schemaId);
}
