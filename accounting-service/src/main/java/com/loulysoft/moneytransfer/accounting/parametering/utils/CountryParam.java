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
public class CountryParam extends CoreParameterUtilities implements IParam {

    private final MontantParamSchemaComptableRepository montantParamSchemaRepository;

    private final ParameteringUtils parameteringUtils;

    private final AccountingMapper accountingMapper;

    private final TransactionTmpMapper transactionTmpMapper;

    private final DeviseService deviseService;

    private final ValeurParametreRepository valeurParamRepository;

    private final TransactionTmpRepository transactionTmpRepository;

    protected CountryParam(
            GrilleRepository grilleRepository,
            GrilleItemRepository grilleItemRepository,
            GrilleMapper grilleMapper,
            MontantParamSchemaComptableRepository montantParamSchemaRepository,
            ParameteringUtils parameteringUtils,
            AccountingMapper accountingMapper,
            TransactionTmpMapper transactionTmpMapper,
            DeviseService deviseService,
            ValeurParametreRepository valeurParamRepository,
            TransactionTmpRepository transactionTmpRepository) {
        super(grilleRepository, grilleItemRepository, grilleMapper);
        this.montantParamSchemaRepository = montantParamSchemaRepository;
        this.parameteringUtils = parameteringUtils;
        this.accountingMapper = accountingMapper;
        this.transactionTmpMapper = transactionTmpMapper;
        this.deviseService = deviseService;
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

            Long montantSchemaId = context.getMontantSchemaId();
            // Long companyId = context.getCompanyId(); // .getCompany().getId();

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
            // Pays pays = parameteringUtils.getPays(sessionUtilisateur.getUniteOrganisationnelle().getId());

            // .getCompany().getPays();
            // companyId = UniteOrganisational.getId();

            Optional<ValeurParametre> valeurParametre = valeurParamRepository
                    .findFirstByCompanyIdAndParamCodeAndPaysCode(
                            uniteOrganisational.getId(),
                            montantSchema.getParam().getCode(),
                            pays.getCode())
                    .map(accountingMapper::toValeurParametre);

            if (valeurParametre.isPresent()) {
                Grille grille = valeurParametre.get().getGrille();
                return getValeurParametre(grille.getId(), context.getMontantDeBase());
            }

            return null;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            log.error(e.getMessage(), e);
            throw new TransactionException("CorridorParam parameter evaluation error");
        }
    }
}
