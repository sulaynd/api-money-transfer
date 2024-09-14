package com.loulysoft.moneytransfer.ratings.services;

import com.loulysoft.moneytransfer.ratings.models.EcritureSchemaComptable;
import com.loulysoft.moneytransfer.ratings.models.MontantParamSchemaComptable;
import com.loulysoft.moneytransfer.ratings.models.MontantSchemaComptable;
import com.loulysoft.moneytransfer.ratings.models.SchemaComptable;
import com.loulysoft.moneytransfer.ratings.utils.Code;
import com.loulysoft.moneytransfer.ratings.utils.Pivot;
import com.loulysoft.moneytransfer.ratings.utils.Type;
import java.util.List;

public interface AccountingSchemaService {

    SchemaComptable readSchemaByServiceCodeAndTypeCodeAndVariantAndStatus(
            String serviceCode, String type, String variant, String status);

    MontantSchemaComptable readMontantSchemaBySchemaComptableId(Long schemaComptableId);

    List<MontantParamSchemaComptable> readMontantParamSchemaByMontantSchemaIdAndParametreRechercheType(
            Long montantSchemaId, Type type);

    List<EcritureSchemaComptable> readEcritureSchemaBySchemaIdAndCodeEcritureCodeIn(
            Long schemaId, List<Pivot> pivots, Type type, Integer niveau, List<Code> codes);
}
