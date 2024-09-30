package com.loulysoft.moneytransfer.accounting.parametering.utils;

import com.loulysoft.moneytransfer.accounting.enums.Type;
import com.loulysoft.moneytransfer.accounting.exceptions.NotFoundException;
import com.loulysoft.moneytransfer.accounting.exceptions.ResourceNotFoundException;
import com.loulysoft.moneytransfer.accounting.exceptions.TransactionException;
import com.loulysoft.moneytransfer.accounting.mappers.AccountingMapper;
import com.loulysoft.moneytransfer.accounting.mappers.GrilleMapper;
import com.loulysoft.moneytransfer.accounting.mappers.TransactionTmpMapper;
import com.loulysoft.moneytransfer.accounting.models.Grille;
import com.loulysoft.moneytransfer.accounting.models.MontantContext;
import com.loulysoft.moneytransfer.accounting.models.TransactionTmp;
import com.loulysoft.moneytransfer.accounting.models.UniteOrganisational;
import com.loulysoft.moneytransfer.accounting.models.ValeurParametre;
import com.loulysoft.moneytransfer.accounting.repositories.GrilleItemRepository;
import com.loulysoft.moneytransfer.accounting.repositories.GrilleRepository;
import com.loulysoft.moneytransfer.accounting.repositories.MontantParamSchemaComptableRepository;
import com.loulysoft.moneytransfer.accounting.repositories.TransactionTmpRepository;
import com.loulysoft.moneytransfer.accounting.repositories.ValeurParametreRepository;
import com.loulysoft.moneytransfer.accounting.services.DeviseService;
import com.loulysoft.moneytransfer.accounting.utils.ParameteringUtils;
import java.math.BigDecimal;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CorridorParam extends CoreParameterUtilities implements IParam {

    private final MontantParamSchemaComptableRepository montantParamSchemaRepository;

    private final AccountingMapper accountingMapper;

    private final TransactionTmpMapper transactionTmpMapper;

    private final DeviseService deviseService;

    private final ParameteringUtils parameteringUtils;

    private final ValeurParametreRepository valeurParamRepository;

    private final TransactionTmpRepository transactionTmpRepository;

    protected CorridorParam(
            GrilleRepository grilleRepository,
            GrilleItemRepository grilleItemRepository,
            GrilleMapper grilleMapper,
            MontantParamSchemaComptableRepository montantParamSchemaRepository,
            AccountingMapper accountingMapper,
            TransactionTmpMapper transactionTmpMapper,
            DeviseService deviseService,
            ParameteringUtils parameteringUtils,
            ValeurParametreRepository valeurParamRepository,
            TransactionTmpRepository transactionTmpRepository) {
        super(grilleRepository, grilleItemRepository, grilleMapper);
        this.montantParamSchemaRepository = montantParamSchemaRepository;
        this.accountingMapper = accountingMapper;
        this.transactionTmpMapper = transactionTmpMapper;
        this.deviseService = deviseService;
        this.parameteringUtils = parameteringUtils;
        this.valeurParamRepository = valeurParamRepository;
        this.transactionTmpRepository = transactionTmpRepository;
    }

    @Override
    public BigDecimal getValeurMontant(MontantContext context) {
        try {
            TransactionTmp transaction = transactionTmpMapper.toDto(transactionTmpRepository
                    .findById(context.getTransactionId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Transaction with Id " + context.getTransactionId() + " not found")));
            // em.find(Transaction__.class, context.get__transactionId());
            Long montantSchemaId = context.getMontantSchemaId();
            // Long companyId = context.getCompanyId();

            var montantParam = accountingMapper.toMontantParamSchema(montantParamSchemaRepository
                    .findByMontantSchemaIdAndSearchTypeNot(montantSchemaId, Type.DEVISE)
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Montant param schema with montantSchemaId " + montantSchemaId + " not found")));

            var parametreRecherche = montantParam.getSearch();
            if (parametreRecherche.getType().compareTo(Type.UNITE_ORGANISATIONELLE) != 0) {
                throw new NotFoundException("Param search evaluation error");
            }

            UniteOrganisational uniteOrganisational = parameteringUtils.chercherUniteOrganisational(
                    parametreRecherche.getId(),
                    transaction.getCompanyId(),
                    transaction.getEntiteTierceId(),
                    transaction.getPaysDestination());

            var montantSchema = montantParam.getMontantSchema();
            var pays = deviseService.readPaysByCode(context.getPaysSource());
            // Pays pays = parameteringUtils.getPays(uniteOrganisational.getId());

            var grille = getParameterGrille(
                    uniteOrganisational.getId(),
                    montantSchema.getParam().getCode(),
                    pays.getCode(),
                    context.getPaysDestination());

            return getValeurParametre(grille.getId(), context.getMontantDeBase());
        } catch (Exception e) {
            // TODO Auto-generated catch block
            log.error(e.getMessage(), e);
            log.info("capture ex----:" + context.getMontantSchemaId());
            throw new TransactionException("CorridorParam evaluation error");
        }
    }

    public Grille getParameterGrille(Long companyId, String parametre, String paysSource, String destination) {
        Grille grille = null;
        try {
            Optional<ValeurParametre> valeurParametre = valeurParamRepository
                    .findFirstByCompanyIdAndParamCodeAndPaysCode(companyId, parametre, paysSource)
                    .map(accountingMapper::toValeurParametre);

            //            Optional<Grille> grille = valeurParamRepository
            //                    .findCompanyIdAndCodeAndPaysSource(companyId, parametre, paysSource)
            //                    .map(accountingMapper::toGrille);

            if (valeurParametre.isPresent()) {
                grille = valeurParametre.get().getGrille();
            }

            return grille;

        } catch (Exception e) {
            // TODO Auto-generated catch block
            log.error(e.getMessage(), e);
            throw new TransactionException("CorridorParam Grille Parameter evaluation error");
        }
    }
}
