package com.loulysoft.moneytransfer.accounting.repositories;

import com.loulysoft.moneytransfer.accounting.entities.GrilleItemEntity;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface GrilleItemRepository extends JpaRepository<GrilleItemEntity, Long> {

    List<GrilleItemEntity> findByGrilleId(Long grilleId);

    @Query(
            """
            SELECT g FROM GrilleItemEntity g
            WHERE g.grille.id = :grilleId
            AND borneInf<= :value
            AND borneSup>= :value
            ORDER BY borneInf asc
            """)
    Optional<GrilleItemEntity> findGrilleIdAndBorneInfLessThanEqualAndBorneSupGreaterThanEqualOrderByBorneInfAsc(
            Long grilleId, BigDecimal value);
}
