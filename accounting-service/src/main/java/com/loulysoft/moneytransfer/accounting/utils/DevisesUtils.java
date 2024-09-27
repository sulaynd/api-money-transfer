package com.loulysoft.moneytransfer.accounting.utils;

import com.loulysoft.moneytransfer.accounting.exceptions.ResourceNotFoundException;
import com.loulysoft.moneytransfer.accounting.exceptions.TransactionException;
import com.loulysoft.moneytransfer.accounting.mappers.DeviseMapper;
import com.loulysoft.moneytransfer.accounting.models.CoursDevise;
import com.loulysoft.moneytransfer.accounting.models.Devise;
import com.loulysoft.moneytransfer.accounting.repositories.CoursDeviseRepository;
import com.loulysoft.moneytransfer.accounting.services.DeviseService;
import java.math.BigDecimal;
import java.math.MathContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DevisesUtils {

    private final DeviseService deviseService;
    private final DeviseMapper deviseMapper;
    ;
    private final CoursDeviseRepository coursDeviseRepository;

    public BigDecimal getCoursDevisePratique(Long companyId, String deviseSource, String deviseCible) {
        if (deviseSource.equalsIgnoreCase(deviseCible)) return new BigDecimal(1);

        try {
            CoursDevise coursDevise = deviseMapper.toCoursDevise(coursDeviseRepository
                    .findCoursDeviseBySourceAndCibleAndCompanyId(deviseSource, deviseCible, companyId)
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Currency with source " + deviseSource + " and target " + deviseCible + " not found")));

            return coursDevise.getCoursPratique();
        } catch (Exception e) {
            log.info("CoursDevisePratique error - code:", e);
            throw new TransactionException("Currency conversion error");
        }
    }

    public BigDecimal roundUnit(BigDecimal value, String devise) {
        BigDecimal result = BigDecimal.ZERO;
        try {

            Devise _devise = deviseService.readDeviseByCode(devise);
            float multiple = _devise.getUniteComptable();
            // String m = new Float(multiple).toString();
            String m = Float.toString(multiple);
            BigDecimal temp = new BigDecimal(m);
            BigDecimal a = value.divide(temp, MathContext.DECIMAL64);
            BigDecimal b = BigDecimal.valueOf(a.longValue());
            if (a.subtract(b).compareTo(BigDecimal.valueOf(0.5)) < 0) result = b.multiply(temp);
            else result = b.add(BigDecimal.valueOf(1)).multiply(temp);
        } catch (RuntimeException e) {
            throw new TransactionException("roundUnit error");
        }
        return result;
    }

    public BigDecimal roundMonetaryUnit(BigDecimal value, String devise) {
        BigDecimal result = BigDecimal.ZERO;
        try {

            Devise _devise = deviseService.readDeviseByCode(devise);
            float multiple = _devise.getUniteMonetaire();
            // String m = new Float(multiple).toString();
            String m = Float.toString(multiple);
            BigDecimal temp = new BigDecimal(m);
            BigDecimal a = value.divide(temp, MathContext.DECIMAL64);
            BigDecimal b = BigDecimal.valueOf(a.longValue());
            if (a.subtract(b).compareTo(BigDecimal.valueOf(0.5)) < 0) result = b.multiply(temp);
            else result = b.add(BigDecimal.valueOf(1)).multiply(temp);
        } catch (RuntimeException e) {
            throw new TransactionException("roundMonetaryUnit error");
        }
        return result;
    }
}
