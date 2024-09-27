package com.loulysoft.moneytransfer.accounting.repositories;

import com.loulysoft.moneytransfer.accounting.entities.GrilleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GrilleRepository extends JpaRepository<GrilleEntity, Long> {

    //    String jpql = "select palier from Grille grille join grille.paliers palier  where grille.id = :grille ";
    //
    //    jpql += "and palier.borneInf<= :value and palier.borneSup>= :value order by palier.sequence desc";

    //    @Query(
    //            """
    //        SELECT palier
    //        FROM GrilleEntity g
    //        JOIN g.paliers palier
    //        WHERE g.id = :grilleId
    //        AND palier.borneInf<= :montant
    //        AND palier.borneSup>= :montant
    //        ORDER BY palier.sequence desc
    //        """)
    //    Optional<GrilleEntity> findByIdAndPaliersBorneInfLessThanAndPaliersBorneSupGreaterThanOrderBySequenceDesc(
    //            Long grilleId, BigDecimal montant);
}
