package com.loulysoft.moneytransfer.accounting.repositories;

import com.loulysoft.moneytransfer.accounting.entities.JournalEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JournalRepository extends JpaRepository<JournalEntity, Long> {}
