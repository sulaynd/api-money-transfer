package com.loulysoft.moneytransfer.ratings.services.impl;

import com.loulysoft.moneytransfer.ratings.exceptions.ResourceNotFoundException;
import com.loulysoft.moneytransfer.ratings.mappers.AccountingMapper;
import com.loulysoft.moneytransfer.ratings.models.EcritureSchemaComptable;
import com.loulysoft.moneytransfer.ratings.models.MontantParamSchemaComptable;
import com.loulysoft.moneytransfer.ratings.models.MontantSchemaComptable;
import com.loulysoft.moneytransfer.ratings.models.SchemaComptable;
import com.loulysoft.moneytransfer.ratings.repositories.EcritureSchemaComptableRepository;
import com.loulysoft.moneytransfer.ratings.repositories.MontantParamSchemaComptableRepository;
import com.loulysoft.moneytransfer.ratings.repositories.MontantSchemaComptableRepository;
import com.loulysoft.moneytransfer.ratings.repositories.SchemaComptableRepository;
import com.loulysoft.moneytransfer.ratings.services.AccountingSchemaService;
import com.loulysoft.moneytransfer.ratings.utils.Code;
import com.loulysoft.moneytransfer.ratings.utils.Pivot;
import com.loulysoft.moneytransfer.ratings.utils.Type;
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
    public SchemaComptable readSchemaByServiceCodeAndTypeCodeAndVariantAndStatus(
            String serviceCode, String type, String variant, String status) {
        return schemaRepository
                .findByServiceCodeAndTypeCodeAndVariantAndStatus(serviceCode, type, variant, status)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Account Schema with service " + serviceCode + " not found"));
    }

    @Override
    public MontantSchemaComptable readMontantSchemaBySchemaComptableId(Long schemaComptableId) {
        return montantSchemaRepository
                .findBySchemaId(schemaComptableId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Amount accounting Schema with schema Id " + schemaComptableId + " not found"));
    }

    @Override
    public List<MontantParamSchemaComptable> readMontantParamSchemaByMontantSchemaIdAndParametreRechercheType(
            Long montantSchemaId, Type type) {
        return accountingMapper.convertToMontantParamSchema(
                montantParamSchemaRepository.findByMontantSchemaIdAndParametreRechercheType(montantSchemaId, type));
    }

    @Override
    public List<EcritureSchemaComptable> readEcritureSchemaBySchemaIdAndCodeEcritureCodeIn(
            Long schemaId, List<Pivot> pivots, Type type, Integer niveau, List<Code> codes) {
        return accountingMapper.convertToEcritureSchemaComptable(
                ecritureSchemaComptableRepository
                        .findBySchemaIdAndAccountSearchPivotInAndAccountSearchTypeAndAccountSearchNiveauAndWriterCodeIn(
                                schemaId, pivots, type, niveau, codes));
    }
}
