package com.loulysoft.moneytransfer.accounting.repositories;

import com.loulysoft.moneytransfer.accounting.entities.NatureServiceEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NatureServiceRepository extends JpaRepository<NatureServiceEntity, String> {

    Optional<NatureServiceEntity> findByCode(String code);
}
