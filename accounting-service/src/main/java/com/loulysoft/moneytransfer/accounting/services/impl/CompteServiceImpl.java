package com.loulysoft.moneytransfer.accounting.services.impl;

import com.loulysoft.moneytransfer.accounting.entities.TransactionMouvementSoldePK;
import com.loulysoft.moneytransfer.accounting.enums.Category;
import com.loulysoft.moneytransfer.accounting.enums.Code;
import com.loulysoft.moneytransfer.accounting.enums.Criticite;
import com.loulysoft.moneytransfer.accounting.enums.DebitCredit;
import com.loulysoft.moneytransfer.accounting.enums.Niveau;
import com.loulysoft.moneytransfer.accounting.enums.NodeType;
import com.loulysoft.moneytransfer.accounting.enums.OuiNon;
import com.loulysoft.moneytransfer.accounting.enums.TypeCompte;
import com.loulysoft.moneytransfer.accounting.enums.UniteOrganisationalType;
import com.loulysoft.moneytransfer.accounting.exceptions.InsufficientBalanceException;
import com.loulysoft.moneytransfer.accounting.exceptions.NotFoundException;
import com.loulysoft.moneytransfer.accounting.exceptions.ResourceNotFoundException;
import com.loulysoft.moneytransfer.accounting.exceptions.TransactionException;
import com.loulysoft.moneytransfer.accounting.mappers.AccountingMapper;
import com.loulysoft.moneytransfer.accounting.mappers.CompteMapper;
import com.loulysoft.moneytransfer.accounting.mappers.OperationMapper;
import com.loulysoft.moneytransfer.accounting.mappers.ServiceMapper;
import com.loulysoft.moneytransfer.accounting.mappers.TransactionMapper;
import com.loulysoft.moneytransfer.accounting.models.Compte;
import com.loulysoft.moneytransfer.accounting.models.CompteSchemaComptable;
import com.loulysoft.moneytransfer.accounting.models.Devise;
import com.loulysoft.moneytransfer.accounting.models.Operation;
import com.loulysoft.moneytransfer.accounting.models.ParametreRecherche;
import com.loulysoft.moneytransfer.accounting.models.Solde;
import com.loulysoft.moneytransfer.accounting.models.Transaction;
import com.loulysoft.moneytransfer.accounting.models.TransactionMouvementSolde;
import com.loulysoft.moneytransfer.accounting.models.TransactionTmp;
import com.loulysoft.moneytransfer.accounting.models.TypeUniteOrganisational;
import com.loulysoft.moneytransfer.accounting.models.UniteOrganisational;
import com.loulysoft.moneytransfer.accounting.repositories.CompteRepository;
import com.loulysoft.moneytransfer.accounting.repositories.CompteSchemaComptableRepository;
import com.loulysoft.moneytransfer.accounting.repositories.OperationRepository;
import com.loulysoft.moneytransfer.accounting.repositories.TransactionMouvementSoldeRepository;
import com.loulysoft.moneytransfer.accounting.services.CompteService;
import com.loulysoft.moneytransfer.accounting.utils.CompteUtils;
import com.loulysoft.moneytransfer.accounting.utils.ParameteringUtils;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CompteServiceImpl implements CompteService {

    private final AccountingMapper accountingMapper;

    private final CompteSchemaComptableRepository compteSchemaComptableRepository;

    private final CompteRepository compteRepository;

    private final ServiceMapper serviceMapper;

    private final ParameteringUtils parameteringUtils;

    private final OperationRepository operationRepository;

    private final TransactionMouvementSoldeRepository transactionMouvementSoldeRepository;

    private final OperationMapper operationMapper;

    private final TransactionMapper transactionMapper;

    private final CompteMapper compteMapper;

    @Override
    public HashMap<Long, CompteSchemaComptable> findComptesSchemaComptable(TransactionTmp transTmp) {

        if (transTmp == null) {
            throw new NotFoundException("Invalid temporary transaction or unknown temporary transaction");
        }

        List<CompteSchemaComptable> comptesSchema = accountingMapper.convertToCompteSchemaComptable(
                compteSchemaComptableRepository.findBySchemaId(transTmp.getSchemaComptableId()));
        if (comptesSchema.isEmpty()) {
            throw new ResourceNotFoundException(
                    "Schema account with transactionId " + transTmp.getId() + " not found ");
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

    @Override
    public void controlerSoldeCompte(Transaction transaction) {

        if (transaction == null) {
            throw new NotFoundException("Invalid transaction or unknown transaction");
        }

        if (transaction.getStatus() != 'T') {
            throw new TransactionException("INVALID TRANSACTION STATUS");
        }
        List<Operation> operations =
                operationMapper.toDtoList(operationRepository.findOperationsByTransactionIdAndCategoryOrderById(
                        transaction.getId(), Category.PRINCIPAL));

        HashMap<Long, BigDecimal> soldeMap = new HashMap<>();
        OuiNon decouvertApplicable =
                transaction.getSchemaComptable().getService().getDecouvertApplicable();

        for (Operation operation : operations) {
            BigDecimal amount = operation.getAmount();
            Compte compte = operation.getCompte();
            if (!soldeMap.containsKey(compte.getId())) {
                BigDecimal soldeDisponible = getSoldeDisponible(compte);
                soldeMap.put(compte.getId(), soldeDisponible.negate());
            }

            BigDecimal nouvSolde = amount.add(soldeMap.get(compte.getId()));

            BigDecimal decouvert = getDecouvertApplicable(compte, decouvertApplicable);

            if (nouvSolde.compareTo(decouvert) > 0) { // Solde debiteur
                throw new InsufficientBalanceException("Insufficient Balance: AccountId : " + compte.getId()
                        + ", new balance : " + nouvSolde.negate() + ", decouvert : " + decouvert);
            }

            soldeMap.put(compte.getId(), nouvSolde);
        }
        // transaction.setCreatedAt(new Date());
        transaction.setStatus('R');
    }

    @Override
    public BigDecimal getSoldeDisponible(Compte compte) {

        if (compte == null) {
            throw new NotFoundException("Invalid account or unknown account");
        }

        BigDecimal value = getSolde(compte);
        BigDecimal pendingDebits = getPendingDebits(compte);

        value = value.subtract(pendingDebits);

        return value;
    }

    @Override
    public BigDecimal getSolde(Compte compte) {

        if (compte == null) {
            throw new NotFoundException("Invalid account owner or unknown account");
        }
        BigDecimal value = compte.getSolde();

        try {
            List<BigDecimal> variation =
                    operationRepository.getTotalByCompteIdAndTransactionStatus(compte.getId(), 'R');
            if (!variation.isEmpty()) {
                BigDecimal amount = variation.getFirst();
                if (amount != null) {
                    value = value.add(amount);
                }
            }
            Niveau niveau = compte.getOwner().getType().getNiveau();
            Criticite criticite = CompteUtils.getCriticiteByNiveau(niveau);
            variation = operationRepository.getTotalByCompteIdAndTransactionStatusInAndTmsStatusInAndCriticite(
                    compte.getId(), Arrays.asList('V', 'C'), Arrays.asList('P', 'B'), criticite);
            if (!variation.isEmpty()) {
                BigDecimal amount = variation.getFirst();
                if (amount != null) {
                    value = value.add(amount);
                }
            }

            return value.negate();
        } catch (DataAccessException e) {
            // TODO Auto-generated catch block
            log.error(e.getMessage(), e);
            throw new NotFoundException("Account with id " + compte.getId() + " not found");
        }
    }

    @Override
    public BigDecimal getPendingDebits(Compte compte) {

        if (compte == null) {
            throw new NotFoundException("Invalid account owner or unknown account");
        }

        BigDecimal value = BigDecimal.ZERO;
        try {
            BigDecimal variation = operationRepository.getTotalByCompteIdAndTransactionStatusInAndTypeOperation(
                    compte.getId(), Arrays.asList('Y', 'Z'), DebitCredit.DEBIT);
            if (variation != null) {
                value = variation;
            }
        } catch (DataAccessException e) {
            // TODO Auto-generated catch block
            throw new TransactionException("Pending Debits Error");
        }

        return value;
    }

    @Override
    public BigDecimal getDecouvertApplicable(Compte compte, OuiNon decouvertApplicable) {

        if (compte == null) {
            throw new NotFoundException("Invalid account owner or unknown account");
        }

        BigDecimal decouvert = BigDecimal.ZERO;

        if (compte.getMaxSolde() != null) {
            if (compte.getOwner().getType().getNiveau().getValue() <= 2) {
                decouvert = compte.getMaxSolde();
            } else if (compte.getOwner().getType().getCode().equals(UniteOrganisationalType.DISTRIBUTEUR.name())) {
                if (decouvertApplicable != null && decouvertApplicable.compareTo(OuiNon.OUI) == 0) {
                    decouvert = compte.getMaxSolde();
                }
            }
        }
        return decouvert;
    }

    @Override
    public Solde getSoldeUniteOrganisational(Long companyId) {

        Solde solde = getSoldeUniteOrganisational(companyId, TypeCompte.COURANT);
        solde.setCommissionJour(BigDecimal.ZERO);
        Calendar calendar = Calendar.getInstance();
        //            calendar.set(Calendar.HOUR_OF_DAY, 0);
        //            calendar.set(Calendar.MINUTE, 0);
        //            calendar.set(Calendar.SECOND, 0);
        LocalDateTime localDateTime = CompteUtils.toLocalDateTime(calendar);

        //            String ql = "select sum(o.amount) from Operation o join o.ecritureSchemaComptable esc join
        // o.transaction t where esc.codeEcriture.code in (:codes)"
        //                    + " and t.status in (:status) and t.date >=:date and o.compte.id =:compteId";
        //            Query query = em.createQuery(ql);
        //            query.setParameter("codes", Arrays.asList(Code.COMMISSION_HORS_TAXE, Code.COMMISSION_TAXE));
        //            query.setParameter("status", Arrays.asList('V', 'C'));
        //            query.setParameter("date", calendar.getTime());
        //            query.setParameter("compteId", solde.getCompteId());
        //            List<Object> results = query.getResultList();
        List<BigDecimal> results =
                operationRepository
                        .getTotalByEcritureSchemaComptableCodesInAndTransactionStatusInAndTransactionCreatedAtAndCompteId(
                                Arrays.asList(Code.COMMISSION_HORS_TAXE, Code.COMMISSION_TAXE),
                                Arrays.asList('V', 'C'),
                                localDateTime,
                                solde.getCompteId());
        if (results != null && !results.isEmpty()) {
            BigDecimal commission = results.getFirst();
            if (commission != null) {
                solde.setCommissionJour(commission.negate());
            }
        }
        return solde;
    }

    @Override
    public Solde getSoldeUniteOrganisational(Long companyId, TypeCompte typeCompte) {

        Compte compte = findCompteUniteOrganisational(companyId, typeCompte);
        BigDecimal valeur = getSolde(compte);
        BigDecimal attenteDebit = getPendingDebits(compte);
        Devise devise = parameteringUtils.getDevise(companyId);
        Solde solde = new Solde(compte.getId(), valeur, valeur.subtract(attenteDebit), devise.getCode());
        solde.setReference(compte.getOwner().getCode());
        solde.setDecouvert(compte.getMaxSolde());
        TypeUniteOrganisational type = compte.getOwner().getType();
        solde.setVisible(true);
        if (type.getNiveau().getValue() <= 2 && type.getNodeType().compareTo(NodeType.LEAF) != 0) {
            solde.setVisible(false);
        }

        return solde;
    }

    @Override
    public Compte findCompteUniteOrganisational(Long companyId, TypeCompte typeCompte) {

        return serviceMapper.toCompte(compteRepository
                .findByTypeCompteCodeAndOwnerId(typeCompte.name(), companyId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Account with type " + typeCompte.name() + " and company id " + companyId + " not found")));
    }

    @Override
    public void applyOperation(Operation operation) {

        if (operation == null) {
            throw new NotFoundException("Invalid operation : unknown operation");
        }
        DebitCredit direction = operation.getEcritureSchemaComptable().getDirection();
        Transaction transaction = operation.getTransaction();
        Long compteId = operation.getCompte().getId();
        BigDecimal amount = operation.getAmount();
        if (amount.compareTo(BigDecimal.ZERO) != 0) {
            Compte compte = compteMapper.toDto(compteRepository
                    .findById(compteId)
                    .orElseThrow(() -> new ResourceNotFoundException("Account with id " + compteId + " not found")));

            if (direction.compareTo(DebitCredit.DEBIT) == 0) {
                if (transaction.getAnnulation().compareTo(OuiNon.NON) == 0) {
                    compte.setSolde(compte.getSolde().add(amount.abs()));
                } else {
                    compte.setSolde(compte.getSolde().subtract(amount.abs()));
                }
            } else {
                if (transaction.getAnnulation().compareTo(OuiNon.NON) == 0) {
                    compte.setSolde(compte.getSolde().subtract(amount.abs()));
                } else {
                    compte.setSolde(compte.getSolde().add(amount.abs()));
                }
            }
            operation.setNewSolde(compte.getSolde());
            compte.setDernierMouvement(LocalDateTime.now());
        }
    }

    @Override
    public void mouvementerCompte(Transaction transaction, Criticite criticite) {

        if (transaction == null) {
            throw new NotFoundException("Invalid transaction : unknown transaction");
        }

        if (transaction.getStatus() != 'R' && transaction.getStatus() != 'V') {
            throw new TransactionException("INVALID TRANSACTION STATUS");
        }

        TransactionMouvementSoldePK id = new TransactionMouvementSoldePK(transaction.getId(), criticite);

        TransactionMouvementSolde transactionMouvementSolde =
                transactionMapper.toDto(transactionMouvementSoldeRepository
                        .findById(id)
                        .orElseThrow(
                                () -> new ResourceNotFoundException("Transaction balance movement with transactionId "
                                        + transaction.getId() + "and criticite " + criticite + " not found")));

        List<Category> categories = Arrays.asList(Category.PRINCIPAL, Category.SECONDAIRE);
        List<Niveau> niveaux = CompteUtils.getNiveauxByCriticite(criticite);

        List<Operation> operations = operationMapper.toDtoList(
                operationRepository.findOperationsByTransactionIdAndNiveauInAndCategoryInOrderById(
                        transaction.getId(), niveaux, categories));

        for (Operation operation : operations) {
            applyOperation(operation);
        }

        transactionMouvementSolde.setStatus('V');
        // transactionMouvementSolde.setCreatedAt(LocalDateTime.now());
    }
}
