package com.loulysoft.moneytransfer.accounting.mappers;

import com.loulysoft.moneytransfer.accounting.entities.MontantSchemaComptableEntity;
import com.loulysoft.moneytransfer.accounting.entities.TransactionTmpEntity;
import com.loulysoft.moneytransfer.accounting.enums.OuiNon;
import com.loulysoft.moneytransfer.accounting.enums.TransactionContextItem;
import com.loulysoft.moneytransfer.accounting.models.Compte;
import com.loulysoft.moneytransfer.accounting.models.Devise;
import com.loulysoft.moneytransfer.accounting.models.EcritureSchemaComptable;
import com.loulysoft.moneytransfer.accounting.models.Operation;
import com.loulysoft.moneytransfer.accounting.models.OperationRequest;
import com.loulysoft.moneytransfer.accounting.models.SchemaComptable;
import com.loulysoft.moneytransfer.accounting.models.Transaction;
import com.loulysoft.moneytransfer.accounting.models.TransactionContext;
import com.loulysoft.moneytransfer.accounting.models.TransactionRequest;
import com.loulysoft.moneytransfer.accounting.models.UniteOrganisational;
import java.math.BigDecimal;

public class TransferMapper {

    public static TransactionRequest buildTransactionRequest(
            Long userId,
            Long companyId,
            Long schemaComptableId,
            String paysSource,
            String deviseSource,
            TransactionContext transactionContext) {
        String paysDest = (String) transactionContext.getContextItemValue(TransactionContextItem.DESTINATION_COUNTRY);
        String natureServiceCode =
                (String) transactionContext.getContextItemValue(TransactionContextItem.NATURE_SERVICE);
        //        String autreParametre = (String) serviceContext.getServiceContext()
        //                .get(ServiceContextItem.AUTRE_PARAMETRE.name());
        TransactionRequest request = new TransactionRequest();
        request.setSchemaComptableId(schemaComptableId);
        request.setAutreParametre(null);
        request.setNatureService(natureServiceCode);
        request.setUserId(userId);
        request.setInitialTransaction(null);
        request.setDevise(deviseSource);
        request.setPaysDestination(paysDest);
        request.setPaysSource(paysSource);
        request.setCompanyId(companyId);
        request.setEntiteTierceId(null);

        return request;
    }

    public static OperationRequest buildOperationRequest(
            BigDecimal montant, MontantSchemaComptableEntity montantSchema, TransactionTmpEntity transaction) {
        OperationRequest request = new OperationRequest();
        request.setMontant(montant);
        request.setMontantSchema(montantSchema);
        request.setTransaction(transaction);

        return request;
    }

    public static Transaction setTransaction(
            SchemaComptable schemaComptable,
            UniteOrganisational root,
            UniteOrganisational launchEntity,
            Devise devise,
            String pickupCode,
            Long initialTransactionId) {

        Transaction transaction = new Transaction();
        transaction.setStatus('T');
        transaction.setSchemaComptable(schemaComptable);
        transaction.setAnnulation(OuiNon.NON);
        transaction.setRoot(root);
        transaction.setLaunchEntity(launchEntity);
        transaction.setService(schemaComptable.getService());
        transaction.setDevise(devise);
        transaction.setPickupCode(pickupCode);
        transaction.setInitialTransaction(initialTransactionId);

        return transaction;
    }

    public static Operation setOperation(
            Transaction transaction,
            Compte compte,
            BigDecimal montant,
            EcritureSchemaComptable ecritureSchema,
            char direction) {

        Operation operation = new Operation();
        operation.setTransaction(transaction);
        operation.setCompte(compte);
        operation.setAmount(montant);
        operation.setEcritureSchemaComptable(ecritureSchema);
        operation.setDirection(direction);

        return operation;
    }
}
