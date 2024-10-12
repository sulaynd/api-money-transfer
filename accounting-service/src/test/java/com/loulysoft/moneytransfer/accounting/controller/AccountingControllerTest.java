package com.loulysoft.moneytransfer.accounting.controller;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;

import com.loulysoft.moneytransfer.accounting.AbstractIT;
import com.loulysoft.moneytransfer.accounting.models.TransactionReport;
import com.loulysoft.moneytransfer.accounting.repositories.OperationTmpRepository;
import com.loulysoft.moneytransfer.accounting.repositories.TransactionTmpRepository;
import com.loulysoft.moneytransfer.accounting.services.CashInService;
import com.loulysoft.moneytransfer.accounting.services.TransactionService;
import com.loulysoft.moneytransfer.accounting.testdata.TestDataFactory;
import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

@Transactional
public class AccountingControllerTest extends AbstractIT {
    @Autowired
    private TransactionTmpRepository transactionTmpRepository;

    @Autowired
    private OperationTmpRepository operationTmpRepository;

    @Autowired
    private CashInService cashInService;

    @Autowired
    private TransactionService transactionService;

    String deviseSource = "XOF";
    String devisePayer = "XOF";
    Long userId = 1002L;
    // Long companyId = 1001L; //SYSTEM_XOF
    // Long companyId = 1003L; //SYSTEM_EUR
    Long companyId = 1269L; // POS -- SN-XOF
    // Long companyId = 1299L; // DISTRIBUTEUR -- FR-EUR
    String paysDest = "SN";
    String serviceCode = "CASH_TRANSFER";
    BigDecimal montant = new BigDecimal(5000);
    String baseUrl = "/api/accounting/ratings?user=" + userId + "&company=" + companyId + "&destination=" + paysDest;
    // String urlCreateTransaction = "/api/accounting/transactions?user=" + userId + "&company=" + companyId;

    //    @BeforeEach
    //    public void setup(){
    //        mockMvc = MockMvcBuilders.standaloneSetup(new
    // AccountingController(cashInService,transactionService)).build();
    //    }
    @Test
    void shouldCalculatePrincipalAndCommissionsByTotalSuccessfully() {
        String natureMontant = "PRINCIPAL_FEE";
        baseUrl = baseUrl + "&nature=" + natureMontant + "&montant=" + montant + "&service=" + serviceCode;
        given().when()
                .get(baseUrl)
                .then()
                .statusCode(200)
                .body("devise", is(deviseSource))
                .body("devisePayer", is(devisePayer));
    }

    @Test
    void shouldCalculatePrincipalAndCommissionsByReceivedAmountSuccessfully() {
        String natureMontant = "PRINCIPAL_CONVERTI";
        baseUrl = baseUrl + "&nature=" + natureMontant + "&montant=" + montant + "&service=" + serviceCode;
        given().when()
                .get(baseUrl)
                .then()
                .statusCode(200)
                .body("devise", is(deviseSource))
                .body("devisePayer", is(devisePayer));
    }

    @Test
    void shouldCalculatePrincipalAndCommissionsSuccessfully() {
        String natureMontant = "PRINCIPAL";
        baseUrl = baseUrl + "&nature=" + natureMontant + "&montant=" + montant + "&service=" + serviceCode;
        given().when()
                .get(baseUrl)
                .then()
                .statusCode(200)
                .body("devise", is(deviseSource))
                .body("devisePayer", is(devisePayer));
    }

    @Test
    void shouldReturnNotFoundExceptionWhenServiceCodeIsExist() {
        String natureMontant = "PRINCIPAL";
        serviceCode = "CASH";
        baseUrl = baseUrl + "&nature=" + natureMontant + "&montant=" + montant + "&service=" + serviceCode;
        given().when().get(baseUrl).then().statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    void shouldReturnInternalServerErrorWhenInvalidAmount() {
        String natureMontant = "PRINCIPAL_CONVERTI";
        montant = new BigDecimal(1);
        baseUrl = baseUrl + "&nature=" + natureMontant + "&montant=" + montant + "&service=" + serviceCode;
        given().when().get(baseUrl).then().statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    @Test
    void shouldReturnUnauthorizedExceptionWhenCompanyTypeIsUnauthorized() {
        String natureMontant = "PRINCIPAL_FEE";
        companyId = 1001L;
        baseUrl = "/api/accounting/ratings?user=" + userId + "&company=" + companyId + "&destination=" + paysDest
                + "&nature=" + natureMontant + "&montant=" + montant + "&service=" + serviceCode;
        given().when().get(baseUrl).then().statusCode(HttpStatus.UNAUTHORIZED.value());
    }

    @Test
    @Transactional
    void shouldCreateTransactionWithPrincipalAndCommissionsByTotalSuccessfully() {
        String natureMontant = "PRINCIPAL_FEE";
        baseUrl = baseUrl + "&nature=" + natureMontant + "&montant=" + montant + "&service=" + serviceCode;

        TransactionReport report = given().when()
                .get(baseUrl)
                .then()
                .statusCode(200)
                .extract()
                .body()
                .as(new TypeRef<>() {});

        // urlCreateTransaction = urlCreateTransaction + "&reference=" + report.getReference();

        //        given().when()
        //                .get(urlCreateTransaction)
        //                .then()
        //                .statusCode(200)
        //                .body("devise", is(deviseSource))
        //                .body("devisePayer", is(devisePayer));

        assertThat(report.getReference()).isNotNull();

        var payload = TestDataFactory.createValidTransactionRequest(userId, companyId, report.getReference());

        given().contentType(ContentType.JSON)
                // .header("Authorization", "Bearer " + getToken())
                .body(payload)
                .when()
                .post("/api/accounting/transactions")
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .body("devisePayer", notNullValue());

        assertThat(transactionTmpRepository.findAll()).isEmpty();

        assertThat(operationTmpRepository.findAll()).isEmpty();
    }

    @Test
    void shouldReturnBadRequestWhenMandatoryDataIsMissing() {
        var payload = TestDataFactory.createTransactionRequestWithInvalidReference(userId, companyId, null);

        given().contentType(ContentType.JSON)
                .body(payload)
                .when()
                .post("/api/accounting/transactions")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void shouldReturnResourceNotFoundExceptionWhenReferenceNotExist() {
        var payload = TestDataFactory.createTransactionRequestWithInvalidReference(userId, companyId, 999L);

        given().contentType(ContentType.JSON)
                .body(payload)
                .when()
                .post("/api/accounting/transactions")
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }
}
