package com.loulysoft.moneytransfer.accounting.parametering.utils;

import com.loulysoft.moneytransfer.accounting.enums.TransactionContextItem;
import com.loulysoft.moneytransfer.accounting.exceptions.TransactionException;
import com.loulysoft.moneytransfer.accounting.mappers.AccountingMapper;
import com.loulysoft.moneytransfer.accounting.mappers.GrilleMapper;
import com.loulysoft.moneytransfer.accounting.models.MontantContext;
import com.loulysoft.moneytransfer.accounting.models.MontantSchemaComptable;
import com.loulysoft.moneytransfer.accounting.models.ServiceContext;
import com.loulysoft.moneytransfer.accounting.repositories.GrilleItemRepository;
import com.loulysoft.moneytransfer.accounting.repositories.GrilleRepository;
import com.loulysoft.moneytransfer.accounting.repositories.MontantSchemaComptableRepository;
import java.math.BigDecimal;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ServiceContextParam extends CoreParameterUtilities implements IParam {

    private final MontantSchemaComptableRepository montantSchemaRepository;

    private final AccountingMapper accountingMapper;

    protected ServiceContextParam(
            GrilleRepository grilleRepository,
            GrilleItemRepository grilleItemRepository,
            GrilleMapper grilleMapper,
            MontantSchemaComptableRepository montantSchemaRepository,
            AccountingMapper accountingMapper) {
        super(grilleRepository, grilleItemRepository, grilleMapper);
        this.montantSchemaRepository = montantSchemaRepository;
        this.accountingMapper = accountingMapper;
    }

    @Override
    public BigDecimal getValeurMontant(MontantContext context) {
        Object result = '0';
        try {
            var transactionContext = context.getTransactionContext();
            var serviceContext =
                    (ServiceContext) transactionContext.getContextItemValue(TransactionContextItem.SERVICE_CONTEXT);
            if (serviceContext == null) {
                throw new TransactionException("ServiceContextParam parameter evaluation error");
            }
            Optional<MontantSchemaComptable> montantSchemaComptable = montantSchemaRepository
                    .findById(context.getMontantSchemaId())
                    .map(accountingMapper::toMontantSchema);

            if (montantSchemaComptable.isPresent()) {
                result = serviceContext
                        .getServiceContext()
                        .get(montantSchemaComptable.get().getParam().getCode());
            }

            return new BigDecimal(String.valueOf(result));
        } catch (Exception e) {
            // TODO Auto-generated catch block
            log.error(e.getMessage(), e);
            throw new TransactionException("ServiceContextParam parameter evaluation error");
        }
    }
}
