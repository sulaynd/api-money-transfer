package com.loulysoft.moneytransfer.accounting.unit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import com.loulysoft.moneytransfer.accounting.enums.TransactionContextItem;
import com.loulysoft.moneytransfer.accounting.models.Devise;
import com.loulysoft.moneytransfer.accounting.models.Pays;
import com.loulysoft.moneytransfer.accounting.models.TransactionContext;
import com.loulysoft.moneytransfer.accounting.models.TransactionReport;
import com.loulysoft.moneytransfer.accounting.models.TypeUniteOrganisational;
import com.loulysoft.moneytransfer.accounting.models.UniteOrganisational;
import com.loulysoft.moneytransfer.accounting.models.Users;
import com.loulysoft.moneytransfer.accounting.services.AccountingSchemaService;
import com.loulysoft.moneytransfer.accounting.services.CashInService;
import com.loulysoft.moneytransfer.accounting.services.DeviseService;
import com.loulysoft.moneytransfer.accounting.services.TransactionService;
import com.loulysoft.moneytransfer.accounting.services.impl.CashInServiceImpl;
import com.loulysoft.moneytransfer.accounting.testdata.TestDataFactory;
import java.math.BigDecimal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class CashInServiceTest {

    @Mock
    private AccountingSchemaService accountingSchemaService;

    @Mock
    private DeviseService deviseService;

    private CashInService cashInService;

    private TransactionService transactionService;

    private TransactionReport report;

    private Devise deviseCible;

    private Pays paysDestination;

    private UniteOrganisational company;

    private TypeUniteOrganisational type;

    private Pays paysSource;

    private Users user;

    @BeforeEach
    public void setup() {
        report = TestDataFactory.getTransactionReport();
        company = TestDataFactory.getUniteOrganisational();
        deviseCible = TestDataFactory.getDevise();
        paysDestination = TestDataFactory.getPays();
        user = TestDataFactory.getUser();
        // typeService = TestDataFactory.getTypeService();
        type = TestDataFactory.getTypeUniteOrganisational();
        paysSource = TestDataFactory.getPays();
        user = TestDataFactory.getUser();
        cashInService = new CashInServiceImpl(accountingSchemaService, deviseService, transactionService);
    }

    @Test
    public void calculatePrincipalAndCommissionsByTotalTest() {

        when(accountingSchemaService.demarrerTransaction(any(), any(), any(), any(), any()))
                .thenReturn(report);
        when(deviseService.readPaysByCode(anyString())).thenReturn(paysSource);
        String natureMontant = "PRINCIPAL_FEE";
        BigDecimal montant = new BigDecimal(5000);
        TransactionContext transactionContext = new TransactionContext();
        transactionContext.addContextItem(TransactionContextItem.DEVISE, deviseCible);
        transactionContext.addContextItem(TransactionContextItem.PRINCIPAL_FEE, montant);
        transactionContext.addContextItem(TransactionContextItem.DESTINATION_COUNTRY, paysDestination);
        // typeService.setComposant("com.loulysoft.moneytransfer.accounting.runtime.RuntimeCashTransfer");

        TransactionReport report = cashInService.calculerDetailsMontant(
                user.getId(), company.getId(), paysDestination.getCode(), "CASH_TRANSFER", natureMontant, montant);

        assertThat(report).isNotNull();
    }

    @Test
    public void calculatePrincipalAndCommissionsByReceivedAmountTest() {

        when(accountingSchemaService.demarrerTransaction(any(), any(), any(), any(), any()))
                .thenReturn(report);
        when(deviseService.readPaysByCode(anyString())).thenReturn(paysSource);
        String natureMontant = "PRINCIPAL_CONVERTI";
        BigDecimal montant = new BigDecimal(50000);
        TransactionContext transactionContext = new TransactionContext();
        transactionContext.addContextItem(TransactionContextItem.DEVISE, deviseCible);
        transactionContext.addContextItem(TransactionContextItem.PRINCIPAL_CONVERTI, montant);
        transactionContext.addContextItem(TransactionContextItem.DESTINATION_COUNTRY, paysDestination);
        // typeService.setComposant("com.loulysoft.moneytransfer.accounting.runtime.RuntimeCashTransfer");

        TransactionReport report = cashInService.calculerDetailsMontant(
                user.getId(), company.getId(), paysDestination.getCode(), "CASH_TRANSFER", natureMontant, montant);

        assertThat(report).isNotNull();
    }
}
