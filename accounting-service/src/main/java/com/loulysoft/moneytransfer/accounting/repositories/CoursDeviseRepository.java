package com.loulysoft.moneytransfer.accounting.repositories;

import com.loulysoft.moneytransfer.accounting.entities.CoursDeviseEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CoursDeviseRepository extends JpaRepository<CoursDeviseEntity, Long> {

    Optional<CoursDeviseEntity> findBySourceCodeAndCibleCode(String source, String cible);

    @Query(
            """
            SELECT cd
            FROM CoursDeviseEntity cd
            JOIN cd.templateCoursDevise template
            JOIN template.compagnies company
            WHERE cd.source.code = :source
            AND cd.cible.code = :cible
            AND company.id = :companyId
            """)
    Optional<CoursDeviseEntity> findCoursDeviseBySourceAndCibleAndCompanyId(
            String source, String cible, Long companyId);

    //    Optional<CoursDeviseEntity> findBySourceCodeAndCibleCodeAndTemplateCoursDeviseId(
    //            String source, String cible, Long companyId);
}
