package com.loulysoft.moneytransfer.accounting.utils;

import com.loulysoft.moneytransfer.accounting.enums.Category;
import com.loulysoft.moneytransfer.accounting.enums.Code;
import com.loulysoft.moneytransfer.accounting.enums.DebitCredit;
import com.loulysoft.moneytransfer.accounting.enums.Niveau;
import com.loulysoft.moneytransfer.accounting.enums.TransactionContextItem;
import com.loulysoft.moneytransfer.accounting.enums.UniteOrganisationalType;
import com.loulysoft.moneytransfer.accounting.exceptions.ResourceNotFoundException;
import com.loulysoft.moneytransfer.accounting.exceptions.TransactionException;
import com.loulysoft.moneytransfer.accounting.mappers.AccountingMapper;
import com.loulysoft.moneytransfer.accounting.mappers.CompanyMapper;
import com.loulysoft.moneytransfer.accounting.mappers.OperationMapper;
import com.loulysoft.moneytransfer.accounting.mappers.OperationTmpMapper;
import com.loulysoft.moneytransfer.accounting.mappers.ServiceMapper;
import com.loulysoft.moneytransfer.accounting.mappers.TransactionTmpMapper;
import com.loulysoft.moneytransfer.accounting.mappers.TransferMapper;
import com.loulysoft.moneytransfer.accounting.models.Compte;
import com.loulysoft.moneytransfer.accounting.models.CompteSchemaComptable;
import com.loulysoft.moneytransfer.accounting.models.EcritureSchemaComptable;
import com.loulysoft.moneytransfer.accounting.models.Journal;
import com.loulysoft.moneytransfer.accounting.models.OperationTmp;
import com.loulysoft.moneytransfer.accounting.models.ParametreRecherche;
import com.loulysoft.moneytransfer.accounting.models.SchemaComptable;
import com.loulysoft.moneytransfer.accounting.models.TransactionContext;
import com.loulysoft.moneytransfer.accounting.models.TransactionTmp;
import com.loulysoft.moneytransfer.accounting.models.UniteOrganisational;
import com.loulysoft.moneytransfer.accounting.repositories.CompteRepository;
import com.loulysoft.moneytransfer.accounting.repositories.CompteSchemaComptableRepository;
import com.loulysoft.moneytransfer.accounting.repositories.EcritureSchemaComptableRepository;
import com.loulysoft.moneytransfer.accounting.repositories.OperationTmpRepository;
import com.loulysoft.moneytransfer.accounting.repositories.SchemaComptableRepository;
import com.loulysoft.moneytransfer.accounting.repositories.TransactionTmpRepository;
import com.loulysoft.moneytransfer.accounting.repositories.UniteOrganisationalRepository;
import com.loulysoft.moneytransfer.accounting.services.OperationService;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
@Transactional(readOnly = false)
public class Utilities {

    private final AccountingMapper accountingMapper;
    private final TransactionTmpRepository transactionTmpRepository;
    private final UniteOrganisationalRepository uniteOrganisationalRepository;
    private final SchemaComptableRepository schemaComptableRepository;
    private final CompteSchemaComptableRepository compteSchemaComptableRepository;
    private final EcritureSchemaComptableRepository ecritureSchemaRepository;
    ;
    private final CompteRepository compteRepository;
    private final OperationTmpRepository operationTmpRepository;
    private final ServiceMapper serviceMapper;
    private final CompanyMapper companyMapper;
    private final OperationMapper operationMapper;
    private final OperationTmpMapper operationTmpMapper;
    private final TransactionTmpMapper transactionTmpMapper;
    private final ParameteringUtils parameteringUtils;
    private final OperationService operationService;

