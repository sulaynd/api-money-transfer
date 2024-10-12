package com.loulysoft.moneytransfer.accounting.runtime;

import com.loulysoft.moneytransfer.accounting.enums.Code;
import com.loulysoft.moneytransfer.accounting.enums.DebitCredit;
import com.loulysoft.moneytransfer.accounting.enums.Mode;
import com.loulysoft.moneytransfer.accounting.enums.NatureServiceCode;
import com.loulysoft.moneytransfer.accounting.enums.ServiceContextItem;
import com.loulysoft.moneytransfer.accounting.enums.TransactionContextItem;
import com.loulysoft.moneytransfer.accounting.enums.TypeCompte;
import com.loulysoft.moneytransfer.accounting.enums.UniteOrganisationalType;
import com.loulysoft.moneytransfer.accounting.enums.Variant;
import com.loulysoft.moneytransfer.accounting.exceptions.ResourceNotFoundException;
import com.loulysoft.moneytransfer.accounting.exceptions.TransactionException;
import com.loulysoft.moneytransfer.accounting.exceptions.UnauthorizedException;
import com.loulysoft.moneytransfer.accounting.mappers.AccountingMapper;
import com.loulysoft.moneytransfer.accounting.mappers.CompanyMapper;
import com.loulysoft.moneytransfer.accounting.mappers.OperationMapper;
import com.loulysoft.moneytransfer.accounting.mappers.TransactionTmpMapper;
import com.loulysoft.moneytransfer.accounting.models.Devise;
import com.loulysoft.moneytransfer.accounting.models.Grille;
import com.loulysoft.moneytransfer.accounting.models.GrilleItem;
import com.loulysoft.moneytransfer.accounting.models.InfoTransfert;
import com.loulysoft.moneytransfer.accounting.models.Operation;
import com.loulysoft.moneytransfer.accounting.models.Pays;
import com.loulysoft.moneytransfer.accounting.models.PaysRecord;
import com.loulysoft.moneytransfer.accounting.models.ServiceContext;
import com.loulysoft.moneytransfer.accounting.models.TransactionContext;
import com.loulysoft.moneytransfer.accounting.models.TransactionReport;
import com.loulysoft.moneytransfer.accounting.models.TransactionTmp;
import com.loulysoft.moneytransfer.accounting.models.UniteOrganisational;
import com.loulysoft.moneytransfer.accounting.parametering.utils.CorridorParam;
import com.loulysoft.moneytransfer.accounting.repositories.GrilleItemRepository;
import com.loulysoft.moneytransfer.accounting.repositories.OperationRepository;
import com.loulysoft.moneytransfer.accounting.repositories.TransactionTmpRepository;
import com.loulysoft.moneytransfer.accounting.repositories.UniteOrganisationalRepository;
import com.loulysoft.moneytransfer.accounting.services.DeviseService;
import com.loulysoft.moneytransfer.accounting.utils.DevisesUtils;
import com.loulysoft.moneytransfer.accounting.utils.ParameteringUtils;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Iterator;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class RuntimeCashTransfer extends AbstractRuntimeService<InfoTransfert> {

    private final ParameteringUtils parameteringUtils;
    private final DevisesUtils devisesUtils;
    private final CorridorParam corridorParam;
    private final AccountingMapper accountingMapper;
    private final GrilleItemRepository grilleItemRepository;
    private final UniteOrganisationalRepository uniteOrganisationalRepository;
    private final TransactionTmpRepository transactionTmpRepository;
    private final OperationRepository operationRepository;
    private final DeviseService deviseService;
    private final OperationMapper operationMapper;
    private final TransactionTmpMapper transactionTmpMapper;
    private final CompanyMapper companyMapper;

    // @Override
    public ServiceContext getServiceContext(
            Long companyId,
            Long companyRootId,
            String companyType,
            String service,
            TransactionContext transactionContext) {

        if (!companyType.equals(UniteOrganisationalType.POS.name())
                && !companyType.equals(UniteOrganisationalType.DISTRIBUTEUR.name())
                && !companyType.equals(UniteOrganisationalType.PARTENAIRE_EXTERNE.name())
                && !companyType.equals(UniteOrganisationalType.AGENCE_BANQUE.name())
                && !companyType.equals(UniteOrganisationalType.PARTENAIRE_INTERNE.name())) {
            throw new UnauthorizedException("Unauthorized company type");
        }

        String natureService = NatureServiceCode.SENDING.name();
        log.info("getContext-Service Name:" + natureService);

        String destination =
                (String) transactionContext.getContextItemValue(TransactionContextItem.DESTINATION_COUNTRY);

        PaysRecord record = uniteOrganisationalRepository
                .findPaysByCompanyRootId(companyRootId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Country with company root id " + companyRootId + " not found"));

        Pays paysSource = deviseService.readPaysByCode(record.code());
        Pays paysDest = deviseService.readPaysByCode(destination);
        if (paysDest == null) {
            throw new TransactionException("Pays de destination invalide");
        }
        Devise deviseCible = paysDest.getZoneMonetaire().getDevise();
        UniteOrganisational rootUniteOrganisational = parameteringUtils.getRootUniteOrganisational();
        if (paysSource == null) {
            throw new TransactionException("Pays source invalide");
        }
        Devise deviseSource = paysSource.getZoneMonetaire().getDevise();
        transactionContext.addContextItem(TransactionContextItem.SENDER_COUNTRY, paysSource);
        Grille grille2 = null;

        if (transactionContext.getContextItemValue(TransactionContextItem.PRINCIPAL_CONVERTI) != null) {
            BigDecimal principalConverti = new BigDecimal(
                    String.valueOf(transactionContext.getContextItemValue(TransactionContextItem.PRINCIPAL_CONVERTI)));
            // BigDecimal cours = devisesUtils.getCoursDevisePratique(deviseSource.getCode(), deviseCible.getCode());
            BigDecimal cours = devisesUtils.getCoursDevisePratique(
                    rootUniteOrganisational.getId(), deviseSource.getCode(), deviseCible.getCode());
            BigDecimal principal = principalConverti.divide(cours, MathContext.DECIMAL64);
            principal = devisesUtils.roundMonetaryUnit(principal, deviseSource.getCode());
            transactionContext.addContextItem(TransactionContextItem.PRINCIPAL, principal);
        } else if (transactionContext.getContextItemValue(TransactionContextItem.PRINCIPAL_FEE) != null) {
            BigDecimal principalFee = new BigDecimal(
                    String.valueOf(transactionContext.getContextItemValue(TransactionContextItem.PRINCIPAL_FEE)));

            Grille grille = corridorParam.getParameterGrille(
                    rootUniteOrganisational.getId(), "TARIF_TRANSFERT_CASH", paysSource.getCode(), paysDest.getCode());
            BigDecimal cent = new BigDecimal(100);
            List<GrilleItem> items =
                    accountingMapper.convertToGrilleItem(grilleItemRepository.findByGrilleId(grille.getId()));
            Iterator<GrilleItem> iterator = items.iterator();
            boolean trouve = false;
            while (iterator.hasNext()) {
                GrilleItem item = iterator.next();
                log.info(item.getId() + "  " + item.getValue());
                boolean percentage = item.getPourcentage().toString().equalsIgnoreCase("y")
                        || item.getPourcentage().toString().equalsIgnoreCase("o");
                BigDecimal min = item.getBorneInf();
                BigDecimal max = item.getBorneSup();
                if (percentage) {
                    min = min.multiply(item.getValue()).divide(cent, MathContext.DECIMAL64);
                    max = max.multiply(item.getValue()).divide(cent, MathContext.DECIMAL64);
                } else {
                    min = min.add(item.getValue());
                    max = max.add(item.getValue());
                }

                if (principalFee.compareTo(max) <= 0 && principalFee.compareTo(min) >= 0) {
                    trouve = true;
                    BigDecimal principal = BigDecimal.ZERO;
                    if (percentage) {
                        BigDecimal taux = item.getValue().divide(cent, MathContext.DECIMAL64);
                        principal = principalFee.divide(BigDecimal.ONE.add(taux), MathContext.DECIMAL64);
                    } else {
                        principal = principalFee.subtract(item.getValue());
                    }
                    principal = devisesUtils.roundMonetaryUnit(principal, deviseSource.getCode());
                    transactionContext.addContextItem(TransactionContextItem.PRINCIPAL, principal);
                    break;
                }
            }
            if (!trouve) {
                throw new TransactionException("Montant invalide");
            }
        }

        BigDecimal principal = new BigDecimal(
                String.valueOf(transactionContext.getContextItemValue(TransactionContextItem.PRINCIPAL)));
        try {
            // principal.divide(BigDecimal.valueOf(deviseSource.getUniteMonetaire()), MathContext.DECIMAL32);
            //            principal
            //                    .divide(BigDecimal.valueOf(deviseSource.getUniteMonetaire()), MathContext.DECIMAL64);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            throw new TransactionException("Montant invalide");
        }

        if (companyType.equals(UniteOrganisationalType.PARTENAIRE_EXTERNE.name())) {
            String partenaireTransactionId =
                    (String) transactionContext.getContextItemValue(TransactionContextItem.PARTENAIRE_TRANSACTION_ID);
            if (partenaireTransactionId == null || partenaireTransactionId.isEmpty()) {
                throw new TransactionException("partenaire transactionId requis");
            }

            return super.getServiceContext(companyId, companyRootId, companyType, service, transactionContext);
        } else {
            try {
                //                TransfertPaysReseau paysReseau = query.getSingleResult();
                ServiceContext serviceContext = new ServiceContext();
                //                boolean versPartenaireExterne = false;
                //                if (paysReseau.getUniteOrganisationnelle().getType().getCode()
                //                        .equals(TypeUniteOrganisationnelle.Type.PARTENAIRE_EXTERNE.name())) {
                //                    versPartenaireExterne = true;
                //                }

                Variant variant = Variant.VARIANTE_1;

                if (companyType.equals(UniteOrganisationalType.POS.name())
                        || companyType.equals(UniteOrganisationalType.DISTRIBUTEUR.name())
                        || companyType.equals(UniteOrganisationalType.AGENCE_BANQUE.name())
                        || companyType.equals(UniteOrganisationalType.PARTENAIRE_INTERNE.name())) {
                    //                    if (versPartenaireExterne) {
                    //                        variant = Variant.VARIANTE_2;
                    //                        transactionContext.addContextItem(
                    //                                TransactionContextItem.UNITE_ORGANISATIONNELLE_TIERCE_REFERENCE,
                    //                                paysReseau.getUniteOrganisationnelle().getCode());
                    //                    }

                    if (grille2 != null) {
                        variant = variant.compareTo(Variant.VARIANTE_1) == 0 ? Variant.VARIANTE_3 : Variant.VARIANTE_4;
                        BigDecimal fraisAdditionnel = corridorParam.getValeurParametre(grille2.getId(), principal);
                        serviceContext.addTransactionContext(
                                ServiceContextItem.FRAIS_TRANSFERT_ADDITIONNEL, fraisAdditionnel);
                    }
                }

                serviceContext.addTransactionContext(ServiceContextItem.ACCOUNTING_SCHEMA_VARIANTE, variant);

                serviceContext.addTransactionContext(ServiceContextItem.NATURE_SERVICE, natureService);

                return serviceContext;
            } catch (Exception e) {
                // TODO: handle exception
                log.error(e.getMessage(), e);
                throw new TransactionException("Pays de destination invalide");
            }
        }
    }

    @Override
    public Class<InfoTransfert> getJournalClass() {
        return InfoTransfert.class;
    }

    @Override
    public TransactionReport preRun(InfoTransfert infoTransfert, TransactionContext transactionContext) {

        TransactionTmp transTmp = transactionTmpMapper.toDto(transactionTmpRepository
                .findById(transactionContext.getTransactionId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Transaction with Id " + transactionContext.getTransactionId() + " not found")));

        Pays paysDest = deviseService.readPaysByCode(transTmp.getPaysDestination());
        Pays paysOrigine = deviseService.readPaysByCode(transTmp.getPaysSource());

        Operation operation = operationMapper.toDto(operationRepository
                .findOperationByCodeAndDirectionAndTransactionIdAndTypeCompte(
                        Code.ATTENTE_TRANSFERT,
                        DebitCredit.CREDIT,
                        infoTransfert.getTransaction().getId(),
                        TypeCompte.ATTENTE.name())
                .orElseThrow(() -> new ResourceNotFoundException("Operation with Code " + Code.ATTENTE_TRANSFERT
                        + " and direction " + DebitCredit.CREDIT + " not found")));

        infoTransfert.setMontantRecu(operation.getAmount().abs());
        // infoTransfert.setDevise(paysOrigine.getZoneMonetaire().getDevise());
        infoTransfert.setPaysOrigine(paysOrigine);
        infoTransfert.setPaysDestination(paysDest);
        // infoTransfert.setDeviseDestination(paysDest.getZoneMonetaire().getDevise());
        // transactionContext.addContextItem(TransactionContextItem.SENDER_COUNTRY, paysOrigine);
        // transactionContext.addContextItem(TransactionContextItem.DESTINATION_COUNTRY, paysDest);
        // transactionContext.addContextItem(TransactionContextItem.DEVISE, paysDest);

        UniteOrganisational partenaireExterne = null;
        Mode mode = null;
        String partenaireTransactionId = null;
        if (transTmp.getEntiteTierceId() != null) {
            // partenaireExterne = em.find(UniteOrganisationnelle.class, transaction__.getEntiteTierce());
            partenaireExterne = companyMapper.toUniteOrganisational(uniteOrganisationalRepository
                    .findById(transTmp.getEntiteTierceId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Company with id " + transTmp.getEntiteTierceId() + " not found")));

            mode = Mode.PUSH;

        } else {
            UniteOrganisational uniteOrganisationnelle =
                    companyMapper.toUniteOrganisational(uniteOrganisationalRepository
                            .findById(transTmp.getCompanyId())
                            .orElseThrow(() -> new ResourceNotFoundException(
                                    "Company with id " + transTmp.getEntiteTierceId() + " not found")));

            if (uniteOrganisationnelle.getType().getCode().equals(UniteOrganisationalType.PARTENAIRE_EXTERNE.name())) {
                partenaireExterne = uniteOrganisationnelle;
                mode = Mode.PUSH_PARTNER;
                partenaireTransactionId = (String)
                        transactionContext.getContextItemValue(TransactionContextItem.PARTENAIRE_TRANSACTION_ID);
            }
        }

        return super.preRun(infoTransfert, transactionContext);
    }
}
