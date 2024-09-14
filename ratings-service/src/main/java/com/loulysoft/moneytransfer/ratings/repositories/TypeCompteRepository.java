package com.loulysoft.moneytransfer.ratings.repositories;

import com.loulysoft.moneytransfer.ratings.entities.CompteEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TypeCompteRepository extends JpaRepository<CompteEntity, String> {}
