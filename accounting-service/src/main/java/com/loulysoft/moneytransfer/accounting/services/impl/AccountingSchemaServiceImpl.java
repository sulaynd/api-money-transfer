package com.loulysoft.moneytransfer.accounting.services.impl;

import com.loulysoft.moneytransfer.accounting.enums.Code;
import com.loulysoft.moneytransfer.accounting.enums.DebitCredit;
import com.loulysoft.moneytransfer.accounting.enums.Pivot;
import com.loulysoft.moneytransfer.accounting.enums.ServiceContextItem;
import com.loulysoft.moneytransfer.accounting.enums.TransactionContextItem;
import com.loulysoft.moneytransfer.accounting.enums.Type;
import com.loulysoft.moneytransfer.accounting.enums.UniteOrganisationalType;
import com.loulysoft.moneytransfer.accounting.enums.Variant;
import com.loulysoft.moneytransfer.accounting.exceptions.NotFoundException;
import com.loulysoft.moneytransfer.accounting.exceptions.ResourceNotFoundException;
import com.loulysoft.moneytransfer.accounting.mappers.AccountingMapper;
import com.loulysoft.moneytransfer.accounting.mappers.CompanyMapper;
import com.loulysoft.moneytransfer.accounting.models.EcritureSchemaComptable;
import com.loulysoft.moneytransfer.accounting.models.Journal;
import com.loulysoft.moneytransfer.accounting.models.MontantParamSchemaComptable;
import com.loulysoft.moneytransfer.accounting.models.MontantSchemaComptable;
import com.loulysoft.moneytransfer.accounting.models.MontantSchemaRecord;
import com.loulysoft.moneytransfer.accounting.models.Pays;
import com.loulysoft.moneytransfer.accounting.models.SchemaComptable;
import com.loulysoft.moneytransfer.accounting.models.ServiceContext;
import com.loulysoft.moneytransfer.accounting.models.TransactionContext;
import com.loulysoft.moneytransfer.accounting.models.TransactionReport;
import com.loulysoft.moneytransfer.accounting.models.UniteOrganisational;
import com.loulysoft.moneytransfer.accounting.repositories.EcritureSchemaComptableRepository;
import com.loulysoft.moneytransfer.accounting.repositories.MontantParamSchemaComptableRepository;
import com.loulysoft.moneytransfer.accounting.repositories.MontantSchemaComptableRepository;
import com.loulysoft.moneytransfer.accounting.repositories.SchemaComptableRepository;
import com.loulysoft.moneytransfer.accounting.repositories.UniteOrganisationalRepository;
import com.loulysoft.moneytransfer.accounting.runtime.AbstractRuntimeService;
import com.loulysoft.moneytransfer.accounting.services.AccountingSchemaService;
import com.loulysoft.moneytransfer.accounting.services.DeviseService;
import com.loulysoft.moneytransfer.accounting.utils.CoreUtils;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = false)
public class AccountingSchemaServiceImpl implements AccountingSchemaService {

    private final SchemaComptableRepository schemaRepository;

    private final UniteOrganisationalRepository uniteOrganisationalRepository;

    private final MontantSchemaComptableRepository montantSchemaComptableRepository;

    private final MontantParamSchemaComptableRepository montantParamSchemaComptableRepository;

    private final EcritureSchemaComptableRepository ecritureSchemaComptableRepository;

    private final AccountingMapper accountingMapper;

    private final DeviseService deviseService;

    private final CompanyMapper companyMapper;

    private final CoreUtils coreUtils;

