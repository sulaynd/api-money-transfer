package com.loulysoft.moneytransfer.accounting.models;

import com.loulysoft.moneytransfer.accounting.enums.TransactionContextItem;
import java.util.HashMap;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionContext {
    private Long transactionId;
    private HashMap<TransactionContextItem, Object> context = new HashMap<>();

    public void addContextItem(TransactionContextItem item, Object value) {
        context.put(item, value);
    }

    public Object getContextItemValue(TransactionContextItem item) {
        return context.get(item);
    }
}
