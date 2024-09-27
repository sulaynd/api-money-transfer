package com.loulysoft.moneytransfer.accounting.parametering.utils;

import com.loulysoft.moneytransfer.accounting.exceptions.CalculatorException;
import com.loulysoft.moneytransfer.accounting.exceptions.NotFoundException;
import com.loulysoft.moneytransfer.accounting.exceptions.TransactionException;
import com.loulysoft.moneytransfer.accounting.mappers.AccountingMapper;
import com.loulysoft.moneytransfer.accounting.mappers.GrilleMapper;
import com.loulysoft.moneytransfer.accounting.models.CalculMontantSchemaComptable;
import com.loulysoft.moneytransfer.accounting.models.MontantContext;
import com.loulysoft.moneytransfer.accounting.repositories.CalculMontantSchemaComptableRepository;
import com.loulysoft.moneytransfer.accounting.repositories.GrilleItemRepository;
import com.loulysoft.moneytransfer.accounting.repositories.GrilleRepository;
import com.loulysoft.moneytransfer.accounting.services.OperationService;
import com.loulysoft.moneytransfer.accounting.utils.Calculator;
import java.math.BigDecimal;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CalculParam extends CoreParameterUtilities implements IParam {

    private final CalculMontantSchemaComptableRepository calculMontantSchemaRepository;

    private final AccountingMapper accountingMapper;
    private final OperationService operationService;

    protected CalculParam(
            GrilleRepository grilleRepository,
            GrilleItemRepository grilleItemRepository,
            GrilleMapper grilleMapper,
            CalculMontantSchemaComptableRepository calculMontantSchemaRepository,
            AccountingMapper accountingMapper,
            OperationService operationService) {
        super(grilleRepository, grilleItemRepository, grilleMapper);
        this.calculMontantSchemaRepository = calculMontantSchemaRepository;
        this.accountingMapper = accountingMapper;
        this.operationService = operationService;
    }

    @Override
    public BigDecimal getValeurMontant(MontantContext context) {

        Long montantSchemaId = context.getMontantSchemaId();
        Long transactionId = context.getTransactionContext().getTransactionId();
        List<CalculMontantSchemaComptable> montantFormules = accountingMapper.convertToCalculMontantSchema(
                calculMontantSchemaRepository.findByMontantSchemaIdOrderByRang(montantSchemaId));

        if (montantFormules.isEmpty()) {
            throw new NotFoundException("Calculation formula with schemaId " + montantSchemaId + " not found");
        }

        try {
            BigDecimal value = BigDecimal.ZERO;
            for (CalculMontantSchemaComptable montantFormule : montantFormules) {
                BigDecimal param = BigDecimal.ZERO;
                BigDecimal constanteParam = montantFormule.getConstanteParam();
                // MontantSchemaComptable montantSchema = montantFormule.getMontantSchema();
                var montantParam = montantFormule.getMontantParam();
                // Long montantParam = montantFormule.getMontantParam();
                if (constanteParam != null && montantParam != null) {
                    throw new NotFoundException("Parameter MontantSchemaComptable evaluation error");
                }

                if (constanteParam != null) {
                    param = constanteParam;
                } else if (montantParam != null) {
                    // logger.debug(montantParam.getId()+" "+montantFormule.getId()+" "+tempTransactionId);
                    // Operation__ operation__ = findOperation__(tempTransactionId, montantParam.getId());
                    var operation = operationService.findOperation(transactionId, montantParam.getId());
                    // Operation operation = operationService.findOperation(transactionId, montantParam);
                    param = operation.getMontant();
                }

                var operateurArith = montantFormule.getOperateurArithmetique();
                value = Calculator.calculate(operateurArith.getOperateur(), value, param);
            }

            return value;
        } catch (CalculatorException e) {
            // TODO Auto-generated catch block
            log.error(e.getMessage(), e);
            throw new TransactionException("CalculParam evaluation error");
        }
    }
}
