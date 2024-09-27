package com.loulysoft.moneytransfer.accounting.models;

import com.loulysoft.moneytransfer.accounting.enums.ServiceContextItem;
import java.util.HashMap;
import lombok.Builder;
import lombok.Data;

/**
 * Class allowing to define context of a transaction. <br>
 * <br>
 * Classe permettant de d�finir le contexte d'une transaction.
 *
 */
@Data
@Builder
public class ServiceContext {

    private HashMap<String, Object> serviceContext;

    /**
     *
     */
    public ServiceContext() {

        this.serviceContext = new HashMap<String, Object>();
    }

    public ServiceContext(HashMap<String, Object> param) {

        this.serviceContext = param;
    }

    /**
     * Method allowing to add stocks in transaction. <br>
     * <br>
     * M�thode permettant d'ajouter des valeurs dans la transaction.
     *
     * @param valueTransactionContext
     */
    public void addTransactionContext(ServiceContextItem key, Object valueTransactionContext) {

        this.serviceContext.put(key.name(), valueTransactionContext);
    }

    /**
     * Method allowing to recover the parameters of transaction. <br>
     * <br>
     * M�thode permettant de r�cup�rer les param�tres de la transaction.
     *
     * @return transactionContext
     */
    public HashMap<String, Object> getServiceContext() {

        return this.serviceContext;
    }
}
