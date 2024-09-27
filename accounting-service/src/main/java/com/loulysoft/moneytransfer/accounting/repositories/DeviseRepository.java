package com.loulysoft.moneytransfer.accounting.repositories;

import com.loulysoft.moneytransfer.accounting.entities.DeviseEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeviseRepository extends JpaRepository<DeviseEntity, String> {
    Optional<DeviseEntity> findByCode(String code);
}
