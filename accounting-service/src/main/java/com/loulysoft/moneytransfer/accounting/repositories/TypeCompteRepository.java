package com.loulysoft.moneytransfer.accounting.repositories;

import com.loulysoft.moneytransfer.accounting.entities.CompteEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TypeCompteRepository extends JpaRepository<CompteEntity, String> {}
