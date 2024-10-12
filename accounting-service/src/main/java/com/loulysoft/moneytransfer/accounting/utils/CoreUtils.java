package com.loulysoft.moneytransfer.accounting.utils;

import com.loulysoft.moneytransfer.accounting.exceptions.NotFoundException;
import com.loulysoft.moneytransfer.accounting.mappers.AccountingMapper;
import com.loulysoft.moneytransfer.accounting.models.Journal;
import com.loulysoft.moneytransfer.accounting.models.TypeService;
import com.loulysoft.moneytransfer.accounting.parametering.utils.IParam;
import com.loulysoft.moneytransfer.accounting.parametering.utils.ParameterTypes;
import com.loulysoft.moneytransfer.accounting.repositories.TypeServiceRepository;
import com.loulysoft.moneytransfer.accounting.runtime.AbstractRuntimeService;
import java.text.MessageFormat;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
@SuppressWarnings("unchecked")
public class CoreUtils {

    private static final String CLASS_NAME_NOT_DEFINED = "Undefined class name with param code {0}";

    private final ApplicationContext context;

    private final AccountingMapper accountingMapper;

    private final TypeServiceRepository typeServiceRepository;

    public IParam getParamComponent(String typeParameter) {
        try {

            ParameterTypes parameterType = ParameterTypes.valueOf(typeParameter);

            Class<?> clazz = Class.forName(parameterType.getClassName());

            return (IParam) context.getBean(clazz);

        } catch (ClassNotFoundException | ClassCastException e) {

            throw new NotFoundException(MessageFormat.format(CLASS_NAME_NOT_DEFINED, typeParameter));
        }
    }

    public AbstractRuntimeService<Journal> getRuntimeService(String serviceCode) {
        try {
            TypeService service = accountingMapper.toTypeService(typeServiceRepository
                    .findByCode(serviceCode)
                    .orElseThrow(() -> new NotFoundException("Service with code " + serviceCode + " not found")));

            Class<?> clazz = Class.forName(service.getComposant());

            return (AbstractRuntimeService<Journal>) context.getBean(clazz);
        } catch (ClassNotFoundException | ClassCastException e) {
            // TODO Auto-generated catch block
            log.error(e.getMessage(), e);
            throw new NotFoundException("AbstractRuntimeService evaluation error");
        }
    }
}
