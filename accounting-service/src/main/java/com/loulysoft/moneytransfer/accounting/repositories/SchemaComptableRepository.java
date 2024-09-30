package com.loulysoft.moneytransfer.accounting.repositories;

import com.loulysoft.moneytransfer.accounting.entities.SchemaComptableEntity;
import com.loulysoft.moneytransfer.accounting.enums.Variant;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
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
    Optional<SchemaComptableEntity> findByServiceCodeAndTypeCodeAndVariantAndStatusOrderByVersionDesc(
            String serviceCode, String type, Variant variant, Character status);

    @Query(
            """
            SELECT sc
            FROM SchemaComptableEntity sc
            WHERE sc.service.code =:serviceCode
            AND sc.type.code =:type
            AND sc.status = :status
            AND sc.variant = :variant
            ORDER BY sc.version DESC
            """)
    Optional<SchemaComptableEntity> findSchemaByServiceCodeAndTypeCodeAndVariantAndStatusOrderByVersionDesc(
            String serviceCode, String type, Variant variant, Character status);
}
