package com.loulysoft.moneytransfer.accounting.repositories;

import com.loulysoft.moneytransfer.accounting.entities.PaysEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaysRepository extends JpaRepository<PaysEntity, String> {

    Optional<PaysEntity> findByCode(String code);

    //    @Query(
    //            """
    //               SELECT uo
    //               FROM Pays p
    //               JOIN p.zoneMonetaire zm
    //               JOIN zm.uniteOrganisational uo
    //               WHERE p.code =:code
    //                """)
    //    Optional<UniteOrganisationalEntity> findUniteOrganisationalByCode(String code);

    // jpql += "left join payme.uo_pays uop on uop.uop_uo_id = ref.trans_root_id ";

}
