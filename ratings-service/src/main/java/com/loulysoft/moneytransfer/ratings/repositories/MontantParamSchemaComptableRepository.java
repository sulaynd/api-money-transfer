package com.loulysoft.moneytransfer.ratings.repositories;

import com.loulysoft.moneytransfer.ratings.entities.MontantParamSchemaComptableEntity;
import com.loulysoft.moneytransfer.ratings.utils.Type;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MontantParamSchemaComptableRepository extends JpaRepository<MontantParamSchemaComptableEntity, Long> {

    //    ql = "select mpsc from MontantParamSchemaComptable mpsc join mpsc.parametreRecherche pr "
    //            + " where mpsc.montantSchemaComptable.id =:montantSchemaComptableId and pr.type =:type";
    //    @Query(
    //            """
    //        select distinct o
    //        from OrderEntity o left join fetch o.items
    //        where o.userName = :userName and o.orderNumber = :orderNumber
    //        """)
    List<MontantParamSchemaComptableEntity> findByMontantSchemaIdAndParametreRechercheType(
            Long montantSchemaId, Type type);
}
