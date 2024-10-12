package com.loulysoft.moneytransfer.accounting.services.impl;

import com.loulysoft.moneytransfer.accounting.entities.TransactionEntity;
import com.loulysoft.moneytransfer.accounting.entities.TransactionMouvementSoldeEntity;
import com.loulysoft.moneytransfer.accounting.entities.TransactionMouvementSoldePK;
import com.loulysoft.moneytransfer.accounting.entities.TransactionTmpEntity;
import com.loulysoft.moneytransfer.accounting.enums.Category;
import com.loulysoft.moneytransfer.accounting.enums.Code;
import com.loulysoft.moneytransfer.accounting.enums.Criticite;
import com.loulysoft.moneytransfer.accounting.enums.DebitCredit;
import com.loulysoft.moneytransfer.accounting.enums.Niveau;
import com.loulysoft.moneytransfer.accounting.enums.OuiNon;
import com.loulysoft.moneytransfer.accounting.enums.TransactionContextItem;
import com.loulysoft.moneytransfer.accounting.enums.UniteOrganisationalType;
import com.loulysoft.moneytransfer.accounting.exceptions.NotFoundException;
import com.loulysoft.moneytransfer.accounting.exceptions.ResourceNotFoundException;
import com.loulysoft.moneytransfer.accounting.exceptions.TransactionException;
import com.loulysoft.moneytransfer.accounting.mappers.AccountingMapper;
import com.loulysoft.moneytransfer.accounting.mappers.CompanyMapper;
import com.loulysoft.moneytransfer.accounting.mappers.OperationTmpMapper;
import com.loulysoft.moneytransfer.accounting.mappers.ServiceMapper;
import com.loulysoft.moneytransfer.accounting.mappers.TransactionMapper;
import com.loulysoft.moneytransfer.accounting.mappers.TransactionTmpMapper;
import com.loulysoft.moneytransfer.accounting.mappers.TransferMapper;
import com.loulysoft.moneytransfer.accounting.models.Compte;
import com.loulysoft.moneytransfer.accounting.models.CompteSchemaComptable;
import com.loulysoft.moneytransfer.accounting.models.CreateTransactionRequest;
import com.loulysoft.moneytransfer.accounting.models.EcritureSchemaComptable;
import com.loulysoft.moneytransfer.accounting.models.Journal;
import com.loulysoft.moneytransfer.accounting.models.OperationTmp;
import com.loulysoft.moneytransfer.accounting.models.Pays;
import com.loulysoft.moneytransfer.accounting.models.SchemaComptable;
import com.loulysoft.moneytransfer.accounting.models.Solde;
import com.loulysoft.moneytransfer.accounting.models.Transaction;
import com.loulysoft.moneytransfer.accounting.models.TransactionContext;
import com.loulysoft.moneytransfer.accounting.models.TransactionReport;
import com.loulysoft.moneytransfer.accounting.models.TransactionRequest;
import com.loulysoft.moneytransfer.accounting.models.TransactionTmp;
import com.loulysoft.moneytransfer.accounting.models.TypeService;
import com.loulysoft.moneytransfer.accounting.models.UniteOrganisational;
import com.loulysoft.moneytransfer.accounting.repositories.CompteRepository;
import com.loulysoft.moneytransfer.accounting.repositories.EcritureSchemaComptableRepository;
import com.loulysoft.moneytransfer.accounting.repositories.OperationRepository;
import com.loulysoft.moneytransfer.accounting.repositories.OperationTmpRepository;
import com.loulysoft.moneytransfer.accounting.repositories.SchemaComptableRepository;
import com.loulysoft.moneytransfer.accounting.repositories.TransactionMouvementSoldeRepository;
import com.loulysoft.moneytransfer.accounting.repositories.TransactionRepository;
import com.loulysoft.moneytransfer.accounting.repositories.TransactionTmpRepository;
import com.loulysoft.moneytransfer.accounting.repositories.TypeServiceRepository;
import com.loulysoft.moneytransfer.accounting.repositories.UniteOrganisationalRepository;
import com.loulysoft.moneytransfer.accounting.runtime.AbstractRuntimeService;
import com.loulysoft.moneytransfer.accounting.services.CompteService;
import com.loulysoft.moneytransfer.accounting.services.DeviseService;
import com.loulysoft.moneytransfer.accounting.services.JournalService;
import com.loulysoft.moneytransfer.accounting.services.OperationService;
import com.loulysoft.moneytransfer.accounting.services.TransactionService;
import com.loulysoft.moneytransfer.accounting.utils.CoreUtils;
import com.loulysoft.moneytransfer.accounting.utils.ParameteringUtils;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class TransactionServiceImpl implements TransactionService {

    private final AccountingMapper accountingMapper;

    private final TransactionTmpRepository transactionTmpRepository;

    private final SchemaComptableRepository schemaComptableRepository;

    private final OperationRepository operationRepository;

    private final TransactionRepository transactionRepository;

    private final TransactionMouvementSoldeRepository transactionMouvementSoldeRepository;

    private final TransactionTmpMapper transactionTmpMapper;

    private final TransactionMapper transactionMapper;

    private final OperationService operationService;

    private final DeviseService deviseService;

    private final JournalService journalService;

    private final CoreUtils coreUtils;

    private final CompteService compteService;

    private final UniteOrganisationalRepository uniteOrganisationalRepository;

    private final EcritureSchemaComptableRepository ecritureSchemaRepository;

    private final CompteRepository compteRepository;

    private final TypeServiceRepository typeServiceRepository;

    private final OperationTmpRepository operationTmpRepository;

    private final ServiceMapper serviceMapper;

    private final CompanyMapper companyMapper;

    private final OperationTmpMapper operationTmpMapper;

    private final ParameteringUtils parameteringUtils;

    @Override
    public TransactionReport finaliserTransaction(Long userId, Long companyId, Long reference) {

        TransactionReport report = deroulerTransaction(userId, companyId, reference);

        Transaction transaction = report.getJournal().getTransaction();

        // compteService.controlerSoldeCompte(transaction);

        deroulerService(transaction);

        // updateTransaction(transaction);
        //            transactionManagerSession.preparerMouvementCompte(transactionId);

        // exporterTransactionReport(transaction, report);

        return report;

        //        } catch (TransactionException e) {
        //            // TODO Auto-generated catch block
        //            // Logger.getLogger(TransactionManagerFacade.class).error(e.getMessage(), e);
        //            if (transactionId != null) {
        //                //  transactionManagerSession.supprimerTransaction(transactionId, e);
        //            }
        //            throw new TransactionException("Finalize transaction error");
        //        }
    }

    @Override
    public TransactionReport deroulerTransaction(Long userId, Long companyId, Long reference) {
        log.info("deroulerTransaction : demarrage.....................................................");

        TransactionContext transactionContext = new TransactionContext();
        TransactionTmp transactionTmp = transactionTmpMapper.toDto(transactionTmpRepository
                .findById(reference)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Temporary Transaction with Id " + reference + " not found")));

        transactionContext.setTransactionId(transactionTmp.getId());
        Pays paysDest = deviseService.readPaysByCode(transactionTmp.getPaysDestination());
        Pays paysOrigine = deviseService.readPaysByCode(transactionTmp.getPaysSource());

        SchemaComptable schemaComptable = accountingMapper.toSchema(schemaComptableRepository
                .findById(transactionTmp.getSchemaComptableId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Accounting Schema with Id " + transactionTmp.getSchemaComptableId() + " not found")));

        AbstractRuntimeService<Journal> runtimeService =
                coreUtils.getRuntimeService(schemaComptable.getService().getCode());
        Class<Journal> journalClass = runtimeService.getJournalClass();
        Journal journal = ecrireLignesComptables(
                transactionTmp.getId(), transactionTmp.getCompanyId(), transactionContext, journalClass);

        TransactionReport transactionReport = runtimeService.preRun(journal, transactionContext);
        if (transactionReport == null) {
            transactionReport = new TransactionReport();
        }

        Journal createdJournal = journalService.createJournal(journal);
        transactionTmp.setTransactionId(journal.getTransaction().getId());
        updateTemporaryTransaction(transactionTmp);
        createdJournal.setPaysOrigine(paysOrigine);
        createdJournal.setPaysDest(paysDest);
        BigDecimal principal = createdJournal.getMontant();
        BigDecimal commissions =
                createdJournal.getFrais().add(createdJournal.getTaxes()).add(createdJournal.getTimbre());
        transactionReport.setJournal(createdJournal);
        transactionReport.setCreatedAt(createdJournal.getTransaction().getCreatedAt());
        transactionReport.setReference(createdJournal.getTransaction().getId());
        transactionReport.setTransactionId(createdJournal.getTransaction().getId());
        transactionReport.setStatus('T');
        transactionReport.setMontant(createdJournal.getMontant());
        transactionReport.setMontantAPayer(createdJournal.getMontant().abs());
        transactionReport.setPrincipal(principal);
        transactionReport.setCommissions(commissions);
        transactionReport.setTotal(principal.add(commissions));
        transactionReport.setFrais(createdJournal.getFrais());
        transactionReport.setTimbre(createdJournal.getTimbre());
        transactionReport.setTaxes(createdJournal.getTaxes());
        transactionReport.setUoName(journal.getTransaction().getLaunchEntity().getLibelle());
        transactionReport.setUoParentName(
                journal.getTransaction().getLaunchEntity().getParent().getLibelle());
        transactionReport.setUoRootName(
                journal.getTransaction().getLaunchEntity().getParent().getRoot().getLibelle());
        transactionReport.setDevise(paysOrigine.getZoneMonetaire().getCode());
        transactionReport.setDevisePayer(paysDest.getZoneMonetaire().getCode());
        log.info("deroulerTransaction : fin............................................................");

        return transactionReport;
    }

    @Override
    public <T extends Journal> Journal ecrireLignesComptables(
            Long transactionId, Long companyId, TransactionContext transactionContext, Class<T> journalClass) {
        try {
            String code = (String) transactionContext.getContextItemValue(TransactionContextItem.CODE);
            Long initialtransactionId;
            String natureServiceCode;

            TransactionTmp transactionTmp = transactionTmpMapper.toDto(transactionTmpRepository
                    .findById(transactionId)
                    .orElseThrow(() ->
                            new ResourceNotFoundException("Transaction with Id " + transactionId + " not found")));

            List<OperationTmp> operations = operationTmpMapper.toDtoList(
                    operationTmpRepository.findOperationsByTransactionId(transactionTmp.getId()));

            if (operations.isEmpty()) {
                throw new ResourceNotFoundException(
                        "Missing operations with transaction id :" + transactionTmp.getId());
            }

            transactionTmp.setOperations(new HashSet<>(operations));

            SchemaComptable schemaComptable = accountingMapper.toSchema(schemaComptableRepository
                    .findById(transactionTmp.getSchemaComptableId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Accounting Schema with Id " + transactionTmp.getSchemaComptableId() + " not found")));

            initialtransactionId = transactionTmp.getInitialTransaction();
            natureServiceCode = transactionTmp.getNatureService();

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
            HashMap<Long, CompteSchemaComptable> comptesSchema =
                    compteService.findComptesSchemaComptable(transactionTmp);

            var transaction = TransferMapper.setTransaction(
                    schemaComptable,
                    root,
                    uniteOrganisational,
                    parameteringUtils.getDevise(root.getId()),
                    code,
                    initialtransactionId);

            var savedTransaction = createTransaction(transaction);

            List<EcritureSchemaComptable> ecritureSchemaComptables = accountingMapper.convertToEcritureSchema(
                    ecritureSchemaRepository.findBySchemaIdOrderByRang(schemaComptable.getId()));

            if (ecritureSchemaComptables.isEmpty()) {
                throw new ResourceNotFoundException("Accounting schema not found");
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

                OperationTmp operationTmp = transactionTmp.getOperations().stream()
                        .filter(o -> o.getMontantSchema()
                                        .getId()
                                        .compareTo(ecritureSchema
                                                .getMontantSchema()
                                                .getId())
                                == 0)
                        .findAny()
                        .orElse(null);

                if (operationTmp == null) {
                    throw new ResourceNotFoundException("Temporary operations not found");
                }

                BigDecimal amount = operationTmp.getMontant();
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
            throw new TransactionException("Accounting line entry error : " + e.getMessage());
        }
    }

    @Override
    public void deroulerService(Transaction transaction) {

        if (transaction == null) {
            throw new NotFoundException("Invalid transaction or unknown transaction");
        }

        if (transaction.getStatus() == 'V') {
            throw new TransactionException("Transaction is already validated: " + transaction.getId());
        }

        if (transaction.getStatus() != 'R') {
            throw new TransactionException(
                    "Validate transaction : invalid status for transaction " + transaction.getId());
        }

        transaction.setStatus('V');
        // transaction.setDate(new Date());

        updateTransaction(transaction);

        List<Niveau> niveaux = operationRepository.getNiveauByTransactionId(transaction.getId());
        if (!niveaux.isEmpty()) {
            boolean criticiteHauteTrouvee = false;
            for (Niveau niveau : niveaux) {
                if (niveau.compareTo(Niveau.QUATRE) == 0) {
                    createTransactionMouvementSolde(transaction, Criticite.BAS, 'P');

                } else if (niveau.compareTo(Niveau.TROIS) == 0) {
                    createTransactionMouvementSolde(transaction, Criticite.MOYEN, 'P');

                } else {
                    criticiteHauteTrouvee = true;
                }
            }

            if (criticiteHauteTrouvee) {
                createTransactionMouvementSolde(transaction, Criticite.HAUT, 'P');
            }
        }

        String serviceCode = transaction.getSchemaComptable().getService().getCode();
        AbstractRuntimeService<Journal> abstractService = coreUtils.getRuntimeService(serviceCode);
        if (transaction.getAnnulation().compareTo(OuiNon.NON) == 0) {
            log.info("abstractService.run() method: {}", abstractService);
            // abstractService.run(transactionId);
        } else {
            log.info("abstractService.cancel() method : {}", abstractService);
            //            Transaction initialTransaction = transaction.getTransactions().iterator().next();
            //            initialTransaction.setStatus('C');
            //            abstractService.cancel(initialTransaction.getId());
        }

        try {
            //            ql = "select t from Transaction__ t where t.transactionId =:transactionId";
            //            TypedQuery<Transaction__> query = em.createQuery(ql, Transaction__.class);
            //            query.setParameter("transactionId", transactionId);
            //            em.remove(query.getSingleResult());
            deleteTemporaryTransactionWithTransactionId(transaction.getId());
        } catch (RuntimeException e) {
            // TODO Auto-generated catch block
            throw new TransactionException("Error while processing service : " + e.getMessage());
        }
    }

    @Override
    public TransactionReport exporterTransactionReport(Transaction transaction, TransactionReport report) {

        //        // TODO Auto-generated method stub
        //        Transaction transaction = em.find(Transaction.class, transactionId);
        //        //// Tracking
        //        SessionUtilisateur sessionTrack = transaction.getSessionUtilisateur();
        //        String lObservation = "suid:" + sessionTrack.getId() + " -transid:" + transactionId;
        //        AuditTracking audittrack = null;
        //        audittrack = new AuditTracking();
        //
        //        Tracking tracking = audittrack.AuditTrans("TransactionManagerSessionBean::exporterTransactionReport",
        //                sessionTrack);
        //        tracking.setUserfonction(sessionTrack.getFonction().getCode());
        //        tracking.setPageDemande("ExporterTransactionReport");

        ///////

        if (transaction == null) {
            throw new NotFoundException("Invalid transaction : unknown transaction");
        }

        String serviceCode = transaction.getSchemaComptable().getService().getCode();

        if (transaction.getStatus() == 'F') {
            report.setStatus('F');
            report.setMessage("UNKNOWN TRANSACTION");
            return report;
        }

        if (transaction.getStatus() != 'V' && transaction.getStatus() != 'C') {
            report.setStatus('F');
            report.setMessage("INVALID TRANSACTION STATUS");
            return report;
        }

        // Service service = em.find(Service.class, transaction.getSchemaComptable().getService().getCode());
        TypeService service = accountingMapper.toTypeService(typeServiceRepository
                .findByCode(serviceCode)
                .orElseThrow(() -> new NotFoundException("Service with code " + serviceCode + " not found")));

        AbstractRuntimeService<Journal> abstractService = coreUtils.getRuntimeService(service.getCode());
        // TransactionReport report = abstractService.exportReport(transactionId);
        report.setService(service);
        // UniteOrganisationnelle uniteOrganisationnelle =
        // transaction.getSessionUtilisateur().getUniteOrganisationnelle();
        UniteOrganisational uniteOrganisational = transaction.getLaunchEntity();
        report.setUoName(uniteOrganisational.getLibelle());
        if (uniteOrganisational.getType().getNiveau().getValue() > 2) {
            report.setUoParentName(uniteOrganisational.getParent().getLibelle());
            if (uniteOrganisational.getType().getNiveau().getValue() > 3) {
                report.setUoRootName(uniteOrganisational.getRoot().getLibelle());
            }
        }

        Solde solde = compteService.getSoldeUniteOrganisational(uniteOrganisational.getId());
        report.setSolde(solde);
        // lObservation = lObservation + "-Solde:" + solde;

        if (transaction.getStatus() != 'V') {
            report.setCancelable(false);
            report.setUpdatable(false);
        }

        report.setCreatedAt(transaction.getCreatedAt());
        //            lObservation = lObservation + "-status: " + transaction.getStatus();
        //            tracking.setObservation(lObservation);
        //            audittrack.saveTracking(tracking);

        return report;
    }

    @Override
    public TransactionReport createTransaction(CreateTransactionRequest request) {
        log.info("createTransaction - start :");
        return finaliserTransaction(request.userId(), request.companyId(), request.reference());
    }

    @Override
    public TransactionTmp createTemporaryTransaction(TransactionRequest request) {
        try {
            TransactionTmpEntity newTransaction = transactionTmpMapper.toEntity(request);

            TransactionTmpEntity savedTransaction = transactionTmpRepository.save(newTransaction);

            return transactionTmpMapper.toDto(savedTransaction);

        } catch (RuntimeException e) {
            throw new TransactionException("Error while creating temporary transaction : " + e.getMessage());
        }
    }

    @Override
    public Transaction createTransaction(Transaction transaction) {
        try {
            TransactionEntity newTransaction = transactionMapper.toEntity(transaction);

            TransactionEntity savedTransaction = transactionRepository.save(newTransaction);

            return transactionMapper.toDto(savedTransaction);

        } catch (RuntimeException e) {
            throw new TransactionException("Error while creating transaction: " + e.getMessage());
        }
    }

    @Override
    public void deleteTemporaryTransactionWithTransactionId(Long transactionId) {

        //        Optional<TransactionTmpEntity> deletedTransactionTmpEntity =
        // transactionTmpRepository.findById(transactionId);
        //        transactionTmpRepository.delete(deletedTransactionTmpEntity);
        TransactionTmp deletedTemporaryTransaction = transactionTmpMapper.toDto(transactionTmpRepository
                .findByTransactionId(transactionId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Temporary Transaction with transactionId " + transactionId + " not found")));

        /* Deleting entity */
        transactionTmpRepository.deleteById(deletedTemporaryTransaction.getId());

        log.info("deleteTemporaryTransaction end ok - transactionId: {}", transactionId);
        log.trace("deleteTemporaryTransaction end ok - transactionTmp: {}", deletedTemporaryTransaction);
    }

    @Override
    public void createTransactionMouvementSolde(Transaction transaction, Criticite criticite, Character status) {

        try {
            TransactionMouvementSoldeEntity transactionMouvementSoldeEntity = new TransactionMouvementSoldeEntity();
            transactionMouvementSoldeEntity.setId(new TransactionMouvementSoldePK(transaction.getId(), criticite));
            transactionMouvementSoldeEntity.setStatus(status);
            transactionMouvementSoldeRepository.save(transactionMouvementSoldeEntity);

        } catch (RuntimeException e) {
            throw new TransactionException("Error while creating transactionMouvementSolde : " + e.getMessage());
        }
    }

    @Override
    public TransactionTmp updateTemporaryTransaction(TransactionTmp transactionTmp) {

        /* Getting transactionTmpId */
        Long transactionTmpId = transactionTmp.getId();

        /* Getting existing existingTransactionTmpEntity */
        TransactionTmpEntity existingTransactionTmpEntity = transactionTmpRepository
                .findById(transactionTmpId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        MessageFormat.format("Temporary transaction not found with the id: {0}", transactionTmpId)));

        TransactionTmpEntity transactionTmpEntity = transactionTmpMapper.toEntity(transactionTmp);

        /* Saving entity */
        TransactionTmp updatedTransactionTmp =
                transactionTmpMapper.toDto(transactionTmpRepository.save(transactionTmpEntity));

        log.info("updateTemporaryTransaction end ok - Id: {}", existingTransactionTmpEntity.getId());
        log.trace("updateTemporaryTransaction end ok - transactionTmp: {}", updatedTransactionTmp);

        return updatedTransactionTmp;
    }

    @Override
    public Transaction updateTransaction(Transaction transaction) {
        /* Getting transactionTmpId */
        Long transactionId = transaction.getId();

        /* Getting existing existingTransactionTmpEntity */
        TransactionEntity existingTransactionEntity = transactionRepository
                .findById(transactionId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        MessageFormat.format("Transaction not found with the id: {0}", transactionId)));

        TransactionEntity transactionEntity = transactionMapper.toEntity(transaction);

        /* Saving entity */
        Transaction updatedTransaction = transactionMapper.toDto(transactionRepository.save(transactionEntity));

        log.info("updateTransaction end ok - Id: {}", existingTransactionEntity.getId());
        log.trace("updateTransaction end ok - transaction: {}", updatedTransaction);

        return updatedTransaction;
    }
}
