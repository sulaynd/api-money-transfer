package com.loulysoft.moneytransfer.ratings.services.impl;

import com.loulysoft.moneytransfer.ratings.exceptions.ResourceNotFoundException;
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
import com.loulysoft.moneytransfer.ratings.utils.Code;
import com.loulysoft.moneytransfer.ratings.utils.Pivot;
import com.loulysoft.moneytransfer.ratings.utils.Type;
import com.loulysoft.moneytransfer.ratings.utils.TypeUnit;
import com.loulysoft.moneytransfer.ratings.utils.Variant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
public class AccountingSchemaServiceImpl implements AccountingSchemaService {

    private final SchemaComptableRepository schemaRepository;

    private final MontantSchemaComptableRepository montantSchemaRepository;

    private final MontantParamSchemaComptableRepository montantParamSchemaRepository;

    private final EcritureSchemaComptableRepository ecritureSchemaComptableRepository;

    private final AccountingMapper accountingMapper;

    public AccountingSchemaServiceImpl(
            SchemaComptableRepository schemaRepository,
            MontantSchemaComptableRepository montantSchemaRepository,
            MontantParamSchemaComptableRepository montantParamSchemaRepository,
            EcritureSchemaComptableRepository ecritureSchemaComptableRepository,
            AccountingMapper accountingMapper) {
        this.schemaRepository = schemaRepository;
        this.montantSchemaRepository = montantSchemaRepository;
        this.montantParamSchemaRepository = montantParamSchemaRepository;
        this.ecritureSchemaComptableRepository = ecritureSchemaComptableRepository;
        this.accountingMapper = accountingMapper;
    }

    @Override
    public SchemaComptable readSchemaByServiceCodeAndTypeCodeAndVariantAndStatus(Users user, String serviceCode) {

        String type = user.getUniteOrganisational().getType().getCode();
        Variant variant = Variant.VARIANTE_1;
        Character status = 'A';

        return accountingMapper.toSchemaComptable(schemaRepository
                .findByServiceCodeAndTypeCodeAndVariantAndStatusOrderByVersionDesc(serviceCode, type, variant, status)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Account Schema with service " + serviceCode + " not found")));
    }

    @Override
    public MontantSchemaComptable readMontantSchemaBySchemaId(Long schemaId) {
        return accountingMapper.toMontantSchemaComptable(montantSchemaRepository
                .findBySchemaIdOrderByRangAsc(schemaId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Amount accounting Schema with schema Id " + schemaId + " not found")));
    }

    @Override
    public List<MontantParamSchemaComptable> readMontantParamSchemaByMontantSchemaIdAndParametreRechercheType(
            Long montantSchemaId) {
        Type type = Type.DEVISE;
        return accountingMapper.convertToMontantParamSchema(
                montantParamSchemaRepository.findByMontantSchemaIdAndSearchType(montantSchemaId, type));
    }

    @Override
    public List<EcritureSchemaComptable> readEcritureSchemaBySchemaIdAndCodeEcritureCodeIn(Users user, Long schemaId) {

        List<Pivot> pivots =
                Arrays.asList(Pivot.UNITE_ORGANISATIONNELLE_EMETTRICE, Pivot.UNITE_ORGANISATIONNELLE_TIERCE);
        List<Code> codes = new ArrayList<>();
        codes.add(Code.PRINCIPAL);
        codes.add(Code.FRAIS);
        codes.add(Code.TIMBRE);
        codes.add(Code.TAXES);
        Type type = Type.UNITE_ORGANISATIONELLE;
        int niveau = 0;
        if (user.getUniteOrganisational().getType().getCode().equals(TypeUnit.AGENCE_BANQUE.name())) {
            niveau = 1;
        }

        return accountingMapper.convertToEcritureSchemaComptable(
                ecritureSchemaComptableRepository
                        .findBySchemaIdAndAccountSearchPivotInAndAccountSearchTypeAndAccountSearchNiveauAndWriterCodeIn(
                                schemaId, pivots, type, niveau, codes));
    }
}
