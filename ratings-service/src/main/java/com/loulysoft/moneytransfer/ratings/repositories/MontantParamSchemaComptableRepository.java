package com.loulysoft.moneytransfer.ratings.repositories;

import com.loulysoft.moneytransfer.ratings.entities.MontantParamSchemaComptableEntity;
import com.loulysoft.moneytransfer.ratings.utils.Type;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MontantParamSchemaComptableRepository extends JpaRepository<MontantParamSchemaComptableEntity, Long> {

    //    ql = "select mpsc from MontantParamSchemaComptable mpsc join mpsc.parametreRecherche pr "
    //            + " where mpsc.montantSchemaComptable.id =:montantSchemaComptableId and pr.type =:type";
    @Query(
            """
        SELECT mpsc
        FROM MontantParamSchemaComptableEntity mpsc
        LEFT JOIN FETCH mpsc.search pr
        WHERE mpsc.montantSchema.id = :montantSchemaId
        AND pr.type = :type
        """)
    List<MontantParamSchemaComptableEntity> findByMontantSchemaIdAndSearchType(Long montantSchemaId, Type type);
}
