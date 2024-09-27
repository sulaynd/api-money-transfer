package com.loulysoft.moneytransfer.accounting.repositories;

import com.loulysoft.moneytransfer.accounting.entities.TypeServiceEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TypeServiceRepository extends JpaRepository<TypeServiceEntity, String> {
    Optional<TypeServiceEntity> findByCode(String code);
}
