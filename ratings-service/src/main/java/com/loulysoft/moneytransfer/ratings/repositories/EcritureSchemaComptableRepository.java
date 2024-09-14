package com.loulysoft.moneytransfer.ratings.repositories;

import com.loulysoft.moneytransfer.ratings.entities.EcritureSchemaComptableEntity;
import com.loulysoft.moneytransfer.ratings.utils.Code;
import com.loulysoft.moneytransfer.ratings.utils.Pivot;
import com.loulysoft.moneytransfer.ratings.utils.Type;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface EcritureSchemaComptableRepository extends JpaRepository<EcritureSchemaComptableEntity, Long> {

    @Query(
            """
            SELECT DISTINCT esc.amount.id,esc.writer
            FROM EcritureSchemaComptableEntity esc
            JOIN esc.account csc
            JOIN csc.search pr
            WHERE esc.schema.id =:schemaId
            AND pr.pivot IN (:pivots)
            AND pr.type =:type
            AND pr.niveau =:niveau
            AND esc.writer.code IN (:codes)
            """)
    List<EcritureSchemaComptableEntity>
            findBySchemaIdAndAccountSearchPivotInAndAccountSearchTypeAndAccountSearchNiveauAndWriterCodeIn(
                    Long schemaId, List<Pivot> pivots, Type type, Integer niveau, List<Code> codes);
}
