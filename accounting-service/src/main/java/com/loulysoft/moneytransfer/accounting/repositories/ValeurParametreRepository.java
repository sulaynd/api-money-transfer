package com.loulysoft.moneytransfer.accounting.repositories;

import com.loulysoft.moneytransfer.accounting.entities.GrilleEntity;
import com.loulysoft.moneytransfer.accounting.entities.ValeurParametreEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ValeurParametreRepository extends JpaRepository<ValeurParametreEntity, Long> {
    //    String ql = "select g from ValeurParametre vp join vp.grille g where vp.uniteOrganisationnelle.id
    // =:uniteOrganisationnelleId and vp.parametre.code =:parametre "
    //            + " and vp.pays.code =:paysSource ";
    //			if (zoneMonetaire != null) {
    //        ql += " and vp.zoneMonetaire.code =:zoneMonetaire";
    //    } else {
    //        ql += " and vp.zoneMonetaire is null";
    //    }

    Optional<ValeurParametreEntity> findFirstByCompanyIdAndParamCodeAndPaysCode(
            Long companyId, String code, String paysSource);

    Optional<ValeurParametreEntity> findFirstByCompanyIdAndParamCode(Long companyId, String code);

    // Optional<ValeurParametreEntity> findByCompanyIdAndCode(Long companyId, String code);

    @Query(
            """
            SELECT g
            FROM  ValeurParametreEntity vp
            JOIN vp.grille g
            WHERE vp.companyId = :companyId
            AND vp.param.code = :code
            AND vp.paysCode = :paysSource
        """)
    Optional<GrilleEntity> findCompanyIdAndCodeAndPaysSource(Long companyId, String code, String paysSource);

    @Query(
            """
            SELECT g
            FROM ValeurParametreEntity vp
            JOIN vp.grille g
            WHERE vp.companyId = :companyId
            AND vp.param.code = :code
        """)
    Optional<GrilleEntity> findCompanyIdAndCode(Long companyId, String code);
}
