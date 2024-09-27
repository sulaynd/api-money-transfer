package com.loulysoft.moneytransfer.accounting.repositories;

import com.loulysoft.moneytransfer.accounting.entities.UniteOrganisationalEntity;
import com.loulysoft.moneytransfer.accounting.models.PaysRecord;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UniteOrganisationalRepository extends JpaRepository<UniteOrganisationalEntity, Long> {
    Optional<UniteOrganisationalEntity> findByTypeCode(String code);

    @Query(
            """
               SELECT DISTINCT new com.loulysoft.moneytransfer.accounting.models.PaysRecord(pu.code)
               FROM UniteOrganisationalEntity uo
               JOIN uo.pays p
               JOIN p.paysUoIds pu
               WHERE pu.id = :companyId
               """)
    Optional<PaysRecord> findPaysByCompanyRootId(Long companyId);

    // Optional<PaysRecord> findByParentId(Long companyId);
}
