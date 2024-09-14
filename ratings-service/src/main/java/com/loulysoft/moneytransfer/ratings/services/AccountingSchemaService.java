package com.loulysoft.moneytransfer.ratings.services;

import com.loulysoft.moneytransfer.ratings.models.EcritureSchemaComptable;
import com.loulysoft.moneytransfer.ratings.models.MontantParamSchemaComptable;
import com.loulysoft.moneytransfer.ratings.models.MontantSchemaComptable;
import com.loulysoft.moneytransfer.ratings.models.SchemaComptable;
import com.loulysoft.moneytransfer.ratings.models.Users;
import java.util.List;

public interface AccountingSchemaService {

    SchemaComptable readSchemaByServiceCodeAndTypeCodeAndVariantAndStatus(Users user, String serviceCode);

    MontantSchemaComptable readMontantSchemaBySchemaId(Long schemaId);

    List<MontantParamSchemaComptable> readMontantParamSchemaByMontantSchemaIdAndParametreRechercheType(
            Long montantSchemaId);

    List<EcritureSchemaComptable> readEcritureSchemaBySchemaIdAndCodeEcritureCodeIn(Users user, Long schemaId);
}
