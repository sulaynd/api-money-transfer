package com.loulysoft.moneytransfer.ratings.repositories;

import com.loulysoft.moneytransfer.ratings.entities.SchemaComptableEntity;
import com.loulysoft.moneytransfer.ratings.models.SchemaComptable;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SchemaComptableRepository extends JpaRepository<SchemaComptableEntity, Long> {

    //    @Query(
    //            """
    //        SELECT distinct s
    //        FROM SchemaComptableEntity
    //        WHERE s.service.code =:serviceCode
    //        AND s.type.code =:type
    //        AND s.variant =:variant
    //        AND s.status =:status
    //        ORDER BY s.version desc
    //        """)
    Optional<SchemaComptable> findByServiceCodeAndTypeCodeAndVariantAndStatus(
            String serviceCode, String type, String variant, String status);
}
