package com.loulysoft.moneytransfer.accounting.services;

import com.loulysoft.moneytransfer.accounting.enums.Criticite;
import com.loulysoft.moneytransfer.accounting.enums.OuiNon;
import com.loulysoft.moneytransfer.accounting.enums.TypeCompte;
import com.loulysoft.moneytransfer.accounting.models.Compte;
import com.loulysoft.moneytransfer.accounting.models.CompteSchemaComptable;
import com.loulysoft.moneytransfer.accounting.models.Operation;
import com.loulysoft.moneytransfer.accounting.models.Solde;
import com.loulysoft.moneytransfer.accounting.models.Transaction;
import com.loulysoft.moneytransfer.accounting.models.TransactionTmp;
import java.math.BigDecimal;
import java.util.HashMap;

public interface CompteService {

    HashMap<Long, CompteSchemaComptable> findComptesSchemaComptable(TransactionTmp transTmp);

    void controlerSoldeCompte(Transaction transaction);

    BigDecimal getSoldeDisponible(Compte compte);

    BigDecimal getSolde(Compte compte);

    BigDecimal getPendingDebits(Compte compte);

    BigDecimal getDecouvertApplicable(Compte compte, OuiNon decouvertApplicable);

    Solde getSoldeUniteOrganisational(Long companyId);

    Solde getSoldeUniteOrganisational(Long companyId, TypeCompte typeCompte);

    Compte findCompteUniteOrganisational(Long companyId, TypeCompte typeCompte);

    void applyOperation(Operation operation);

    void mouvementerCompte(Transaction transaction, Criticite criticite);
}
