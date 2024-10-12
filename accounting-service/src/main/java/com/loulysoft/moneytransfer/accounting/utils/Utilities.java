// package com.loulysoft.moneytransfer.accounting.utils;
//
// import com.loulysoft.moneytransfer.accounting.entities.TransactionMouvementSoldeEntity;
// import com.loulysoft.moneytransfer.accounting.entities.TransactionMouvementSoldePK;
// import com.loulysoft.moneytransfer.accounting.entities.TransactionMouvementSoldePK.Criticite;
// import com.loulysoft.moneytransfer.accounting.enums.Category;
// import com.loulysoft.moneytransfer.accounting.enums.Code;
// import com.loulysoft.moneytransfer.accounting.enums.DebitCredit;
// import com.loulysoft.moneytransfer.accounting.enums.Niveau;
// import com.loulysoft.moneytransfer.accounting.enums.OuiNon;
// import com.loulysoft.moneytransfer.accounting.enums.TransactionContextItem;
// import com.loulysoft.moneytransfer.accounting.enums.UniteOrganisationalType;
// import com.loulysoft.moneytransfer.accounting.exceptions.NotFoundException;
// import com.loulysoft.moneytransfer.accounting.exceptions.ResourceNotFoundException;
// import com.loulysoft.moneytransfer.accounting.exceptions.TransactionException;
// import com.loulysoft.moneytransfer.accounting.mappers.AccountingMapper;
// import com.loulysoft.moneytransfer.accounting.mappers.CompanyMapper;
// import com.loulysoft.moneytransfer.accounting.mappers.OperationMapper;
// import com.loulysoft.moneytransfer.accounting.mappers.OperationTmpMapper;
// import com.loulysoft.moneytransfer.accounting.mappers.ServiceMapper;
// import com.loulysoft.moneytransfer.accounting.mappers.TransactionMapper;
// import com.loulysoft.moneytransfer.accounting.mappers.TransactionTmpMapper;
// import com.loulysoft.moneytransfer.accounting.mappers.TransferMapper;
// import com.loulysoft.moneytransfer.accounting.models.Compte;
// import com.loulysoft.moneytransfer.accounting.models.CompteSchemaComptable;
// import com.loulysoft.moneytransfer.accounting.models.EcritureSchemaComptable;
// import com.loulysoft.moneytransfer.accounting.models.Journal;
// import com.loulysoft.moneytransfer.accounting.models.OperationTmp;
// import com.loulysoft.moneytransfer.accounting.models.ParametreRecherche;
// import com.loulysoft.moneytransfer.accounting.models.SchemaComptable;
// import com.loulysoft.moneytransfer.accounting.models.Transaction;
// import com.loulysoft.moneytransfer.accounting.models.TransactionContext;
// import com.loulysoft.moneytransfer.accounting.models.TransactionTmp;
// import com.loulysoft.moneytransfer.accounting.models.UniteOrganisational;
// import com.loulysoft.moneytransfer.accounting.repositories.CompteRepository;
// import com.loulysoft.moneytransfer.accounting.repositories.CompteSchemaComptableRepository;
// import com.loulysoft.moneytransfer.accounting.repositories.EcritureSchemaComptableRepository;
// import com.loulysoft.moneytransfer.accounting.repositories.OperationRepository;
// import com.loulysoft.moneytransfer.accounting.repositories.OperationTmpRepository;
// import com.loulysoft.moneytransfer.accounting.repositories.SchemaComptableRepository;
// import com.loulysoft.moneytransfer.accounting.repositories.TransactionMouvementSoldeRepository;
// import com.loulysoft.moneytransfer.accounting.repositories.TransactionRepository;
// import com.loulysoft.moneytransfer.accounting.repositories.TransactionTmpRepository;
// import com.loulysoft.moneytransfer.accounting.repositories.UniteOrganisationalRepository;
// import com.loulysoft.moneytransfer.accounting.runtime.AbstractRuntimeService;
// import com.loulysoft.moneytransfer.accounting.services.OperationService;
// import java.lang.reflect.InvocationTargetException;
// import java.math.BigDecimal;
// import java.util.HashMap;
// import java.util.List;
// import lombok.RequiredArgsConstructor;
// import lombok.extern.slf4j.Slf4j;
// import org.springframework.stereotype.Component;
// import org.springframework.transaction.annotation.Propagation;
// import org.springframework.transaction.annotation.Transactional;
//
// @Slf4j
// @Component
// @RequiredArgsConstructor
// @Transactional
// public class Utilities {
//
//    private final AccountingMapper accountingMapper;
//    private final TransactionTmpRepository transactionTmpRepository;
//    private final TransactionRepository transactionRepository;
//    private final UniteOrganisationalRepository uniteOrganisationalRepository;
//    private final SchemaComptableRepository schemaComptableRepository;
//    private final CompteSchemaComptableRepository compteSchemaComptableRepository;
//    private final EcritureSchemaComptableRepository ecritureSchemaRepository;
//    private final TransactionMouvementSoldeRepository transactionMouvementSoldeRepository;
//    private final CompteRepository compteRepository;
//    private final OperationTmpRepository operationTmpRepository;
//    private final OperationRepository operationRepository;
//    private final ServiceMapper serviceMapper;
//    private final CompanyMapper companyMapper;
//    private final OperationMapper operationMapper;
//    private final OperationTmpMapper operationTmpMapper;
//    private final TransactionTmpMapper transactionTmpMapper;
//    private final TransactionMapper transactionMapper;
//    private final ParameteringUtils parameteringUtils;
//    private final OperationService operationService;
//    private final CoreUtils coreUtils;
//
//
//
//    private HashMap<Long, CompteSchemaComptable> findComptesSchemaComptable(TransactionTmp transTmp) {
//
//        List<CompteSchemaComptable> comptesSchema = accountingMapper.convertToCompteSchemaComptable(
//                compteSchemaComptableRepository.findBySchemaId(transTmp.getSchemaComptableId()));
//        if (comptesSchema.isEmpty()) {
//            throw new ResourceNotFoundException("TransactionErrors.PARAMETER_EVALUATION_ERROR");
//        }
//        HashMap<Long, CompteSchemaComptable> results = new HashMap<>();
//        for (CompteSchemaComptable compteSchema : comptesSchema) {
//            ParametreRecherche parametreRecherche = compteSchema.getSearch();
//            log.debug("recherche compte " + compteSchema.getId() + " " + parametreRecherche.getId() + " "
//                    + transTmp.getId() + " ");
//            UniteOrganisational uniteOrganisationalPivot = parameteringUtils.chercherUniteOrganisational(
//                    parametreRecherche.getId(),
//                    transTmp.getCompanyId(),
//                    transTmp.getEntiteTierceId(),
//                    transTmp.getPaysDestination());
//
//            Compte compte = serviceMapper.toCompte(compteRepository
//                    .findByTypeCompteCodeAndOwnerId(
//                            compteSchema.getTypeCompte().getCode(), uniteOrganisationalPivot.getId())
//                    .orElseThrow(() -> new ResourceNotFoundException(
//                            "Compte with code " + compteSchema.getTypeCompte().getCode() + " and company id "
//                                    + uniteOrganisationalPivot.getId() + " not found")));
//
//            compteSchema.setCompteId(compte.getId());
//            results.put(compteSchema.getId(), compteSchema);
//        }
//
//        return results;
//    }
//
//
// }
