package com.loulysoft.moneytransfer.accounting.repositories;

import com.loulysoft.moneytransfer.accounting.entities.ZoneMonetaireEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ZoneMonetaireRepository extends JpaRepository<ZoneMonetaireEntity, String> {

    Optional<ZoneMonetaireEntity> findByUniteOrganisationalId(Long uniteOrganisationalId);
}
