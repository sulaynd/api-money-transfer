package com.loulysoft.moneytransfer.accounting.services.impl;

import com.loulysoft.moneytransfer.accounting.enums.TransactionContextItem;
import com.loulysoft.moneytransfer.accounting.exceptions.TransactionException;
import com.loulysoft.moneytransfer.accounting.models.Devise;
import com.loulysoft.moneytransfer.accounting.models.Pays;
import com.loulysoft.moneytransfer.accounting.models.TransactionContext;
import com.loulysoft.moneytransfer.accounting.models.TransactionReport;
import com.loulysoft.moneytransfer.accounting.services.AccountingSchemaService;
import com.loulysoft.moneytransfer.accounting.services.CashInService;
import com.loulysoft.moneytransfer.accounting.services.DeviseService;
import java.math.BigDecimal;
import java.util.LinkedHashMap;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = false)
public class CashInServiceImpl implements CashInService {

    private final AccountingSchemaService accountingSchemaService;

    private final DeviseService deviseService;

    private TransactionReport report;

    private BigDecimal inputAmount;

    private String pays;

    private LinkedHashMap<String, String> optionNatureMontant;

    private void calculatePrincipalAndCommissionsByTotal(
            Long userId, Long companyId, Pays paysDest, String serviceCode, TransactionContext transactionContext) {

        transactionContext.addContextItem(TransactionContextItem.DESTINATION_COUNTRY, pays);
        transactionContext.addContextItem(TransactionContextItem.PRINCIPAL_FEE, inputAmount);

        report = accountingSchemaService.demarrerTransaction(
                userId, companyId, paysDest, serviceCode, transactionContext);

        log.info("calculatePrincipalAndCommissionsByTotal - report :" + report);
    }

    private void calculatePrincipalAndCommissionsByReceivedAmount(
            Long userId, Long companyId, Pays paysDest, String serviceCode, TransactionContext transactionContext) {

        transactionContext.addContextItem(TransactionContextItem.DESTINATION_COUNTRY, pays);
        transactionContext.addContextItem(TransactionContextItem.PRINCIPAL_CONVERTI, inputAmount);
        report = accountingSchemaService.demarrerTransaction(
                userId, companyId, paysDest, serviceCode, transactionContext);
        log.info("calculatePrincipalAndCommissionsByReceivedAmount - report : " + report);
    }

    @Override
    public TransactionReport calculerDetailsMontant(
            Long userId,
            Long companyId,
            String paysDest,
            String serviceCode,
            String natureMontant,
            BigDecimal montant) {

        //   TypeService typeService = new TypeService();
        // service = service.toUpperCase();
        //        if(service.equalsIgnoreCase("CASH_TRANSFER")){
        //            typeService.setCode("CASH_TRANSFER");
        //            typeService.setComposant("com.loulysoft.moneytransfer.accounting.runtime.RuntimeCashTransfer");
        //        }

        serviceCode = serviceCode.toUpperCase();
        paysDest = paysDest.toUpperCase();
        natureMontant = natureMontant.toUpperCase();
        montant = new BigDecimal(String.valueOf(montant));
        inputAmount = montant;
        pays = paysDest;
        TransactionContext transactionContext = new TransactionContext();

        try {

            Pays paysReceiver = deviseService.readPaysByCode(paysDest);
            Devise devisePayer = paysReceiver.getZoneMonetaire().getDevise();

            transactionContext.addContextItem(TransactionContextItem.COMPANY_ID, companyId);
            // BigDecimal decimalPlaces = BigDecimal.valueOf(deviseSender.getUniteMonetaire());

            if (!natureMontant.isEmpty()) {
                if (natureMontant.equals("PRINCIPAL_CONVERTI")) {
                    //  decimalPlaces = BigDecimal.valueOf(devisePayer.getUniteMonetaire());
                }
                if (inputAmount != null && inputAmount.compareTo(BigDecimal.ZERO) > 0) {
                    switch (natureMontant) {
                        case "PRINCIPAL_FEE" -> calculatePrincipalAndCommissionsByTotal(
                                userId, companyId, paysReceiver, serviceCode, transactionContext);
                        case "PRINCIPAL_CONVERTI" -> calculatePrincipalAndCommissionsByReceivedAmount(
                                userId, companyId, paysReceiver, serviceCode, transactionContext);
                        case "PRINCIPAL" -> {
                            transactionContext.addContextItem(TransactionContextItem.DESTINATION_COUNTRY, pays);
                            transactionContext.addContextItem(TransactionContextItem.PRINCIPAL, inputAmount);
                            report = accountingSchemaService.demarrerTransaction(
                                    userId, companyId, paysReceiver, serviceCode, transactionContext);

                            log.info("report -------- " + report);
                        }
                    }
                }
            }

            return report;
        } catch (TransactionException e) {
            log.error(e.getMessage(), e);
            throw new TransactionException("CalculerDetailsMontant - error processing :" + e.getMessage());
        }
    }
}
