package com.loulysoft.moneytransfer.accounting.services.impl;

import com.loulysoft.moneytransfer.accounting.exceptions.ResourceNotFoundException;
import com.loulysoft.moneytransfer.accounting.exceptions.TransactionException;
import com.loulysoft.moneytransfer.accounting.mappers.AccountingMapper;
import com.loulysoft.moneytransfer.accounting.mappers.CompanyMapper;
import com.loulysoft.moneytransfer.accounting.mappers.OperationMapper;
import com.loulysoft.moneytransfer.accounting.mappers.ServiceMapper;
import com.loulysoft.moneytransfer.accounting.mappers.TransactionTmpMapper;
import com.loulysoft.moneytransfer.accounting.models.Journal;
import com.loulysoft.moneytransfer.accounting.models.Pays;
import com.loulysoft.moneytransfer.accounting.models.SchemaComptable;
import com.loulysoft.moneytransfer.accounting.models.TransactionContext;
import com.loulysoft.moneytransfer.accounting.models.TransactionReport;
import com.loulysoft.moneytransfer.accounting.models.TransactionTmp;
import com.loulysoft.moneytransfer.accounting.repositories.CompteRepository;
import com.loulysoft.moneytransfer.accounting.repositories.EcritureSchemaComptableRepository;
import com.loulysoft.moneytransfer.accounting.repositories.OperationTmpRepository;
import com.loulysoft.moneytransfer.accounting.repositories.SchemaComptableRepository;
import com.loulysoft.moneytransfer.accounting.repositories.TransactionRepository;
import com.loulysoft.moneytransfer.accounting.repositories.TransactionTmpRepository;
import com.loulysoft.moneytransfer.accounting.repositories.UniteOrganisationalRepository;
import com.loulysoft.moneytransfer.accounting.runtime.AbstractRuntimeService;
import com.loulysoft.moneytransfer.accounting.services.AccountingSchemaService;
import com.loulysoft.moneytransfer.accounting.services.DeviseService;
import com.loulysoft.moneytransfer.accounting.services.OperationService;
import com.loulysoft.moneytransfer.accounting.services.TransactionService;
import com.loulysoft.moneytransfer.accounting.utils.CoreUtils;
import com.loulysoft.moneytransfer.accounting.utils.ParameteringUtils;
import com.loulysoft.moneytransfer.accounting.utils.Utilities;
import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = false)
public class TransactionServiceImpl implements TransactionService {

    private final AccountingSchemaService accountingService;
    private final AccountingMapper accountingMapper;
    private final TransactionRepository transactionRepository;
    private final TransactionTmpRepository transactionTmpRepository;
    private final SchemaComptableRepository schemaComptableRepository;
    private final OperationMapper operationMapper;
    private final TransactionTmpMapper transMapper;
    private final OperationService operationService;
    private final DeviseService deviseService;
    // private final CashInService cashInService;
    private final CoreUtils coreUtils;
    private final Utilities utilities;
    private final UniteOrganisationalRepository uniteOrganisationalRepository;
    private final EcritureSchemaComptableRepository ecritureSchemaRepository;
    private final CompteRepository compteRepository;
    private final OperationTmpRepository operationTmpRepository;
    private final ServiceMapper serviceMapper;
    private final CompanyMapper companyMapper;
    private final ParameteringUtils parameteringUtils;
    // private final CashInService cashInService;

    @Override
    public TransactionReport finaliserTransaction(Long userId, Long companyId, Long reference) {
        //        try {

        // String servicename = (String)
        // transactionContext.getContextItemValue(TransactionContextItem.SERVICE_NAME);

        TransactionReport report = deroulerTransaction(userId, companyId, reference);
        Long transactionId = report.getReference();
        //            transactionManagerSession.controlerSoldeCompte(transactionId);
        //            transactionManagerSession.deroulerService(transactionId);
        //            transactionManagerSession.preparerMouvementCompte(transactionId);

        // return exporterTransactionReport(transactionId);

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
    public TransactionReport deroulerTransaction(Long userId, Long companyId, Long transactionId) {
        log.info("deroulerTransaction : demarrage.....................................................");

        try {
            TransactionContext transactionContext = new TransactionContext();
            TransactionTmp transTmp = transMapper.toDto(transactionTmpRepository
                    .findById(transactionId)
                    .orElseThrow(() ->
                            new ResourceNotFoundException("Transaction with Id " + transactionId + " not found")));

            transactionContext.setTransactionId(transTmp.getId());
            Pays paysDest = deviseService.readPaysByCode(transTmp.getPaysDestination());
            Pays paysOrigine = deviseService.readPaysByCode(transTmp.getPaysSource());

            SchemaComptable schemaComptable = accountingMapper.toSchema(schemaComptableRepository
                    .findById(transTmp.getSchemaComptableId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Accounting Schema with Id " + transTmp.getSchemaComptableId() + " not found")));

            AbstractRuntimeService<Journal> runtimeService =
                    coreUtils.getRuntimeService(schemaComptable.getService().getCode());
            Class<Journal> journalClass = runtimeService.getJournalClass();
            Journal journal = utilities.ecrireLignesComptables(
                    transTmp.getId(), transTmp.getCompanyId(), transactionContext, journalClass);

            TransactionReport transactionReport = runtimeService.preRun(journal, transactionContext);
            if (transactionReport == null) {
                transactionReport = new TransactionReport();
            }

            Journal createdJournal = operationService.createJournal(journal);
            createdJournal.setPaysOrigine(paysOrigine);
            createdJournal.setPaysDest(paysDest);
            BigDecimal principal = createdJournal.getMontant();
            BigDecimal commissions =
                    createdJournal.getFrais().add(createdJournal.getTaxes()).add(createdJournal.getTimbre());
            transactionReport.setJournal(createdJournal);
            transactionReport.setReference(createdJournal.getTransaction().getId());
            transactionReport.setStatus('T');
            transactionReport.setMontant(createdJournal.getMontant());
            transactionReport.setMontantAPayer(createdJournal.getMontant().abs());
            transactionReport.setPrincipal(principal);
            transactionReport.setCommissions(commissions);
            transactionReport.setTotal(principal.add(commissions));
            transactionReport.setFrais(createdJournal.getFrais());
            transactionReport.setTimbre(createdJournal.getTimbre());
            transactionReport.setTaxes(createdJournal.getTaxes());
            transactionReport.setUoName(
                    journal.getTransaction().getLaunchEntity().getLibelle());
            transactionReport.setUoParentName(
                    journal.getTransaction().getLaunchEntity().getParent().getLibelle());
            transactionReport.setUoRootName(journal.getTransaction()
                    .getLaunchEntity()
                    .getParent()
                    .getRoot()
                    .getLibelle());
            transactionReport.setDevise(paysOrigine.getZoneMonetaire().getCode());
            transactionReport.setDevisePayer(paysDest.getZoneMonetaire().getCode());
            log.info("deroulerTransaction : fin............................................................");

            return transactionReport;

        } catch (RuntimeException e) {
            throw new TransactionException("Processing transaction error");
        }
    }

    @Override
    public TransactionReport createTransaction(Long userId, Long companyId, Long reference) {

        log.info("createTransaction - start :");

        TransactionReport report = finaliserTransaction(userId, companyId, reference);

        log.info("createTransaction - end with report: {}", report);

        return report;
    }
}
