package com.loulysoft.moneytransfer.accounting.parametering.utils;

import com.loulysoft.moneytransfer.accounting.models.MontantContext;
import java.math.BigDecimal;

public interface IParam {

    public BigDecimal getValeurMontant(MontantContext context);
}
