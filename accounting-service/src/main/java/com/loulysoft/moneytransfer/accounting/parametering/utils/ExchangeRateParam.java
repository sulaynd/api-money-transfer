package com.loulysoft.moneytransfer.accounting.parametering.utils;

import com.loulysoft.moneytransfer.accounting.enums.Type;
import com.loulysoft.moneytransfer.accounting.exceptions.NotFoundException;
import com.loulysoft.moneytransfer.accounting.exceptions.ResourceNotFoundException;
import com.loulysoft.moneytransfer.accounting.exceptions.TransactionException;
import com.loulysoft.moneytransfer.accounting.mappers.AccountingMapper;
import com.loulysoft.moneytransfer.accounting.mappers.GrilleMapper;
import com.loulysoft.moneytransfer.accounting.mappers.TransactionTmpMapper;
import com.loulysoft.moneytransfer.accounting.models.MontantContext;
import com.loulysoft.moneytransfer.accounting.models.MontantParamSchemaComptable;
import com.loulysoft.moneytransfer.accounting.models.TransactionTmp;
import com.loulysoft.moneytransfer.accounting.models.UniteOrganisational;
import com.loulysoft.moneytransfer.accounting.repositories.GrilleItemRepository;
import com.loulysoft.moneytransfer.accounting.repositories.GrilleRepository;
import com.loulysoft.moneytransfer.accounting.repositories.MontantParamSchemaComptableRepository;
import com.loulysoft.moneytransfer.accounting.repositories.TransactionTmpRepository;
import com.loulysoft.moneytransfer.accounting.utils.DevisesUtils;
import com.loulysoft.moneytransfer.accounting.utils.ParameteringUtils;
import java.math.BigDecimal;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ExchangeRateParam extends CoreParameterUtilities implements IParam {

    private final MontantParamSchemaComptableRepository montantParamSchemaRepository;

    private final AccountingMapper accountingMapper;

    private final DevisesUtils devisesUtils;

    private final ParameteringUtils parameteringUtils;

    private final TransactionTmpMapper transactionTmpMapper;

    private final TransactionTmpRepository transactionTmpRepository;

    protected ExchangeRateParam(
            GrilleRepository grilleRepository,
            GrilleItemRepository grilleItemRepository,
            GrilleMapper grilleMapper,
            MontantParamSchemaComptableRepository montantParamSchemaRepository,
            AccountingMapper accountingMapper,
            DevisesUtils devisesUtils,
            ParameteringUtils parameteringUtils,
            TransactionTmpMapper transactionTmpMapper,
            TransactionTmpRepository transactionTmpRepository) {
        super(grilleRepository, grilleItemRepository, grilleMapper);
        this.montantParamSchemaRepository = montantParamSchemaRepository;
        this.accountingMapper = accountingMapper;
        this.devisesUtils = devisesUtils;
        this.parameteringUtils = parameteringUtils;
        this.transactionTmpMapper = transactionTmpMapper;
        this.transactionTmpRepository = transactionTmpRepository;
    }

    @Override
    public BigDecimal getValeurMontant(MontantContext context) {

        Long montantSchemaId = context.getMontantSchemaId();
        String paysSource = context.getPaysSource();

        List<MontantParamSchemaComptable> montantParams = accountingMapper.convertToMontantParamSchema(
                montantParamSchemaRepository.findByMontantSchemaId(montantSchemaId));
        if (montantParams.size() != 2) {
            throw new NotFoundException("Parameter evaluation error");
        }
        try {
            TransactionTmp transaction = transactionTmpMapper.toDto(transactionTmpRepository
                    .findById(context.getTransactionId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Transaction with Id " + context.getTransactionId() + " not found")));

            // var pays = deviseService.readPaysByCode(paysSource);
            // var deviseSender = pays.getZoneMonetaire().getDevise();
            String deviseSource = null;
            String deviseCible = null;
            for (MontantParamSchemaComptable montantParam : montantParams) {
                var parametreRecherche = montantParam.getSearch();
                //
                // var devise = parameteringUtils.chercherDevise(parametreRecherche.getId(), companyId,
                // transaction.getId());
                var devise = parameteringUtils.chercherDevise(
                        parametreRecherche.getId(), transaction.getCompanyId(), transaction.getId());

                if (parametreRecherche.getType().compareTo(Type.DEVISE_SOURCE) == 0) {
                    deviseSource = devise.getCode();
                } else {
                    deviseCible = devise.getCode();
                }
            } // en for

            // assert deviseSource != null;
            // UniteOrganisationnelle system = parameteringUtils.getRootUniteOrganisationnelle();
            UniteOrganisational system = parameteringUtils.getRootUniteOrganisational();
            return devisesUtils.getCoursDevisePratique(system.getId(), deviseSource, deviseCible);

        } catch (Exception e) {
            log.info("getValeurMontant exception :", e);
            throw new TransactionException("Parameter evaluation error");
        }
    }
}
