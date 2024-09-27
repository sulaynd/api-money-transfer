package com.loulysoft.moneytransfer.accounting.services;

import com.loulysoft.moneytransfer.accounting.models.Devise;
import com.loulysoft.moneytransfer.accounting.models.Pays;

public interface DeviseService {
    Devise readDeviseByCode(String code);

    Pays readPaysByCode(String code);
}
