package com.loulysoft.moneytransfer.accounting.services;

import com.loulysoft.moneytransfer.accounting.enums.Variant;
import com.loulysoft.moneytransfer.accounting.models.Devise;
import com.loulysoft.moneytransfer.accounting.models.EcritureSchemaComptable;
import com.loulysoft.moneytransfer.accounting.models.MontantParamSchemaComptable;
import com.loulysoft.moneytransfer.accounting.models.MontantSchemaComptable;
import com.loulysoft.moneytransfer.accounting.models.MontantSchemaRecord;
import com.loulysoft.moneytransfer.accounting.models.Pays;
import com.loulysoft.moneytransfer.accounting.models.SchemaComptable;
import com.loulysoft.moneytransfer.accounting.models.TransactionContext;
import com.loulysoft.moneytransfer.accounting.models.TransactionReport;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public interface AccountingSchemaService {

    SchemaComptable readSchemaByServiceCodeAndTypeCodeAndVariantAndStatus(
            String uniteOrganisationalTypeCode, String serviceCode, Variant variant);

    List<MontantSchemaComptable> readMontantSchemasBySchemaId(Long schemaId);

    List<MontantParamSchemaComptable> readMontantParamSchemaByMontantSchemaIdAndParametreRechercheType(
            Long montantSchemaId);

    List<EcritureSchemaComptable> readEcritureSchemaBySchemaIdAndCodeEcritureCodeIn(
            String uniteOrganisationalTypeCode, Long schemaId);

    EcritureSchemaComptable readEcritureSchemaBySchemaIdAndWriterCodeAndDirection(Long schemaId);

    MontantSchemaRecord readMontantSchemaIdInEcritureBySchemaIdAndWriterCodeAndDirection(Long schemaId);

    void readMontantSchemaComptables(
            Long userId,
            Long schemaComptableId,
            List<MontantSchemaComptable> montantSchemaComptables,
            String paysSource,
            Devise deviseSource,
            Long companyId,
            TransactionContext transactionContext,
            HashMap<Long, BigDecimal> map,
            TransactionReport report);

    void readEcritureSchemaComptables(
            List<EcritureSchemaComptable> ecritureSchemaComptables,
            TransactionReport report,
            HashMap<Long, BigDecimal> map);

    TransactionReport demarrerTransaction(
            Long userId, Long companyId, Pays paysPayer, String serviceCode, TransactionContext transactionContext);
}
