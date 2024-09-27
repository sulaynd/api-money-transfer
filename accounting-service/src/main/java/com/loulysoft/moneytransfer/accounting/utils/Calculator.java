package com.loulysoft.moneytransfer.accounting.utils;

import com.loulysoft.moneytransfer.accounting.enums.Operateur;
import com.loulysoft.moneytransfer.accounting.exceptions.CalculatorException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class Calculator {

    public static BigDecimal calculate(Operateur operateur, BigDecimal amount, BigDecimal param) {
        // 2, RoundingMode.HALF_EVEN)
        try {
            switch (operateur) {
                case SOMME:
                    amount = amount.add(param);
                    break;
                case SOUSTRACTION:
                    amount = amount.subtract(param);
                    break;
                case MULTIPLICATION:
                    amount = amount.multiply(param);
                    break;
                case DIVISION:
                    amount = amount.divide(param, 5, RoundingMode.CEILING);
                    break;
                case INVERSE:
                    amount = BigDecimal.ONE.divide(amount, 5, RoundingMode.CEILING);
                    break;

                default:
                    throw new CalculatorException("Calculator error");
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            log.error(e.getMessage(), e);
            throw new CalculatorException("Calculator error");
        }

        return amount;
    }
}
