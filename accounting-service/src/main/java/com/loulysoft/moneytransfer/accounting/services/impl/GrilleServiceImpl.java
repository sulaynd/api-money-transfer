package com.loulysoft.moneytransfer.accounting.services.impl;

import com.loulysoft.moneytransfer.accounting.exceptions.ResourceNotFoundException;
import com.loulysoft.moneytransfer.accounting.exceptions.TransactionException;
import com.loulysoft.moneytransfer.accounting.mappers.GrilleMapper;
import com.loulysoft.moneytransfer.accounting.models.GrilleItem;
import com.loulysoft.moneytransfer.accounting.repositories.GrilleItemRepository;
import com.loulysoft.moneytransfer.accounting.services.GrilleService;
import java.math.BigDecimal;
import java.math.MathContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class GrilleServiceImpl implements GrilleService {

    private final GrilleItemRepository grilleItemRepository;

    private final GrilleMapper grilleMapper;

    public GrilleServiceImpl(GrilleItemRepository grilleItemRepository, GrilleMapper grilleMapper) {
        this.grilleItemRepository = grilleItemRepository;
        this.grilleMapper = grilleMapper;
    }

    @Override
    public BigDecimal getGrilleValue(Long companyId, Long grilleId, BigDecimal montant) {
        // Long cmpId = user.getUniteOrganisational().getId();
        GrilleItem palier = grilleMapper.toGrilleItem(grilleItemRepository
                .findGrilleIdAndBorneInfLessThanEqualAndBorneSupGreaterThanEqualOrderByBorneInfAsc(grilleId, montant)
                .orElseThrow(() -> new ResourceNotFoundException("Grille with id " + grilleId + " not found")));

        BigDecimal marge = (palier.getMarge() != null ? palier.getMarge() : new BigDecimal(0));
        BigDecimal value = palier.getValue();
        //        UniteOrganisational compagnie = user.getUniteOrganisational();
        //        float uniteCash = compagnie.getPays().getDevise().getUniteMonetaire();
        if (palier.getPourcentage() == 'Y') {
            if (value.compareTo(new BigDecimal(1)) < 0) {
                return value.multiply(montant, MathContext.DECIMAL128).add(montant.multiply(marge));
            } else {
                throw new TransactionException("Parameter evaluation error");
            }
        } else {
            //            value.add(MathsUtils.round(montant.multiply(marge),uniteCash));
            return value.add(montant.multiply(marge));
        }
    }
}
