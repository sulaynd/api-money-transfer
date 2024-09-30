package com.loulysoft.moneytransfer.accounting.repositories;

import com.loulysoft.moneytransfer.accounting.entities.CompteEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompteRepository extends JpaRepository<CompteEntity, Long> {

    //    @Query(
    //            """
    //           SELECT c FROM Compte c
    //           WHERE c.typeCompte.code=:type
    //           AND c.owner.id=:owner
    //        """)
    //    Optional<CompteEntity> findCompteByTypeAndOwner(String type, Long owner);
    //
    Optional<CompteEntity> findByTypeCompteCodeAndOwnerId(String type, Long owner);
}
