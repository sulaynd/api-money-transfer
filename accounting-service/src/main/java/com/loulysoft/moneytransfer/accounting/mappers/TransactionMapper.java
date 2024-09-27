package com.loulysoft.moneytransfer.accounting.mappers;

import com.loulysoft.moneytransfer.accounting.entities.MontantSchemaComptableEntity;
import com.loulysoft.moneytransfer.accounting.entities.OperationEntity;
import com.loulysoft.moneytransfer.accounting.entities.TransactionEntity;
import com.loulysoft.moneytransfer.accounting.enums.TransactionContextItem;
import com.loulysoft.moneytransfer.accounting.models.OperationRequest;
import com.loulysoft.moneytransfer.accounting.models.TransactionContext;
import com.loulysoft.moneytransfer.accounting.models.TransactionRequest;
import java.math.BigDecimal;

public class TransactionMapper {

    public static TransactionEntity convertToEntity(TransactionRequest request) {
        TransactionEntity newTransaction = new TransactionEntity();
        newTransaction.setSchemaComptable(request.getSchemaComptable());
        newTransaction.setUserId(request.getUserId());
        newTransaction.setAutreParametre(request.getAutreParametre());
        newTransaction.setNatureService(request.getNatureService());
        newTransaction.setInitialTransaction(request.getInitialTransaction());
        newTransaction.setDevise(request.getDevise());
        newTransaction.setPaysDestination(request.getPaysDestination());
        newTransaction.setPaysSource(request.getPaysSource());
        newTransaction.setAutreParametre(request.getAutreParametre());
        newTransaction.setInitialTransaction(request.getInitialTransaction());
        newTransaction.setCompanyId(request.getCompanyId());
        newTransaction.setEntiteTierceId(request.getEntiteTierceId());

        return newTransaction;
    }

    public static OperationEntity convertToEntity(OperationRequest request) {
        OperationEntity newOperation = new OperationEntity();
        newOperation.setTransaction(request.getTransaction());
        newOperation.setMontantSchema(request.getMontantSchema());
        newOperation.setMontant(request.getMontant());

        return newOperation;
    }

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
        request.setSchemaComptable(schemaComptableId);
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
            BigDecimal montant, MontantSchemaComptableEntity montantSchema, TransactionEntity transaction) {
        OperationRequest request = new OperationRequest();
        request.setMontant(montant);
        request.setMontantSchema(montantSchema);
        request.setTransaction(transaction);

        return request;
    }
}
