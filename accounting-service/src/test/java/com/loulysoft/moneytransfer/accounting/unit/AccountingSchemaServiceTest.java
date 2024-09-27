// package com.loulysoft.moneytransfer.accounting.unit;
//
// import static org.assertj.core.api.Assertions.assertThat;
// import static org.mockito.ArgumentMatchers.any;
// import static org.mockito.ArgumentMatchers.anyString;
// import static org.mockito.BDDMockito.given;
//
// import com.loulysoft.moneytransfer.accounting.entities.EcritureSchemaComptableEntity;
// import com.loulysoft.moneytransfer.accounting.entities.MontantParamSchemaComptableEntity;
// import com.loulysoft.moneytransfer.accounting.entities.MontantSchemaComptableEntity;
// import com.loulysoft.moneytransfer.accounting.entities.SchemaComptableEntity;
// import com.loulysoft.moneytransfer.accounting.enums.Variant;
// import com.loulysoft.moneytransfer.accounting.mappers.AccountingMapper;
// import com.loulysoft.moneytransfer.accounting.models.Devise;
// import com.loulysoft.moneytransfer.accounting.models.EcritureSchemaComptable;
// import com.loulysoft.moneytransfer.accounting.models.MontantParamSchemaComptable;
// import com.loulysoft.moneytransfer.accounting.models.MontantSchemaComptable;
// import com.loulysoft.moneytransfer.accounting.models.MontantSchemaRecord;
// import com.loulysoft.moneytransfer.accounting.models.Pays;
// import com.loulysoft.moneytransfer.accounting.models.SchemaComptable;
// import com.loulysoft.moneytransfer.accounting.models.TypeUniteOrganisational;
// import com.loulysoft.moneytransfer.accounting.models.UniteOrganisational;
// import com.loulysoft.moneytransfer.accounting.models.Users;
// import com.loulysoft.moneytransfer.accounting.repositories.EcritureSchemaComptableRepository;
// import com.loulysoft.moneytransfer.accounting.repositories.MontantParamSchemaComptableRepository;
// import com.loulysoft.moneytransfer.accounting.repositories.MontantSchemaComptableRepository;
// import com.loulysoft.moneytransfer.accounting.repositories.SchemaComptableRepository;
// import com.loulysoft.moneytransfer.accounting.services.AccountingSchemaService;
// import com.loulysoft.moneytransfer.accounting.services.CashInService;
// import com.loulysoft.moneytransfer.accounting.testdata.TestDataFactory;
// import com.loulysoft.moneytransfer.accounting.utils.CoreUtils;
// import java.util.List;
// import java.util.Optional;
// import java.util.Set;
// import org.junit.jupiter.api.Assertions;
// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.MethodOrderer;
// import org.junit.jupiter.api.Order;
// import org.junit.jupiter.api.Test;
// import org.junit.jupiter.api.TestMethodOrder;
// import org.junit.jupiter.api.extension.ExtendWith;
// import org.mapstruct.factory.Mappers;
// import org.mockito.Mock;
// import org.mockito.Spy;
// import org.mockito.junit.jupiter.MockitoExtension;
// import org.springframework.beans.factory.annotation.Autowired;
//
// @ExtendWith(MockitoExtension.class)
// @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
// public class AccountingSchemaServiceTest {
//
//    @Mock
//    private SchemaComptableRepository schemaRepository;
//
//    @Mock
//    private MontantSchemaComptableRepository amountSchemaRepository;
//
//    @Mock
//    private MontantParamSchemaComptableRepository amountParamSchemaRepository;
//
//    @Mock
//    private EcritureSchemaComptableRepository writerSchemaRepository;
//
//    @Mock
//    CoreUtils coreUtils;
//    //    private AccountingMapper accountingMapper;
//    @Spy
//    private AccountingMapper accountingMapper = Mappers.getMapper(AccountingMapper.class);
//
//    @Autowired
//    private AccountingSchemaService accountingSchemaService;
//
//    @Autowired
//    private CashInService cashInService;
//
//    private SchemaComptable schema;
//
//    private MontantSchemaComptable amountSchema;
//
//    private EcritureSchemaComptable writerSchema;
//
//    private UniteOrganisational company;
//
//    private Devise deviseCible;
//
//    private Pays paysDestination;
//
//    private Pays paysSource;
//
//    private List<EcritureSchemaComptable> writerSchemaList;
//
//    private List<MontantParamSchemaComptable> paramSchemaList;
//
//    private List<MontantSchemaComptable> montantSchemaList;
//
//    private MontantSchemaRecord ecritureSchemaSummary;
//    // private TypeService typeService;
//    private Users user;
//    private TypeUniteOrganisational typeUniteOrganisational;
//
//    //    @Autowired
//    //    private DevisesUtils devisesUtils;
//
//    //    @Autowired
//    //    private CoreUtils coreUtils;
//
//    private Set<MontantSchemaComptable> montantSchemas;
//
//    // https://testcontainers.com/guides/testing-spring-boot-rest-api-using-testcontainers/
//
//    @BeforeEach
//    public void setup() {
//        // coreUtils = new CoreUtils();
//        //        accountingSchemaService = new AccountingSchemaServiceImpl(
//        //                schemaRepository,
//        //                amountSchemaRepository,
//        //                amountParamSchemaRepository,
//        //                writerSchemaRepository,
//        //                accountingMapper,
//        //                devisesUtils,
//        //                coreUtils);
//        schema = TestDataFactory.getSchemaComptable();
//        amountSchema = TestDataFactory.getMontantSchemaComptable();
//        paramSchemaList = TestDataFactory.getListMontantParamSchemas();
//        montantSchemaList = TestDataFactory.getListMontantSchemas();
//        writerSchemaList = TestDataFactory.getListEcritureSchemas();
//        company = TestDataFactory.getUniteOrganisational();
//        writerSchema = TestDataFactory.getEcritureSchemaComptable();
//        montantSchemas = TestDataFactory.getMontantSchemas();
//        deviseCible = TestDataFactory.getDevise();
//        paysDestination = TestDataFactory.getPays();
//        ecritureSchemaSummary = new MontantSchemaRecord(1119L);
//        // typeService = TestDataFactory.getTypeService();
//        user = TestDataFactory.getUser();
//        typeUniteOrganisational = TestDataFactory.getTypeUniteOrganisational();
//        paysSource = TestDataFactory.getPays();
//        // coreUtils = new CoreUtils(context);
//        // ParametreRecherche createdParam = TestDataFactory.createParametreRecherche();
//        // when(schemaComptableRepository.readSchemaComptable(anyString(),anyString(),
//        // anyString(),anyString())).thenReturn(Optional.ofNullable(schemaComptable));
//    }
//
//    @Test
//    @Order(1)
//    public void readSchemaByServiceCodeAndTypeCodeAndVariantAndStatusTest() {
//        SchemaComptableEntity schemaEntity = accountingMapper.toSchemaEntity(schema);
//        // given
//        given(schemaRepository.findByServiceCodeAndTypeCompanyAndVariantAndStatusOrderByVersionDesc(
//                        anyString(), anyString(), any(), any()))
//                .willReturn(Optional.ofNullable(schemaEntity));
//        // action
//        SchemaComptable schema = accountingSchemaService.readSchemaByServiceCodeAndTypeCodeAndVariantAndStatus(
//                typeUniteOrganisational.getCode(), "CASH_TRANSFER", Variant.VARIANTE_1);
//        // verify the output
//        assertThat(schema).isNotNull();
//        Assertions.assertEquals(schema.getStatus(), schema.getStatus(), "The schema status should be A");
//    }
//
//    @Test
//    @Order(2)
//    public void readMontantSchemaComptableBySchemaComptableIdTest() {
//
//        List<MontantSchemaComptableEntity> listMontantSchemas =
//                accountingMapper.toMontantSchemaEntities(montantSchemaList);
//        // precondition
//        given(amountSchemaRepository.findBySchemaIdOrderByRangAsc(any())).willReturn(listMontantSchemas);
//        // action
//        List<MontantSchemaComptable> montantSchemasList =
//                accountingSchemaService.readMontantSchemasBySchemaId(schema.getId());
//
//        // verify the output
//        assertThat(montantSchemasList).isNotNull();
//        Assertions.assertEquals(
//                montantSchemasList.getFirst().getNom(),
//                montantSchemaList.getFirst().getNom(),
//                "The schema status should be 'F'");
//    }
//
//    @Test
//    @Order(3)
//    public void readMontantParamSchemaByMontantSchemaIdAndParametreRechercheTypeTest() {
//        List<MontantParamSchemaComptableEntity> montantParamSchemaList =
//                accountingMapper.toMontantParamSchemaEntities(paramSchemaList);
//        // given
//        given(amountParamSchemaRepository.findByMontantSchemaIdAndSearchType(any(), any()))
//                .willReturn(montantParamSchemaList);
//        // action
//        List<MontantParamSchemaComptable> montantParamSchemas =
//                accountingSchemaService.readMontantParamSchemaByMontantSchemaIdAndParametreRechercheType(
//                        amountSchema.getId());
//        // verify the output
//        assertThat(montantParamSchemas).isNotNull();
//        Assertions.assertEquals(1, montantParamSchemas.size(), "Wrong size");
//        Assertions.assertEquals(
//                montantParamSchemas.getFirst().getId(),
//                paramSchemaList.getFirst().getId(),
//                "The ID should be 1000");
//    }
//
//    @Test
//    @Order(4)
//    public void readEcritureSchemaBySchemaIdAndCodeEcritureCodeInTest() {
//
//        List<EcritureSchemaComptableEntity> listEcritureSchema =
//                accountingMapper.toEcritureSchemaEntities(writerSchemaList);
//        // given
//        given(
//                        writerSchemaRepository
//
// .findBySchemaIdAndAccountSearchPivotInAndAccountSearchTypeAndAccountSearchNiveauAndWriterCodeIn(
//                                        any(), any(), any(), any(), any()))
//                .willReturn(listEcritureSchema);
//        // action
//        List<EcritureSchemaComptable> ecritureSchema =
//                accountingSchemaService.readEcritureSchemaBySchemaIdAndCodeEcritureCodeIn(
//                        typeUniteOrganisational.getCode(), schema.getId());
//        // verify the output
//        assertThat(ecritureSchema).isNotNull();
//        Assertions.assertEquals(
//                listEcritureSchema.getFirst().getWriter().getCode(),
//                ecritureSchema.getFirst().getWriter().getCode());
//    }
//
//    @Test
//    @Order(5)
//    public void readEcritureSchemaBySchemaIdAndWriterCodeAndDirectionTest() {
//
//        EcritureSchemaComptableEntity ecritureSchemaEntity = accountingMapper.toEcritureSchemaEntity(writerSchema);
//        // given
//        given(writerSchemaRepository.findBySchemaIdAndWriterCodeAndDirection(any(), any(), any()))
//                .willReturn(Optional.ofNullable(ecritureSchemaEntity));
//        // action
//        EcritureSchemaComptable ecritureSchema =
//                accountingSchemaService.readEcritureSchemaBySchemaIdAndWriterCodeAndDirection(schema.getId());
//        // verify the output
//        assertThat(ecritureSchema).isNotNull();
//        Assertions.assertEquals(ecritureSchema.getDirection(), writerSchema.getDirection());
//    }
//
//    @Test
//    @Order(6)
//    public void readBySchemaIdAndWriterCodeAndDirectionTest() {
//
//        // given
//        given(writerSchemaRepository.findMontantSchemaIdBySchemaIdAndCodeAndDirection(any(), any(), any()))
//                .willReturn(Optional.ofNullable(ecritureSchemaSummary));
//        // action
//        MontantSchemaRecord ecritureSchema =
//                accountingSchemaService.readMontantSchemaIdInEcritureBySchemaIdAndWriterCodeAndDirection(
//                        schema.getId());
//        // verify the output
//        assertThat(ecritureSchema).isNotNull();
//        // Assertions.assertEquals(ecritureSchema.montantSchemaComptableId(),
//        // ecritureSchemaSummary.montantSchemaComptableId());
//    }
//
//    //    @Test
//    //    @Order(7)
//    //    public void calculatePrincipalAndCommissionsByReceivedAmountTest() {
//    //
//    //        //        when(accountingSchemaService.demarrerTransaction(any(), anyString(), any()))
//    //        //                .thenReturn(report);
//    //
//    //        String natureMontant = "PRINCIPAL_CONVERTI";
//    //        BigDecimal montant = new BigDecimal(50000);
//    //        TransactionContext transactionContext = new TransactionContext();
//    //        transactionContext.addContextItem(TransactionContextItem.DEVISE, deviseCible);
//    //        transactionContext.addContextItem(TransactionContextItem.PRINCIPAL_CONVERTI, montant);
//    //        transactionContext.addContextItem(TransactionContextItem.DESTINATION_COUNTRY, paysDestination);
//    //        // typeService.setComposant("com.loulysoft.moneytransfer.accounting.runtime.RuntimeCashTransfer");
//    //
//    //        TransactionReport report = cashInService.calculerDetailsMontant(
//    //                user.getId(),
//    //                company.getId(),
//    //                // paysSource.getCode(),
//    //                paysDestination.getCode(),
//    //                "CASH_TRANSFER",
//    //                natureMontant,
//    //                montant);
//    //
//    //        assertThat(report).isNotNull();
//    //    }
//    //
//    //    @Test
//    //    @Order(7)
//    //    public void calculatePrincipalAndCommissionsByTotalTest() {
//    //
//    //        //        when(accountingSchemaService.demarrerTransaction(any(), anyString(), any()))
//    //        //                .thenReturn(report);
//    //
//    //        String natureMontant = "PRINCIPAL_FEE";
//    //        BigDecimal montant = new BigDecimal(5000);
//    //        TransactionContext transactionContext = new TransactionContext();
//    //        transactionContext.addContextItem(TransactionContextItem.DEVISE, deviseCible);
//    //        transactionContext.addContextItem(TransactionContextItem.PRINCIPAL_FEE, montant);
//    //        transactionContext.addContextItem(TransactionContextItem.DESTINATION_COUNTRY, paysDestination);
//    //        // typeService.setComposant("com.loulysoft.moneytransfer.accounting.runtime.RuntimeCashTransfer");
//    //
//    //        TransactionReport report = cashInService.calculerDetailsMontant(
//    //                user.getId(),
//    //                company.getId(),
//    //                // paysSource.getCode(),
//    //                paysDestination.getCode(),
//    //                "CASH_TRANSFER",
//    //                natureMontant,
//    //                montant);
//    //
//    //        assertThat(report).isNotNull();
//    //    }
// }