    public <T extends Journal> Journal ecrireLignesComptables(
            Long transactionId, Long companyId, TransactionContext transactionContext, Class<T> journalClass) {

        try {
            String code = (String) transactionContext.getContextItemValue(TransactionContextItem.CODE);
            Long initialtransactionId;
            String natureServiceCode;

            TransactionTmp transTmp = transactionTmpMapper.toDto(transactionTmpRepository
                    .findById(transactionId)
                    .orElseThrow(() ->
                            new ResourceNotFoundException("Transaction with Id " + transactionId + " not found")));

            List<OperationTmp> ops =
                    operationTmpMapper.toDtoList(operationTmpRepository.findByTransactionId(transTmp.getId()));

            if (ops.isEmpty()) {
                throw new ResourceNotFoundException("Missing operations with transaction id :" + transTmp.getId());
            }

            SchemaComptable schemaComptable = accountingMapper.toSchema(schemaComptableRepository
                    .findById(transTmp.getSchemaComptableId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Accounting Schema with Id " + transTmp.getSchemaComptableId() + " not found")));

            initialtransactionId = transTmp.getInitialTransaction();
            natureServiceCode = transTmp.getNatureService();

            log.info("deroulerTransaction-natureService:" + natureServiceCode + "--Code:" + code + "--inittrans:"
                    + initialtransactionId);

            UniteOrganisational uniteOrganisational = companyMapper.toUniteOrganisational(uniteOrganisationalRepository
                    .findById(companyId)
                    .orElseThrow(() -> new ResourceNotFoundException("Company with id " + companyId + " not found")));

            UniteOrganisational root;
            if (uniteOrganisational.getType().getNiveau().getValue() == 2) {
                root = uniteOrganisational;
            } else {
                root = uniteOrganisational.getRoot();
            }
            HashMap<Long, CompteSchemaComptable> comptesSchema = findComptesSchemaComptable(transTmp);

            var transaction = TransferMapper.setTransaction(
                    schemaComptable,
                    root,
                    uniteOrganisational,
                    parameteringUtils.getDevise(root.getId()),
                    code,
                    initialtransactionId);

            var savedTransaction = operationService.createTransaction(transaction);

            List<EcritureSchemaComptable> ecritureSchemaComptables = accountingMapper.convertToEcritureSchema(
                    ecritureSchemaRepository.findBySchemaIdOrderByRang(schemaComptable.getId()));

            if (ecritureSchemaComptables.isEmpty()) {
                throw new ResourceNotFoundException("TransactionErrors.PARAMETER_EVALUATION_ERROR");
            }

            Journal journal = (Journal) Class.forName(journalClass.getName())
                    .getDeclaredConstructor()
                    .newInstance();
            journal.setTransaction(savedTransaction);
            journal.setDevise(parameteringUtils.getDevise(root.getId()));
            journal.setUniteOrganisational(root);

            UniteOrganisational pivotUniteOrganisationnelle = uniteOrganisational;
            if (uniteOrganisational.getType().getCode().equals(UniteOrganisationalType.AGENCE_BANQUE.name())) {
                pivotUniteOrganisationnelle = uniteOrganisational.getParent();
            }

            for (EcritureSchemaComptable ecritureSchema : ecritureSchemaComptables) {
                log.debug("ecrireLignesComptables() -> ecritureSchemaId: {}", ecritureSchema.getId());

                CompteSchemaComptable compteSchema =
                        comptesSchema.get(ecritureSchema.getAccount().getId());
                Compte compte = serviceMapper.toCompte(compteRepository
                        .findById(compteSchema.getCompteId())
                        .orElseThrow(() -> new ResourceNotFoundException(
                                "Account with id " + compteSchema.getCompteId() + " not found")));

                OperationTmp opTmp = ops.stream()
                        .filter(t -> t.getMontantSchema()
                                        .getId()
                                        .compareTo(ecritureSchema
                                                .getMontantSchema()
                                                .getId())
                                == 0)
                        .findAny()
                        .orElse(null);

                if (opTmp == null) {
                    throw new ResourceNotFoundException("TransactionErrors.PARAMETER_EVALUATION_ERROR");
                }

                BigDecimal amount = opTmp.getMontant();
                char debitCredit;
                DebitCredit direction = ecritureSchema.getDirection();
                amount = direction.compareTo(DebitCredit.CREDIT) == 0 ? amount.negate() : amount;
                debitCredit = direction.compareTo(DebitCredit.CREDIT) == 0 ? 'C' : 'D';

                var operation =
                        TransferMapper.setOperation(savedTransaction, compte, amount, ecritureSchema, debitCredit);
                operationService.createOperation(operation);
                BigDecimal absAmount = amount.abs();
                if (pivotUniteOrganisationnelle
                                .getId()
                                .compareTo(compte.getOwner().getId())
                        == 0) {
                    if (ecritureSchema.getWriter().getCode().compareTo(Code.PRINCIPAL) == 0) {
                        journal.setMontant(absAmount);
                        if (direction.compareTo(DebitCredit.DEBIT) == 0) {
                            journal.setTypeOperation(DebitCredit.DEBIT);
                        } else {
                            journal.setTypeOperation(DebitCredit.CREDIT);
                        }
                    } else if (ecritureSchema.getWriter().getCode().compareTo(Code.FRAIS) == 0) {
                        BigDecimal frais = journal.getFrais().add(absAmount);
                        journal.setFrais(frais);
                    } else if (ecritureSchema.getWriter().getCode().compareTo(Code.TIMBRE) == 0) {
                        BigDecimal timbre = journal.getTimbre().add(absAmount);
                        journal.setTimbre(timbre);
                    } else if (ecritureSchema.getWriter().getCode().compareTo(Code.TAXES) == 0) {
                        BigDecimal timbre = journal.getTimbre().add(absAmount);
                        journal.setTimbre(timbre);
                    }
                }

                Category category = compte.getTypeCompte().getCategory();
                Code codeOperation = ecritureSchema.getWriter().getCode();
                if (category.compareTo(Category.PRINCIPAL) == 0
                        && (codeOperation.compareTo(Code.COMMISSION_HORS_TAXE) == 0
                                || codeOperation.compareTo(Code.COMMISSION_TAXE) == 0)) {
                    Niveau niveau = compte.getOwner().getType().getNiveau();
                    switch (niveau) {
                        case UN:
                            if (codeOperation.compareTo(Code.COMMISSION_HORS_TAXE) == 0)
                                journal.setCommissionHorsTaxe_0(absAmount);
                            else journal.setCommissionTaxe_0(absAmount);
                            break;
                        case DEUX:
                            if (codeOperation.compareTo(Code.COMMISSION_HORS_TAXE) == 0)
                                journal.setCommissionHorsTaxe_1(absAmount);
                            else journal.setCommissionTaxe_1(absAmount);
                            break;
                        case TROIS:
                            if (codeOperation.compareTo(Code.COMMISSION_HORS_TAXE) == 0)
                                journal.setCommissionHorsTaxe_2(absAmount);
                            else journal.setCommissionTaxe_2(absAmount);
                            break;
                        case QUATRE:
                            if (codeOperation.compareTo(Code.COMMISSION_HORS_TAXE) == 0)
                                journal.setCommissionHorsTaxe_3(absAmount);
                            else journal.setCommissionTaxe_3(absAmount);
                            break;
                        default:
                            break;
                    }
                }
            }

            return journal;
            // return operationService.saveJournal(journal);

        } catch (InstantiationException
                | IllegalAccessException
                | ClassNotFoundException
                | InvocationTargetException
                | NoSuchMethodException e) {
            throw new TransactionException("Accounting line entry error");
        }
    }

    private HashMap<Long, CompteSchemaComptable> findComptesSchemaComptable(TransactionTmp transTmp) {

        List<CompteSchemaComptable> comptesSchema = accountingMapper.convertToCompteSchemaComptable(
                compteSchemaComptableRepository.findBySchemaId(transTmp.getSchemaComptableId()));
        if (comptesSchema.isEmpty()) {
            throw new ResourceNotFoundException("TransactionErrors.PARAMETER_EVALUATION_ERROR");
        }
        HashMap<Long, CompteSchemaComptable> results = new HashMap<>();
        for (CompteSchemaComptable compteSchema : comptesSchema) {
            ParametreRecherche parametreRecherche = compteSchema.getSearch();
            log.debug("recherche compte " + compteSchema.getId() + " " + parametreRecherche.getId() + " "
                    + transTmp.getId() + " ");
            UniteOrganisational uniteOrganisationalPivot = parameteringUtils.chercherUniteOrganisational(
                    parametreRecherche.getId(),
                    transTmp.getCompanyId(),
                    transTmp.getEntiteTierceId(),
                    transTmp.getPaysDestination());

            Compte compte = serviceMapper.toCompte(compteRepository
                    .findByTypeCompteCodeAndOwnerId(
                            compteSchema.getTypeCompte().getCode(), uniteOrganisationalPivot.getId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Compte with code " + compteSchema.getTypeCompte().getCode() + " and company id "
                                    + uniteOrganisationalPivot.getId() + " not found")));

            compteSchema.setCompteId(compte.getId());
            results.put(compteSchema.getId(), compteSchema);
        }

        return results;
    }
}
