package com.loulysoft.moneytransfer.accounting.parametering.utils;

import com.loulysoft.moneytransfer.accounting.enums.TypeGrille;
import com.loulysoft.moneytransfer.accounting.exceptions.ResourceNotFoundException;
import com.loulysoft.moneytransfer.accounting.mappers.GrilleMapper;
import com.loulysoft.moneytransfer.accounting.models.Grille;
import com.loulysoft.moneytransfer.accounting.models.GrilleItem;
import com.loulysoft.moneytransfer.accounting.repositories.GrilleItemRepository;
import com.loulysoft.moneytransfer.accounting.repositories.GrilleRepository;
import java.math.BigDecimal;
import java.math.MathContext;
import org.springframework.stereotype.Component;

@Component
public abstract class CoreParameterUtilities {

    private final GrilleRepository grilleRepository;

    private final GrilleItemRepository grilleItemRepository;

    private final GrilleMapper grilleMapper;

    protected CoreParameterUtilities(
            GrilleRepository grilleRepository, GrilleItemRepository grilleItemRepository, GrilleMapper grilleMapper) {
        this.grilleRepository = grilleRepository;
        this.grilleItemRepository = grilleItemRepository;
        this.grilleMapper = grilleMapper;
    }

    public BigDecimal getValueInGrille(Long grilleId, BigDecimal base) {

        BigDecimal value = null;

        GrilleItem grilleItem = grilleMapper.toGrilleItem(grilleItemRepository
                .findGrilleIdAndBorneInfLessThanEqualAndBorneSupGreaterThanEqualOrderByBorneInfAsc(grilleId, base)
                .orElseThrow(() -> new ResourceNotFoundException("Grille with id " + grilleId + " not found")));

        if (grilleItem != null) {
            if (grilleItem.getPourcentage().toString().equalsIgnoreCase("y")
                    || grilleItem.getPourcentage().toString().equalsIgnoreCase("o")) {
                value = (grilleItem.getValue().divide(new BigDecimal(100), MathContext.DECIMAL64)).multiply(base);
            } else {
                value = grilleItem.getValue();
            }
        }

        return value;
    }

    public BigDecimal getValeurParametre(long grilleId, BigDecimal montantDeBase) {

        Grille grille = grilleMapper.toGrille(grilleRepository
                .findById(grilleId)
                .orElseThrow(() -> new ResourceNotFoundException("Grille with id " + grilleId + " not found")));

        var type = grille.getType();
        if (type.compareTo(TypeGrille.GRILLE) == 0) {
            return getValueInGrille(grille.getId(), montantDeBase);
        }
        if (type.compareTo(TypeGrille.SIMPLE) == 0) {
            return new BigDecimal(grille.getValue());
        }
        return (new BigDecimal(grille.getValue()).divide(new BigDecimal(100), MathContext.DECIMAL64))
                .multiply(montantDeBase);
    }
}
