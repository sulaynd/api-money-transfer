package com.loulysoft.moneytransfer.accounting.runtime;

import com.loulysoft.moneytransfer.accounting.exceptions.TransactionException;
import com.loulysoft.moneytransfer.accounting.models.Journal;
import com.loulysoft.moneytransfer.accounting.models.ServiceContext;
import com.loulysoft.moneytransfer.accounting.models.TransactionContext;
import com.loulysoft.moneytransfer.accounting.models.TransactionReport;

public abstract class AbstractRuntimeService<J extends Journal> {

    public ServiceContext getServiceContext(
            Long companyId,
            Long companyRootId,
            String companyType,
            String service,
            TransactionContext transactionContext)
            throws TransactionException {
        return null;
    }

    public TransactionReport preRun(J journal, TransactionContext transactionContext) {
        return null;
    }

    //    public abstract void run(long transactionId) throws TransactionException;
    //
    //    public abstract void cancel(long transactionId) throws TransactionException;
    //
    //    public abstract void update(long transactionId, TransactionContext transactionContext) throws
    // TransactionException;
    //
    //    public abstract void updateClient(long transactionId, TransactionContext transactionContext)
    //            throws TransactionException;
    //
    //    public void notify(long transactionId) throws TransactionException {
    //
    //    }
    //
    public abstract Class<J> getJournalClass();

    //    public abstract TransactionReport preRun(DepotVersement journal, TransactionContext transactionContext)
    //                throws TransactionException;
    //
    //    public abstract TransactionReport exportReport(long transactionId);
}
