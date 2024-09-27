package com.loulysoft.moneytransfer.accounting.repositories;

import com.loulysoft.moneytransfer.accounting.entities.EcritureSchemaComptableEntity;
import com.loulysoft.moneytransfer.accounting.enums.Code;
import com.loulysoft.moneytransfer.accounting.enums.DebitCredit;
import com.loulysoft.moneytransfer.accounting.enums.Pivot;
import com.loulysoft.moneytransfer.accounting.enums.Type;
import com.loulysoft.moneytransfer.accounting.models.MontantSchemaRecord;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface EcritureSchemaComptableRepository extends JpaRepository<EcritureSchemaComptableEntity, Long> {

    List<EcritureSchemaComptableEntity>
            findBySchemaIdAndAccountSearchPivotInAndAccountSearchTypeAndAccountSearchNiveauAndWriterCodeIn(
                    Long schemaId, List<Pivot> pivots, Type type, Integer niveau, List<Code> codes);

    Optional<EcritureSchemaComptableEntity> findBySchemaIdAndWriterCodeAndDirection(
            Long schemaId, Code code, DebitCredit direction);

    //        @Query(
    //                """
    //                SELECT DISTINCT new
    // com.loulysoft.moneytransfer.accounting.models.MontantSchemaAndCodeEcritureRequest(esc.montantSchema.id,esc.writer)
    //                FROM EcritureSchemaComptableEntity esc
    //                JOIN esc.account csc
    //                JOIN csc.search pr
    //                WHERE esc.schema.id =:schemaId
    //                AND pr.pivot IN (:pivots)
    //                AND pr.type =:type
    //                AND pr.niveau =:niveau
    //                AND esc.writer.code IN (:codes)
    //                """)
    //        List<EcritureSchemaComptableEntity>
    //        findSchemaIdAndPivotsInAndTypeAndNiveauAndCodesIn(
    //                        Long schemaId, List<Pivot> pivots, Type type, Integer niveau, List<Code> codes);

    @Query(
            """
                SELECT DISTINCT new com.loulysoft.moneytransfer.accounting.models.MontantSchemaRecord(esc.montantSchema.id)
                FROM EcritureSchemaComptableEntity esc
                WHERE esc.schema.id = :schemaId
                AND esc.writer.code = :code
                AND esc.direction= :direction
                """)
    Optional<MontantSchemaRecord> findMontantSchemaIdBySchemaIdAndCodeAndDirection(
            Long schemaId, Code code, DebitCredit direction);
}
