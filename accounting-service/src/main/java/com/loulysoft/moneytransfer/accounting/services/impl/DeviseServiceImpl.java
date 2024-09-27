package com.loulysoft.moneytransfer.accounting.services.impl;

import com.loulysoft.moneytransfer.accounting.exceptions.ResourceNotFoundException;
import com.loulysoft.moneytransfer.accounting.mappers.DeviseMapper;
import com.loulysoft.moneytransfer.accounting.models.Devise;
import com.loulysoft.moneytransfer.accounting.models.Pays;
import com.loulysoft.moneytransfer.accounting.repositories.DeviseRepository;
import com.loulysoft.moneytransfer.accounting.repositories.PaysRepository;
import com.loulysoft.moneytransfer.accounting.services.DeviseService;
import java.text.MessageFormat;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class DeviseServiceImpl implements DeviseService {
    private static final String DEVISE_NOT_FOUND_MESSAGE = "Devise not found with code: {0}";
    private static final String PAYS_NOT_FOUND_MESSAGE = "Country not found with code: {0}";
    private final DeviseMapper deviseMapper;
    private final DeviseRepository deviseRepository;
    private final PaysRepository paysRepository;

    @Override
    public Devise readDeviseByCode(String code) {
        /* Getting devise */
        Devise devise = deviseMapper.toDevise(deviseRepository
                .findByCode(code)
                .orElseThrow(
                        () -> new ResourceNotFoundException(MessageFormat.format(DEVISE_NOT_FOUND_MESSAGE, code))));

        log.info("readDeviseByCode end ok - code: {}", code);
        log.trace("readDeviseByCode end ok - code: {}", code);

        return devise;
    }

    @Override
    public Pays readPaysByCode(String code) {
        /* Getting pays */
        Pays pays = deviseMapper.toPays(paysRepository
                .findByCode(code)
                .orElseThrow(() -> new ResourceNotFoundException(MessageFormat.format(PAYS_NOT_FOUND_MESSAGE, code))));

        log.info("readPaysByCode end ok - code: {}", code);
        log.trace("readPaysByCode end ok - code: {}", code);

        return pays;
    }
}
