package com.loulysoft.moneytransfer.accounting.enums;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Currency;
import java.util.Locale;

public class CurrencyFormatterUtil {

    private final String currency;
    private final Locale locale;

    public CurrencyFormatterUtil(String currency) {
        this.currency = currency;
        this.locale = getLocale(currency);
    }

    public String currencyFormatter(BigDecimal number) {
        String result = null;
        if (number != null) {
            NumberFormat formatter = NumberFormat.getCurrencyInstance(locale);
            formatter.setCurrency(Currency.getInstance(currency));
            result = formatter.format(number);
        }
        return result;
    }

    private Locale getLocale(String currency) {
        for (Locale availableLocale : NumberFormat.getAvailableLocales()) {
            String code = NumberFormat.getCurrencyInstance(availableLocale)
                    .getCurrency()
                    .getCurrencyCode();
            if (currency.equals(code)) {
                return availableLocale;
            }
        }
        return null;
    }
}