    @Override
    @Transactional(readOnly = true)
    public SchemaComptable readSchemaByServiceCodeAndTypeCodeAndVariantAndStatus(
            String type, String serviceCode, Variant variant) {

        // String type = company.getType().getCode();
        // Variant variant = Variant.VARIANTE_1;
        Character status = 'A';

        return accountingMapper.toSchema(schemaRepository
                // .findByServiceCodeAndTypeCompanyAndVariantAndStatusOrderByVersionDesc
                .findSchemaByServiceCodeAndTypeCodeAndVariantAndStatusOrderByVersionDesc(
                        serviceCode, type, variant, status)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Account Schema with service " + serviceCode + " and company type " + type + " not found")));
    }

    @Override
    @Transactional(readOnly = true)
    public List<MontantSchemaComptable> readMontantSchemasBySchemaId(Long schemaId) {
        return accountingMapper.convertToMontantSchema(
                montantSchemaComptableRepository.findBySchemaIdOrderByRangAsc(schemaId));
    }

    @Override
    @Transactional(readOnly = true)
    public List<MontantParamSchemaComptable> readMontantParamSchemaByMontantSchemaIdAndParametreRechercheType(
            Long montantSchemaId) {

        return accountingMapper.convertToMontantParamSchema(
                montantParamSchemaComptableRepository.findByMontantSchemaIdAndSearchType(montantSchemaId, Type.DEVISE));
    }

    @Override
    @Transactional(readOnly = true)
    public List<EcritureSchemaComptable> readEcritureSchemaBySchemaIdAndCodeEcritureCodeIn(
            String companyTypeCode, Long schemaId) {

        List<Pivot> pivots =
                Arrays.asList(Pivot.UNITE_ORGANISATIONNELLE_EMETTRICE, Pivot.UNITE_ORGANISATIONNELLE_TIERCE);
        List<Code> codes = new ArrayList<>();
        codes.add(Code.PRINCIPAL);
        codes.add(Code.FRAIS);
        codes.add(Code.TIMBRE);
        codes.add(Code.TAXES);
        Type type = Type.UNITE_ORGANISATIONELLE;
        int niveau = 0;
        if (companyTypeCode.equals(UniteOrganisationalType.AGENCE_BANQUE.name())) {
            niveau = 1;
        }

        return accountingMapper.convertToEcritureSchema(
                ecritureSchemaComptableRepository
                        .findBySchemaIdAndAccountSearchPivotInAndAccountSearchTypeAndAccountSearchNiveauAndWriterCodeIn(
                                schemaId, pivots, type, niveau, codes));
    }

    @Override
    @Transactional(readOnly = true)
    public EcritureSchemaComptable readEcritureSchemaBySchemaIdAndWriterCodeAndDirection(Long schemaId) {
        var code = Code.ATTENTE_TRANSFERT;
        var credit = DebitCredit.CREDIT;
        return accountingMapper.toEcritureSchema(ecritureSchemaComptableRepository
                .findBySchemaIdAndWriterCodeAndDirection(schemaId, code, credit)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Writer schema with schema Id " + schemaId + " and direction " + credit + " not found")));
    }

    @Override
    @Transactional(readOnly = true)
    public MontantSchemaRecord readMontantSchemaIdInEcritureBySchemaIdAndWriterCodeAndDirection(Long schemaId) {
        var code = Code.ATTENTE_TRANSFERT;
        var credit = DebitCredit.CREDIT;
        return ecritureSchemaComptableRepository
                .findMontantSchemaIdBySchemaIdAndCodeAndDirection(schemaId, code, credit)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Writer schema with schema Id " + schemaId + " and direction " + credit + " not found"));
    }

    @Override
    public TransactionReport demarrerTransaction(
            Long userId, Long companyId, Pays paysPayer, String serviceCode, TransactionContext transactionContext) {
        UniteOrganisational company = companyMapper.toUniteOrganisational(uniteOrganisationalRepository
                .findById(companyId)
                .orElseThrow(() -> new ResourceNotFoundException("Company with id " + companyId + " not found")));

        String companyTypeCode = company.getType().getCode();
        AbstractRuntimeService<Journal> runtimeService = coreUtils.getRuntimeService(serviceCode);
        ServiceContext serviceContext = runtimeService.getServiceContext(
                company.getId(), company.getRoot().getId(), companyTypeCode, serviceCode, transactionContext);
        Variant variant = Variant.VARIANTE_1;
        String autreParametre = null;
        Long initialTransactionId = null;
        String natureServiceCode = null;
        // InfoTransfert infoSend = null;
        var report = new TransactionReport();
        HashMap<Long, BigDecimal> map = new HashMap<>();
        Pays paysSource = (Pays) transactionContext.getContextItemValue(TransactionContextItem.SENDER_COUNTRY);
        transactionContext.addContextItem(
                TransactionContextItem.DEVISE,
                paysSource.getZoneMonetaire().getDevise().getCode());
        if (serviceContext != null) {
            natureServiceCode =
                    (String) serviceContext.getServiceContext().get(ServiceContextItem.NATURE_SERVICE.name());
            log.info("getServiceContext:nature-service:" + natureServiceCode);
            transactionContext.addContextItem(TransactionContextItem.SERVICE_CONTEXT, serviceContext);
            transactionContext.addContextItem(TransactionContextItem.NATURE_SERVICE, natureServiceCode);
            if (serviceContext.getServiceContext().containsKey(ServiceContextItem.ACCOUNTING_SCHEMA_VARIANTE.name())) {
                variant = (Variant)
                        serviceContext.getServiceContext().get(ServiceContextItem.ACCOUNTING_SCHEMA_VARIANTE.name());
            }
        }

        var deviseSource = deviseService.readDeviseByCode(
                paysSource.getZoneMonetaire().getDevise().getCode());
        // company.getPays().getDevise();
        if (deviseSource == null) {
            throw new NotFoundException("Currency source with code "
                    + paysSource.getZoneMonetaire().getDevise().getCode() + " not found.");
        }

        var deviseCible = deviseService.readDeviseByCode(
                paysPayer.getZoneMonetaire().getDevise().getCode());
        // company.getPays().getDevise();
        if (deviseCible == null) {
            throw new NotFoundException("Currency target with code "
                    + paysPayer.getZoneMonetaire().getDevise().getCode() + " not found.");
        }

        var schema = readSchemaByServiceCodeAndTypeCodeAndVariantAndStatus(companyTypeCode, serviceCode, variant);

        List<MontantSchemaComptable> montantSchemaComptables = readMontantSchemasBySchemaId(schema.getId());
        if (montantSchemaComptables.isEmpty()) {
            throw new NotFoundException("Undefined accounting schema amount for schema :" + schema.getId());
        }

        coreUtils.readMontantSchemaComptables(
                userId,
                schema.getId(),
                montantSchemaComptables,
                paysSource.getCode(),
                deviseSource,
                companyId,
                transactionContext,
                map,
                report);

        BigDecimal tauxChange = new BigDecimal(
                String.valueOf(transactionContext.getContextItemValue(TransactionContextItem.TAUX_CHANGE)));
        report.setDevise(deviseSource.getCode());
        report.setDevisePayer(deviseCible.getCode());
        report.setRate(tauxChange);

        List<EcritureSchemaComptable> ecritureSchemaComptables =
                readEcritureSchemaBySchemaIdAndCodeEcritureCodeIn(companyTypeCode, schema.getId());
        if (ecritureSchemaComptables.isEmpty()) {
            throw new NotFoundException("Undefined writer accounting schema");
        }

        coreUtils.readEcritureSchemaComptables(ecritureSchemaComptables, report, map);

        if (serviceCode.equals("CASH_TRANSFER")) {
            MontantSchemaRecord request =
                    readMontantSchemaIdInEcritureBySchemaIdAndWriterCodeAndDirection(schema.getId());
            report.setMontantAPayer(map.get(request.montantSchemaComptableId()));
        }

        log.info(
                "demarrerTransaction end ok - report: frais: {} - montantApayer: {}",
                report.getFrais(),
                report.getMontantAPayer());
        log.trace(
                "demarrerTransaction end ok - report: frais: {}- montantApayer: {}",
                report.getFrais(),
                report.getMontantAPayer());

        BigDecimal principal = report.getMontant();
        BigDecimal commissions = report.getFrais().add(report.getTaxes()).add(report.getTimbre());
        report.setPrincipal(report.getMontant());
        report.setCommissions(commissions);
        report.setTotal(principal.add(commissions));

        return report;
    }
}
