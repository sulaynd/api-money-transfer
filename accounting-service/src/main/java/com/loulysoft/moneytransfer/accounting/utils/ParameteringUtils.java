package com.loulysoft.moneytransfer.accounting.utils;

import com.loulysoft.moneytransfer.accounting.enums.Niveau;
import com.loulysoft.moneytransfer.accounting.enums.Pivot;
import com.loulysoft.moneytransfer.accounting.enums.UniteOrganisationalType;
import com.loulysoft.moneytransfer.accounting.exceptions.ResourceNotFoundException;
import com.loulysoft.moneytransfer.accounting.exceptions.TransactionException;
import com.loulysoft.moneytransfer.accounting.mappers.AccountingMapper;
import com.loulysoft.moneytransfer.accounting.mappers.CompanyMapper;
import com.loulysoft.moneytransfer.accounting.mappers.DeviseMapper;
import com.loulysoft.moneytransfer.accounting.mappers.TransactionTmpMapper;
import com.loulysoft.moneytransfer.accounting.models.Devise;
import com.loulysoft.moneytransfer.accounting.models.ParametreRecherche;
import com.loulysoft.moneytransfer.accounting.models.Pays;
import com.loulysoft.moneytransfer.accounting.models.UniteOrganisational;
import com.loulysoft.moneytransfer.accounting.models.ZoneMonetaire;
import com.loulysoft.moneytransfer.accounting.repositories.DeviseRepository;
import com.loulysoft.moneytransfer.accounting.repositories.ParametreRechercheRepository;
import com.loulysoft.moneytransfer.accounting.repositories.PaysRepository;
import com.loulysoft.moneytransfer.accounting.repositories.TransactionTmpRepository;
import com.loulysoft.moneytransfer.accounting.repositories.UniteOrganisationalRepository;
import com.loulysoft.moneytransfer.accounting.repositories.ZoneMonetaireRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ParameteringUtils {

    private final ParametreRechercheRepository parametreRechercheRepository;
    private final AccountingMapper accountingMapper;
    private final TransactionTmpRepository transactionTmpRepository;
    private final TransactionTmpMapper transactionTmpMapper;
    private final DeviseMapper deviseMapper;
    private final PaysRepository paysRepository;
    private final DeviseRepository deviseRepository;
    private final UniteOrganisationalRepository uniteOrganisationalRepository;
    private final ZoneMonetaireRepository zoneMonetaireRepository;
    private final CompanyMapper companyMapper;

    public UniteOrganisational chercherUniteOrganisational(
            Long parametreRechercheId, Long uniteOrganisationalId, Long uniteOrgTierceId, String codePays) {
        ParametreRecherche parametreRecherche = accountingMapper.toParametreRecherche(parametreRechercheRepository
                .findById(parametreRechercheId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Parameter search Id " + parametreRechercheId + " not found")));
        // em.find(ParametreRecherche.class, parametreRechercheId);
        if (parametreRecherche.getPivot().compareTo(Pivot.ZONE_MONETAIRE_DESTINATION) == 0) {

            //            UniteOrganisational uniteOrganisational = companyMapper.toUniteOrganisational(paysRepository
            //                    .findUniteOrganisationalByCode(codePays)
            //                    .orElseThrow(() ->
            //                            new ResourceNotFoundException("Company with countyr code " + codePays + " not
            // found")));
            //
            //            return uniteOrganisational;

            Pays pays = deviseMapper.toPays(paysRepository
                    .findByCode(codePays)
                    .orElseThrow(() -> new ResourceNotFoundException("Country with code " + codePays + " not found")));

            return pays.getZoneMonetaire().getUniteOrganisational();
        }

        UniteOrganisational uniteOrganisational = null;
        if (parametreRecherche.getPivot().compareTo(Pivot.UNITE_ORGANISATIONNELLE_EMETTRICE) == 0) {

            uniteOrganisational = companyMapper.toUniteOrganisational(uniteOrganisationalRepository
                    .findById(uniteOrganisationalId)
                    .orElseThrow(() ->
                            new ResourceNotFoundException("Company with Id " + uniteOrganisationalId + " not found")));

        } else if (parametreRecherche.getPivot().compareTo(Pivot.SYSTEM) == 0) {

            uniteOrganisational = getRootUniteOrganisational();
        } else {

            uniteOrganisational = companyMapper.toUniteOrganisational(uniteOrganisationalRepository
                    .findById(uniteOrgTierceId)
                    .orElseThrow(() ->
                            new ResourceNotFoundException("Company with Id " + uniteOrganisationalId + " not found")));
        }

        int niveau = parametreRecherche.getNiveau();
        switch (niveau) {
            case 0:
                return uniteOrganisational;
            case 1:
                return uniteOrganisational.getParent();
            case 2:
                return uniteOrganisational.getParent().getParent();
            case 3:
                return uniteOrganisational.getParent().getParent().getParent();
            default:
                throw new TransactionException("chercherUniteOrganisational parameter error");
        }
    }

    public Devise chercherDevise(Long parametreRechercheId, Long uniteOrganisationalId, Long transactionId) {

        var parametreRecherche = deviseMapper.toParametreRecherche(parametreRechercheRepository
                .findById(parametreRechercheId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Parameter search with id " + parametreRechercheId + " not found")));

        var transaction = transactionTmpMapper.toDto(transactionTmpRepository
                .findById(transactionId)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Transaction with id " + transactionId + " not found")));

        if (parametreRecherche.getPivot().compareTo(Pivot.DEVISE) == 0) {

            return deviseMapper.toDevise(deviseRepository
                    .findByCode(transaction.getDevise())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Devise with code " + transaction.getDevise() + " not found")));
        }

        if (parametreRecherche.getPivot().compareTo(Pivot.PAYS_DESTINATION) == 0) {

            var pays = deviseMapper.toPays(paysRepository
                    .findByCode(transaction.getPaysDestination())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Country with code " + transaction.getPaysDestination() + " not found")));

            return pays.getZoneMonetaire().getDevise();
        }

        if (parametreRecherche.getPivot().compareTo(Pivot.UNITE_ORGANISATIONNELLE_TIERCE) == 0) {
            // return getDevise(transaction__.getEntiteTierce());
        }

        //        var pays = deviseMapper.toPays(paysRepository
        //                .findByCode(paysSource)
        //                .orElseThrow(() -> new ResourceNotFoundException("Country with code " + paysSource + " not
        // found")));
        //
        //        return pays.getDevise();

        return getDevise(uniteOrganisationalId);
    }

    public ZoneMonetaire getZoneMonetaire(Long uniteOrganisationalId) {

        //            UniteOrganisationnelle uniteOrganisationnelle = em.find(UniteOrganisationnelle.class,
        //     uniteOrganisationnelleId);

        UniteOrganisational uniteOrganisational = companyMapper.toUniteOrganisational(uniteOrganisationalRepository
                .findById(uniteOrganisationalId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Company with Id " + uniteOrganisationalId + " not found")));
        //            if (uniteOrganisational == null) {
        //                return null;
        //            }

        UniteOrganisational uniteOrganisationalPivot = uniteOrganisational;

        Niveau niveau = uniteOrganisationalPivot.getType().getNiveau();
        if (niveau.compareTo(Niveau.ZERO) != 0) {
            while (niveau.compareTo(Niveau.UN) != 0) {
                uniteOrganisationalPivot = uniteOrganisationalPivot.getParent();
                niveau = uniteOrganisationalPivot.getType().getNiveau();
            }

            UniteOrganisational finalUniteOrganisationalPivot = uniteOrganisationalPivot;
            ZoneMonetaire zoneMonetaire = companyMapper.toZoneMonetaire(zoneMonetaireRepository
                    .findByUniteOrganisationalId(uniteOrganisationalPivot.getId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Currency with company id " + finalUniteOrganisationalPivot.getId() + " not found")));

            return zoneMonetaire;
        }

        return null;
    }

    public Pays getPays(Long uniteOrganisationalId) {

        UniteOrganisational uniteOrganisational = companyMapper.toUniteOrganisational(uniteOrganisationalRepository
                .findById(uniteOrganisationalId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Company with Id " + uniteOrganisationalId + " not found")));
        if (uniteOrganisational == null
                || uniteOrganisational.getType().getNiveau().getValue() <= 1) {
            return null;
        }

        if (uniteOrganisational.getType().getNiveau().compareTo(Niveau.DEUX) == 0) {
            return uniteOrganisational.getPays();
        }

        return uniteOrganisational.getRoot().getPays();
    }

    public UniteOrganisational getRootUniteOrganisational() {

        UniteOrganisational uniteOrganisational = companyMapper.toUniteOrganisational(uniteOrganisationalRepository
                .findByTypeCode(UniteOrganisationalType.SYSTEME.name())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Company with Type Code " + UniteOrganisationalType.SYSTEME.name() + " not found")));

        return uniteOrganisational;
    }

    public Devise getDevise(Long uniteOrganisationalId) {

        ZoneMonetaire zoneMonetaire = getZoneMonetaire(uniteOrganisationalId);
        if (zoneMonetaire != null) {
            return zoneMonetaire.getDevise();
        }

        return null;
    }
}
