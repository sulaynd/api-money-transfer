package com.loulysoft.moneytransfer.accounting.utils;

import com.loulysoft.moneytransfer.accounting.enums.Code;
import com.loulysoft.moneytransfer.accounting.enums.Round;
import com.loulysoft.moneytransfer.accounting.enums.TransactionContextItem;
import com.loulysoft.moneytransfer.accounting.enums.Type;
import com.loulysoft.moneytransfer.accounting.exceptions.NotFoundException;
import com.loulysoft.moneytransfer.accounting.exceptions.ResourceNotFoundException;
import com.loulysoft.moneytransfer.accounting.exceptions.TransactionException;
import com.loulysoft.moneytransfer.accounting.mappers.AccountingMapper;
import com.loulysoft.moneytransfer.accounting.mappers.TransactionTmpMapper;
import com.loulysoft.moneytransfer.accounting.mappers.TransferMapper;
import com.loulysoft.moneytransfer.accounting.models.Devise;
import com.loulysoft.moneytransfer.accounting.models.EcritureSchemaComptable;
import com.loulysoft.moneytransfer.accounting.models.Journal;
import com.loulysoft.moneytransfer.accounting.models.MontantContext;
import com.loulysoft.moneytransfer.accounting.models.MontantParamSchemaComptable;
import com.loulysoft.moneytransfer.accounting.models.MontantSchemaComptable;
import com.loulysoft.moneytransfer.accounting.models.OperationTmp;
import com.loulysoft.moneytransfer.accounting.models.Pays;
import com.loulysoft.moneytransfer.accounting.models.TransactionContext;
import com.loulysoft.moneytransfer.accounting.models.TransactionReport;
import com.loulysoft.moneytransfer.accounting.models.TypeService;
import com.loulysoft.moneytransfer.accounting.parametering.utils.IParam;
import com.loulysoft.moneytransfer.accounting.parametering.utils.ParameterTypes;
import com.loulysoft.moneytransfer.accounting.repositories.MontantParamSchemaComptableRepository;
import com.loulysoft.moneytransfer.accounting.repositories.TypeServiceRepository;
import com.loulysoft.moneytransfer.accounting.runtime.AbstractRuntimeService;
import com.loulysoft.moneytransfer.accounting.services.DeviseService;
import com.loulysoft.moneytransfer.accounting.services.OperationService;
import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CoreUtils {
    private static final String CLASS_NAME_NOT_DEFINED = "Undefined class name with param code {0}";
    private final ApplicationContext context;
    private final DevisesUtils devisesUtils;
    private final ParameteringUtils parameteringUtils;
    private final AccountingMapper accountingMapper;
    private final MontantParamSchemaComptableRepository montantParamSchemaComptableRepository;
    private final TransactionTmpMapper transactionTmpMapper;
    private final OperationService operationService;
    private final DeviseService deviseService;
    private final TypeServiceRepository typeServiceRepository;

    public IParam getParamComponent(String typeParameter) {
        try {

            ParameterTypes parameterType = ParameterTypes.valueOf(typeParameter);

            Class<?> clazz = Class.forName(parameterType.getClassName());

            return (IParam) context.getBean(clazz);

        } catch (ClassNotFoundException | ClassCastException e) {

            throw new NotFoundException(MessageFormat.format(CLASS_NAME_NOT_DEFINED, typeParameter));
        }
    }

    public void readMontantSchemaComptables(
            Long userId,
            Long schemaComptableId,
            List<MontantSchemaComptable> montantSchemaComptables,
            String paysSource,
            Devise deviseSource,
            Long companyId,
            TransactionContext transactionContext,
            HashMap<Long, BigDecimal> map,
            TransactionReport report) {

        String paysDest = (String) transactionContext.getContextItemValue(TransactionContextItem.DESTINATION_COUNTRY);
        String natureServiceCode =
                (String) transactionContext.getContextItemValue(TransactionContextItem.NATURE_SERVICE);
        //        String autreParametre = (String) serviceContext.getServiceContext()
        //                .get(ServiceContextItem.AUTRE_PARAMETRE.name());

        var request = TransferMapper.buildTransactionRequest(
                userId, companyId, schemaComptableId, paysSource, deviseSource.getCode(), transactionContext);
        var transaction = operationService.createTemporaryTransaction(request);

        for (MontantSchemaComptable montantSchema : montantSchemaComptables) {
            Pays paysPayer = deviseService.readPaysByCode(paysDest);
            BigDecimal montantDeBase = BigDecimal.ZERO;
            if (!montantSchema.getMontantSchemaComptables().isEmpty()) {
                MontantSchemaComptable montantBase =
                        montantSchema.getMontantSchemaComptables().iterator().next();
                if (montantBase == null) {
                    throw new TransactionException("undefined montantBase parameter error");
                }
                OperationTmp operation = operationService.findOperation(transaction.getId(), montantBase.getId());
                montantDeBase = operation.getMontant();
            }

            var context = new MontantContext(
                    transaction.getId(),
                    montantSchema.getId(),
                    paysPayer.getCode(),
                    null,
                    companyId,
                    paysSource,
                    montantDeBase,
                    transactionContext);

            context.getTransactionContext().setTransactionId(transaction.getId());

            var codeParam = montantSchema.getParam().getTypeParametre().getCode();
            var iParam = getParamComponent(codeParam);
            BigDecimal montant = iParam.getValeurMontant(context);

            if (montant == null || montant.compareTo(BigDecimal.ZERO) < 0) {
                throw new TransactionException("Undefined amount value param component");
            }
            if (codeParam.equalsIgnoreCase("EXCHANGE_RATE")) {
                transactionContext.addContextItem(TransactionContextItem.TAUX_CHANGE, montant);
            }
            List<MontantParamSchemaComptable> montantParamSchemaComptables =
                    accountingMapper.convertToMontantParamSchema(
                            montantParamSchemaComptableRepository.findByMontantSchemaIdAndSearchType(
                                    montantSchema.getId(), Type.DEVISE));
            if (montantParamSchemaComptables.size() > 1) {
                throw new NotFoundException("Undefined accounting param schema amount");
            }

            var devise = deviseSource;
            if (!montantParamSchemaComptables.isEmpty()) {
                var parametreRecherche = montantParamSchemaComptables.getFirst().getSearch();
                devise = parameteringUtils.chercherDevise(parametreRecherche.getId(), companyId, transaction.getId());
            }

            var round = Round.UNIT;
            if (montantSchema.getRound() != null) {
                round = montantSchema.getRound();
            }

            if (round.compareTo(Round.NONE) != 0) {
                if (round.compareTo(Round.UNIT) == 0) {
                    montant = devisesUtils.roundUnit(montant, devise.getCode());
                } else {
                    montant = devisesUtils.roundMonetaryUnit(montant, devise.getCode());
                }
            }
            report.setReference(transaction.getId());
            var requestOp = TransferMapper.buildOperationRequest(
                    montant,
                    accountingMapper.toMontantSchemaEntity(montantSchema),
                    transactionTmpMapper.toEntity(transaction));
            operationService.createTemporaryOperation(requestOp);

            map.put(montantSchema.getId(), montant);
        } // end for
    }

    public void readEcritureSchemaComptables(
            List<EcritureSchemaComptable> ecritureSchemaComptables,
            TransactionReport report,
            HashMap<Long, BigDecimal> map) {
        for (EcritureSchemaComptable rs : ecritureSchemaComptables) {
            Long montantSchemaComptableId = rs.getMontantSchema().getId();
            var codeEcriture = rs.getWriter();
            var amount = map.get(montantSchemaComptableId);
            if (amount == null) {
                amount = new BigDecimal(0);
            }
            // amount == null ? new BigDecimal(0): new BigDecimal(amount);
            if (codeEcriture.getCode().compareTo(Code.PRINCIPAL) == 0) {
                report.setMontant(amount);
            } else if (codeEcriture.getCode().compareTo(Code.TAXES) == 0) {
                report.setTaxes(amount);
            } else if (codeEcriture.getCode().compareTo(Code.TIMBRE) == 0) {
                report.setTimbre(amount);
            } else {
                report.setFrais(report.getFrais().add(amount));
            }
        }
    }

    //    public void verifyAmount(UniteOrganisational company, TransactionContext transactionContext) {
    //        Pays paysDestination =
    //                (Pays) transactionContext.getContextItemValue(TransactionContextItem.DESTINATION_COUNTRY);
    //        Devise deviseSource = company.getPays().getDevise();
    //        Devise deviseCible = (Devise) transactionContext.getContextItemValue(TransactionContextItem.DEVISE);
    //
    //        if (transactionContext.getContextItemValue(TransactionContextItem.PRINCIPAL_CONVERTI) != null) {
    //            BigDecimal principalConverti = new BigDecimal(
    //
    // String.valueOf(transactionContext.getContextItemValue(TransactionContextItem.PRINCIPAL_CONVERTI)));
    //            BigDecimal cours = devisesUtils.getCoursDevisePratique(deviseSource.getCode(), deviseCible.getCode());
    //            BigDecimal principal = principalConverti.divide(cours, MathContext.DECIMAL64);
    //            principal = devisesUtils.roundMonetaryUnit(principal, deviseSource.getCode());
    //            transactionContext.addContextItem(TransactionContextItem.PRINCIPAL, principal);
    //        } else if (transactionContext.getContextItemValue(TransactionContextItem.PRINCIPAL_FEE) != null) {
    //            BigDecimal principalFee = new BigDecimal(
    //                    String.valueOf(transactionContext.getContextItemValue(TransactionContextItem.PRINCIPAL_FEE)));
    //
    //            Grille grille = corridorParam.getParameterGrille(
    //                    company.getId(), "TARIF_TRANSFERT_CASH", company.getPays().getCode(),
    // paysDestination.getCode());
    //            BigDecimal cent = new BigDecimal(100);
    //            List<GrilleItem> items =
    //                    accountingMapper.convertToGrilleItem(grilleItemRepository.findByGrilleId(grille.getId()));
    //            Iterator<GrilleItem> iterator = items.iterator();
    //            boolean trouve = false;
    //            while (iterator.hasNext()) {
    //                GrilleItem item = iterator.next();
    //                log.info(item.getId() + "  " + item.getValue());
    //                boolean percentage = item.getPourcentage().toString().equalsIgnoreCase("y")
    //                        || item.getPourcentage().toString().equalsIgnoreCase("o");
    //                BigDecimal min = item.getBorneInf();
    //                BigDecimal max = item.getBorneSup();
    //                if (percentage) {
    //                    min = min.multiply(item.getValue()).divide(cent, MathContext.DECIMAL64);
    //                    max = max.multiply(item.getValue()).divide(cent, MathContext.DECIMAL64);
    //                } else {
    //                    min = min.add(item.getValue());
    //                    max = max.add(item.getValue());
    //                }
    //
    //                if (principalFee.compareTo(max) <= 0 && principalFee.compareTo(min) >= 0) {
    //                    trouve = true;
    //                    BigDecimal principal = BigDecimal.ZERO;
    //                    if (percentage) {
    //                        BigDecimal taux = item.getValue().divide(cent, MathContext.DECIMAL64);
    //                        principal = principalFee.divide(BigDecimal.ONE.add(taux), MathContext.DECIMAL64);
    //                    } else {
    //                        principal = principalFee.subtract(item.getValue());
    //                    }
    //                    principal = devisesUtils.roundMonetaryUnit(principal, deviseSource.getCode());
    //                    transactionContext.addContextItem(TransactionContextItem.PRINCIPAL, principal);
    //                    break;
    //                }
    //            }
    //            if (!trouve) {
    //                throw new TransactionException("Montant invalide");
    //            }
    //        }
    //
    //        //        BigDecimal principal = new
    //        // BigDecimal(String.valueOf(transactionContext.getContextItemValue(TransactionContextItem.PRINCIPAL)));
    //        //        try {
    //        //            //principal.divide(BigDecimal.valueOf(deviseSource.getUniteMonetaire()),
    // MathContext.DECIMAL32);
    //        //            principal.divide(BigDecimal.valueOf(deviseSource.getUniteMonetaire()),
    //        // MathContext.DECIMAL32).intValueExact();
    //        //        } catch (Exception e) {
    //        //            // TODO Auto-generated catch block
    //        //            throw new TransactionException("Montant invalide");
    //        //        }
    //    }

    public AbstractRuntimeService<Journal> getRuntimeService(String serviceCode) {
        try {
            TypeService service = accountingMapper.toTypeService(typeServiceRepository
                    .findByCode(serviceCode)
                    .orElseThrow(
                            () -> new ResourceNotFoundException("Service with code " + serviceCode + " not found")));

            Class<?> clazz = Class.forName(service.getComposant());

            return (AbstractRuntimeService<Journal>) context.getBean(clazz);
        } catch (ClassNotFoundException | ClassCastException e) {
            // TODO Auto-generated catch block
            log.error(e.getMessage(), e);
            throw new NotFoundException("AbstractRuntimeService evaluation error");
        }
    }
}
