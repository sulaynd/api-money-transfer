package com.loulysoft.moneytransfer.ratings.unit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

import com.loulysoft.moneytransfer.ratings.entities.EcritureSchemaComptableEntity;
import com.loulysoft.moneytransfer.ratings.entities.MontantParamSchemaComptableEntity;
import com.loulysoft.moneytransfer.ratings.entities.MontantSchemaComptableEntity;
import com.loulysoft.moneytransfer.ratings.entities.SchemaComptableEntity;
import com.loulysoft.moneytransfer.ratings.mappers.AccountingMapper;
import com.loulysoft.moneytransfer.ratings.models.EcritureSchemaComptable;
import com.loulysoft.moneytransfer.ratings.models.MontantParamSchemaComptable;
import com.loulysoft.moneytransfer.ratings.models.MontantSchemaComptable;
import com.loulysoft.moneytransfer.ratings.models.SchemaComptable;
import com.loulysoft.moneytransfer.ratings.models.Users;
import com.loulysoft.moneytransfer.ratings.repositories.EcritureSchemaComptableRepository;
import com.loulysoft.moneytransfer.ratings.repositories.MontantParamSchemaComptableRepository;
import com.loulysoft.moneytransfer.ratings.repositories.MontantSchemaComptableRepository;
import com.loulysoft.moneytransfer.ratings.repositories.SchemaComptableRepository;
import com.loulysoft.moneytransfer.ratings.services.AccountingSchemaService;
import com.loulysoft.moneytransfer.ratings.services.impl.AccountingSchemaServiceImpl;
import com.loulysoft.moneytransfer.ratings.testdata.TestDataFactory;
import com.loulysoft.moneytransfer.ratings.utils.Code;
import com.loulysoft.moneytransfer.ratings.utils.Pivot;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AccountingSchemaServiceTest {

    @Mock
    private SchemaComptableRepository schemaRepository;

    @Mock
    private MontantSchemaComptableRepository montantSchemaRepository;

    @Mock
    private MontantParamSchemaComptableRepository montantParamSchemaRepository;

    @Mock
    private EcritureSchemaComptableRepository ecritureSchemaComptableRepository;
    //    private AccountingMapper accountingMapper;
    @Spy
    private AccountingMapper accountingMapper = Mappers.getMapper(AccountingMapper.class);

    private AccountingSchemaService accountingSchemaService;

    private SchemaComptable createdSchema;

    private MontantSchemaComptable createdMontantSchema;

    private Users user;

    private List<EcritureSchemaComptable> ecritureSchemaList;

    private List<MontantParamSchemaComptable> paramSchemaList;

    // https://testcontainers.com/guides/testing-spring-boot-rest-api-using-testcontainers/

    @BeforeEach
    public void setup() {
        accountingSchemaService = new AccountingSchemaServiceImpl(
                schemaRepository,
                montantSchemaRepository,
                montantParamSchemaRepository,
                ecritureSchemaComptableRepository,
                accountingMapper);
        createdSchema = TestDataFactory.createSchema();
        createdMontantSchema = TestDataFactory.createMontantSchema();
        paramSchemaList = TestDataFactory.getListMontantParamSchemas();
        ecritureSchemaList = TestDataFactory.getListEcritureSchemas();
        user = TestDataFactory.createUser();
        // ParametreRecherche createdParam = TestDataFactory.createParametreRecherche();
        // when(schemaComptableRepository.readSchemaComptable(anyString(),anyString(),
        // anyString(),anyString())).thenReturn(Optional.ofNullable(schemaComptable));
    }

    @Test
    @Order(1)
    public void readSchemaByServiceCodeAndTypeCodeAndVariantAndStatusTest() {
        SchemaComptableEntity schemaEntity = accountingMapper.toSchemaComptableEntity(createdSchema);
        // precondition
        given(schemaRepository.findByServiceCodeAndTypeCodeAndVariantAndStatusOrderByVersionDesc(
                        anyString(), anyString(), any(), any()))
                .willReturn(Optional.ofNullable(schemaEntity));
        // action
        SchemaComptable schema =
                accountingSchemaService.readSchemaByServiceCodeAndTypeCodeAndVariantAndStatus(user, "CASH_TRANSFER");
        // verify the output
        assertThat(schema).isNotNull();
        Assertions.assertEquals(schema.getStatus(), createdSchema.getStatus(), "The schema status should be A");
    }

    @Test
    @Order(2)
    public void readMontantSchemaComptableBySchemaComptableIdTest() {
        MontantSchemaComptableEntity montantSchemaEntity =
                accountingMapper.toMontantSchemaComptableEntity(createdMontantSchema);
        // precondition
        given(montantSchemaRepository.findBySchemaIdOrderByRangAsc(any()))
                .willReturn(Optional.ofNullable(montantSchemaEntity));
        // action
        MontantSchemaComptable montantSchema =
                accountingSchemaService.readMontantSchemaBySchemaId(createdSchema.getId());
        // verify the output
        assertThat(montantSchema).isNotNull();
        Assertions.assertEquals(
                montantSchema.getNom(), createdMontantSchema.getNom(), "The schema status should be 'F'");
    }

    @Test
    @Order(3)
    public void readMontantParamSchemaByMontantSchemaIdAndParametreRechercheTypeTest() {
        List<MontantParamSchemaComptableEntity> montantParamSchemaList =
                accountingMapper.asMontantParamSchemaEntities(paramSchemaList);
        // precondition
        given(montantParamSchemaRepository.findByMontantSchemaIdAndSearchType(any(), any()))
                .willReturn(montantParamSchemaList);
        // action
        List<MontantParamSchemaComptable> montantParamSchemas =
                accountingSchemaService.readMontantParamSchemaByMontantSchemaIdAndParametreRechercheType(
                        createdMontantSchema.getId());
        // verify the output
        assertThat(montantParamSchemas).isNotNull();
        Assertions.assertEquals(1, montantParamSchemas.size(), "Wrong size");
        Assertions.assertEquals(
                montantParamSchemas.getFirst().getId(),
                paramSchemaList.getFirst().getId(),
                "The ID should be 1000");
    }

    @Test
    @Order(4)
    public void readEcritureSchemaBySchemaIdAndCodeEcritureCodeInTest() {
        List<Code> codes = new ArrayList<>();
        codes.add(Code.PRINCIPAL);
        codes.add(Code.FRAIS);
        codes.add(Code.TIMBRE);
        codes.add(Code.TAXES);
        List<Pivot> pivots =
                Arrays.asList(Pivot.UNITE_ORGANISATIONNELLE_EMETTRICE, Pivot.UNITE_ORGANISATIONNELLE_TIERCE);

        List<EcritureSchemaComptableEntity> listEcritureSchema =
                accountingMapper.asEcritureSchemaComptableEntities(ecritureSchemaList);
        // precondition
        given(
                        ecritureSchemaComptableRepository
                                .findBySchemaIdAndAccountSearchPivotInAndAccountSearchTypeAndAccountSearchNiveauAndWriterCodeIn(
                                        any(), any(), any(), any(), any()))
                .willReturn(listEcritureSchema);
        // action
        List<EcritureSchemaComptable> ecritureSchema =
                accountingSchemaService.readEcritureSchemaBySchemaIdAndCodeEcritureCodeIn(user, createdSchema.getId());
        // verify the output
        assertThat(ecritureSchema).isNotNull();
        Assertions.assertEquals(
                listEcritureSchema.getFirst().getWriter().getCode(),
                ecritureSchema.getFirst().getWriter().getCode());
    }
}
