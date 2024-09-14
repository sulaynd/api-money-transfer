// package com.loulysoft.moneytransfer.ratings.unit;
//
// import static org.assertj.core.api.Assertions.assertThat;
// import static org.mockito.ArgumentMatchers.any;
// import static org.mockito.ArgumentMatchers.anyString;
// import static org.mockito.BDDMockito.given;
//
// import com.loulysoft.moneytransfer.ratings.entities.EcritureSchemaComptableEntity;
// import com.loulysoft.moneytransfer.ratings.entities.MontantParamSchemaComptableEntity;
// import com.loulysoft.moneytransfer.ratings.mappers.AccountingMapper;
// import com.loulysoft.moneytransfer.ratings.models.EcritureSchemaComptable;
// import com.loulysoft.moneytransfer.ratings.models.MontantParamSchemaComptable;
// import com.loulysoft.moneytransfer.ratings.models.MontantSchemaComptable;
// import com.loulysoft.moneytransfer.ratings.models.SchemaComptable;
// import com.loulysoft.moneytransfer.ratings.repositories.EcritureSchemaComptableRepository;
// import com.loulysoft.moneytransfer.ratings.repositories.MontantParamSchemaComptableRepository;
// import com.loulysoft.moneytransfer.ratings.repositories.MontantSchemaComptableRepository;
// import com.loulysoft.moneytransfer.ratings.repositories.SchemaComptableRepository;
// import com.loulysoft.moneytransfer.ratings.services.AccountingSchemaService;
// import com.loulysoft.moneytransfer.ratings.services.impl.AccountingSchemaServiceImpl;
// import com.loulysoft.moneytransfer.ratings.testdata.TestDataFactory;
// import com.loulysoft.moneytransfer.ratings.utils.Code;
// import com.loulysoft.moneytransfer.ratings.utils.Pivot;
// import com.loulysoft.moneytransfer.ratings.utils.Type;
// import java.util.ArrayList;
// import java.util.Arrays;
// import java.util.List;
// import java.util.Optional;
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
// import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//
// @WebMvcTest(AccountingSchemaServiceImpl.class)
// @ExtendWith(MockitoExtension.class)
// @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
// public class AccountingSchemaServiceTest {
//
//    @Mock
//    private SchemaComptableRepository schemaRepository;
//
//    @Mock
//    private MontantSchemaComptableRepository montantSchemaRepository;
//
//    @Mock
//    private MontantParamSchemaComptableRepository montantParamSchemaRepository;
//
//    @Mock
//    private EcritureSchemaComptableRepository ecritureSchemaComptableRepository;
//    //    private AccountingMapper accountingMapper;
//    @Spy
//    private AccountingMapper accountingMapper = Mappers.getMapper(AccountingMapper.class);
//
//    private AccountingSchemaService accountingSchemaService;
//
//    private SchemaComptable createdSchema;
//
//    private MontantSchemaComptable createdMontantSchema;
//
//    private List<EcritureSchemaComptable> ecritureSchemaList;
//
//    private List<MontantParamSchemaComptable> paramSchemaList;
//
//    // https://testcontainers.com/guides/testing-spring-boot-rest-api-using-testcontainers/
//
//    @BeforeEach
//    public void setup() {
//        accountingSchemaService = new AccountingSchemaServiceImpl(
//                schemaRepository,
//                montantSchemaRepository,
//                montantParamSchemaRepository,
//                ecritureSchemaComptableRepository,
//                accountingMapper);
//        createdSchema = TestDataFactory.createSchema();
//        createdMontantSchema = TestDataFactory.createMontantSchema();
//        paramSchemaList = TestDataFactory.getListMontantParamSchemas();
//        ecritureSchemaList = TestDataFactory.getListEcritureSchemas();
//        // ParametreRecherche createdParam = TestDataFactory.createParametreRecherche();
//        // when(schemaComptableRepository.readSchemaComptable(anyString(),anyString(),
//        // anyString(),anyString())).thenReturn(Optional.ofNullable(schemaComptable));
//    }
//
//    @Test
//    @Order(1)
//    public void readSchemaByServiceCodeAndTypeCodeAndVariantAndStatusTest() {
//        // precondition
//        given(schemaRepository.findByServiceCodeAndTypeCodeAndVariantAndStatus(
//                        anyString(), anyString(), anyString(), anyString()))
//                .willReturn(Optional.ofNullable(createdSchema));
//        // action
//        SchemaComptable schema = accountingSchemaService.readSchemaByServiceCodeAndTypeCodeAndVariantAndStatus(
//                "CASH_TRANSFER", "DISTRIBUTEUR", "A", "VARIANTE_1");
//        // verify the output
//        assertThat(schema).isNotNull();
//        Assertions.assertEquals(schema.getStatus(), createdSchema.getStatus(), "The schema status should be A");
//    }
//
//    @Test
//    @Order(2)
//    public void readMontantSchemaComptableBySchemaComptableIdTest() {
//        // precondition
//        given(montantSchemaRepository.findBySchemaId(any())).willReturn(Optional.ofNullable(createdMontantSchema));
//        // action
//        MontantSchemaComptable montantSchema =
//                accountingSchemaService.readMontantSchemaBySchemaComptableId(createdSchema.getId());
//        // verify the output
//        assertThat(montantSchema).isNotNull();
//        Assertions.assertEquals(
//                montantSchema.getNom(), createdMontantSchema.getNom(), "The schema status should be 'F'");
//    }
//
//    @Test
//    @Order(3)
//    public void readMontantParamSchemaByMontantSchemaIdAndParametreRechercheTypeTest() {
//        List<MontantParamSchemaComptableEntity> montantParamSchemaList =
//                accountingMapper.asMontantParamSchemaEntities(paramSchemaList);
//        // precondition
//        given(montantParamSchemaRepository.findByMontantSchemaIdAndParametreRechercheType(any(), any()))
//                .willReturn(montantParamSchemaList);
//        // action
//        List<MontantParamSchemaComptable> montantParamSchemas =
//                accountingSchemaService.readMontantParamSchemaByMontantSchemaIdAndParametreRechercheType(
//                        createdMontantSchema.getId(), Type.UNITE_ORGANISATIONELLE);
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
//        List<Code> codes = new ArrayList<>();
//        codes.add(Code.PRINCIPAL);
//        codes.add(Code.FRAIS);
//        codes.add(Code.TIMBRE);
//        codes.add(Code.TAXES);
//        List<Pivot> pivots =
//                Arrays.asList(Pivot.UNITE_ORGANISATIONNELLE_EMETTRICE, Pivot.UNITE_ORGANISATIONNELLE_TIERCE);
//
//        List<EcritureSchemaComptableEntity> listEcritureSchema =
//                accountingMapper.asEcritureSchemaComptableEntities(ecritureSchemaList);
//        // precondition
//        given(
//                        ecritureSchemaComptableRepository
//
// .findBySchemaIdAndAccountSearchPivotInAndAccountSearchTypeAndAccountSearchNiveauAndWriterCodeIn(
//                                        any(), any(), any(), any(), any()))
//                .willReturn(listEcritureSchema);
//        // action
//        List<EcritureSchemaComptable> ecritureSchema =
//                accountingSchemaService.readEcritureSchemaBySchemaIdAndCodeEcritureCodeIn(
//                        createdSchema.getId(), pivots, Type.UNITE_ORGANISATIONELLE, 0, codes);
//        // verify the output
//        assertThat(ecritureSchema).isNotNull();
//        Assertions.assertEquals(
//                listEcritureSchema.getFirst().getWriter().getCode(),
//                ecritureSchema.getFirst().getWriter().getCode());
//    }
// }
