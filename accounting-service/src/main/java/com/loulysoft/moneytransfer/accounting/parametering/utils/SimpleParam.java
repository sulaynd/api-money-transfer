package com.loulysoft.moneytransfer.accounting.parametering.utils;

import com.loulysoft.moneytransfer.accounting.enums.Type;
import com.loulysoft.moneytransfer.accounting.exceptions.NotFoundException;
import com.loulysoft.moneytransfer.accounting.exceptions.ResourceNotFoundException;
import com.loulysoft.moneytransfer.accounting.exceptions.TransactionException;
import com.loulysoft.moneytransfer.accounting.mappers.AccountingMapper;
import com.loulysoft.moneytransfer.accounting.mappers.GrilleMapper;
import com.loulysoft.moneytransfer.accounting.mappers.OperationMapper;
import com.loulysoft.moneytransfer.accounting.models.MontantContext;
import com.loulysoft.moneytransfer.accounting.models.Transaction;
import com.loulysoft.moneytransfer.accounting.models.UniteOrganisational;
import com.loulysoft.moneytransfer.accounting.models.ValeurParametre;
import com.loulysoft.moneytransfer.accounting.repositories.GrilleItemRepository;
import com.loulysoft.moneytransfer.accounting.repositories.GrilleRepository;
import com.loulysoft.moneytransfer.accounting.repositories.MontantParamSchemaComptableRepository;
import com.loulysoft.moneytransfer.accounting.repositories.TransactionRepository;
import com.loulysoft.moneytransfer.accounting.repositories.ValeurParametreRepository;
import com.loulysoft.moneytransfer.accounting.utils.ParameteringUtils;
import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SimpleParam extends CoreParameterUtilities implements IParam {

    private final MontantParamSchemaComptableRepository montantParamSchemaRepository;

    private final ParameteringUtils parameteringUtils;

    private final AccountingMapper accountingMapper;

    private final OperationMapper operationMapper;

    private final ValeurParametreRepository valeurParamRepository;

    private final TransactionRepository transactionRepository;

    protected SimpleParam(
            GrilleRepository grilleRepository,
            GrilleItemRepository grilleItemRepository,
            GrilleMapper grilleMapper,
            MontantParamSchemaComptableRepository montantParamSchemaRepository,
            ParameteringUtils parameteringUtils,
            AccountingMapper accountingMapper,
            OperationMapper operationMapper,
            ValeurParametreRepository valeurParamRepository,
            TransactionRepository transactionRepository) {
        super(grilleRepository, grilleItemRepository, grilleMapper);
        this.montantParamSchemaRepository = montantParamSchemaRepository;
        this.parameteringUtils = parameteringUtils;
        this.accountingMapper = accountingMapper;
        this.operationMapper = operationMapper;
        this.valeurParamRepository = valeurParamRepository;
        this.transactionRepository = transactionRepository;
    }

    @Override
    public BigDecimal getValeurMontant(MontantContext context) {
        // Grille grille = null;

        try {
            Transaction transaction = operationMapper.toTransaction(transactionRepository
                    .findById(context.getTransactionId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Transaction with Id " + context.getTransactionId() + " not found")));

            Long montantSchemaId = context.getMontantSchemaId();
            Long companyId = context.getCompanyId(); // .getCompany().getId();
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

            Optional<ValeurParametre> valeurParametre = valeurParamRepository
                    .findFirstByCompanyIdAndParamCode(
                            uniteOrganisational.getId(),
                            montantSchema.getParam().getCode())
                    .map(accountingMapper::toValeurParametre);

            if (valeurParametre.isPresent()) {
                var grille = valeurParametre.get().getGrille();
                return getValeurParametre(grille.getId(), context.getMontantDeBase());
            }

            return null;

        } catch (Exception e) {
            // TODO Auto-generated catch block
            log.error(e.getMessage(), e);
            throw new TransactionException("SimpleParam parameter evaluation error ");
        }
    }

    public BigDecimal getValeurParametre(long companyId, String parametre, BigDecimal montantDeBase) {
        try {
            ValeurParametre valeurParametre = accountingMapper.toValeurParametre(valeurParamRepository
                    .findFirstByCompanyIdAndParamCode(companyId, parametre)
                    .orElseThrow(() -> new ResourceNotFoundException(
                            MessageFormat.format("Parameter value not found with companyId: ", companyId))));

            return getValeurParametre(valeurParametre.getId(), montantDeBase);

        } catch (Exception e) {
            // TODO Auto-generated catch block
            log.error(e.getMessage(), e);
            throw new TransactionException("SimpleParam valeur parameter evaluation error ");
        }
    }
}
