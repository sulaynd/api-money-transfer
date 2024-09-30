package com.loulysoft.moneytransfer.accounting.controller;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

import com.loulysoft.moneytransfer.accounting.AbstractIT;
import com.loulysoft.moneytransfer.accounting.models.TransactionReport;
import io.restassured.common.mapper.TypeRef;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

@Transactional
public class AccountingControllerTest extends AbstractIT {

    String deviseSource = "XOF";
    String devisePayer = "XOF";
    Long userId = 1002L;
    // Long companyId = 1001L; //SYSTEM_XOF
    // Long companyId = 1003L; //SYSTEM_EUR
    Long companyId = 1269L; // POS -- SN-XOF
    // Long companyId = 1299L; // DISTRIBUTEUR -- FR-EUR
    String paysDest = "SN";
    String serviceCode = "CASH_TRANSFER";
    BigDecimal montant = new BigDecimal(50000);
    String baseUrl = "/api/accounting/ratings?user=" + userId + "&company=" + companyId + "&destination=" + paysDest;
    String urlCreateTransaction = "/api/accounting/transactions?user=" + userId + "&company=" + companyId;

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
    void shouldReturnBadRequestWhenServiceCodeIsWrong() {
        String natureMontant = "PRINCIPAL";
        serviceCode = "TRANSFER_CASH";
        baseUrl = baseUrl + "&nature=" + natureMontant + "&montant=" + montant + "&service=" + serviceCode;
        given().when().get(baseUrl).then().statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void shouldReturnBadRequestWhenPrincipalConvertiIsWrong() {
        String natureMontant = "PRINCIPAL_CONVERTI";
        montant = new BigDecimal(1);
        baseUrl = baseUrl + "&nature=" + natureMontant + "&montant=" + montant + "&service=" + serviceCode;
        given().when().get(baseUrl).then().statusCode(HttpStatus.BAD_REQUEST.value());
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

        urlCreateTransaction = urlCreateTransaction + "&reference=" + report.getReference();

        given().when()
                .get(urlCreateTransaction)
                .then()
                .statusCode(200)
                .body("devise", is(deviseSource))
                .body("devisePayer", is(devisePayer));
    }
}
