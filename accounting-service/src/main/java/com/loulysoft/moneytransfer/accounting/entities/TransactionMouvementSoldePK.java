package com.loulysoft.moneytransfer.accounting.entities;

import com.loulysoft.moneytransfer.accounting.enums.Criticite;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Embeddable
public class TransactionMouvementSoldePK {

    @Column(name = "TMS_TRANS_ID")
    private Long transactionId;

    @Enumerated(EnumType.STRING)
    @Column(name = "TMS_CRITICITE")
    private Criticite criticite;

    public TransactionMouvementSoldePK(Long transactionId, Criticite criticite) {
        super();
        this.transactionId = transactionId;
        this.criticite = criticite;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((criticite == null) ? 0 : criticite.hashCode());
        result = prime * result + ((transactionId == null) ? 0 : transactionId.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        TransactionMouvementSoldePK other = (TransactionMouvementSoldePK) obj;
        if (criticite != other.criticite) return false;
        if (transactionId == null) {
            return other.transactionId == null;
        } else return transactionId.equals(other.transactionId);
    }
}
