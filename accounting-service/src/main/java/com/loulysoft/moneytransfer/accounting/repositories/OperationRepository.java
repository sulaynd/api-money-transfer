package com.loulysoft.moneytransfer.accounting.repositories;

import com.loulysoft.moneytransfer.accounting.entities.OperationEntity;
import com.loulysoft.moneytransfer.accounting.enums.Category;
import com.loulysoft.moneytransfer.accounting.enums.Code;
import com.loulysoft.moneytransfer.accounting.enums.Criticite;
import com.loulysoft.moneytransfer.accounting.enums.DebitCredit;
import com.loulysoft.moneytransfer.accounting.enums.Niveau;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface OperationRepository extends JpaRepository<OperationEntity, Long> {

    @Query(
            """
           SELECT o FROM OperationEntity o
           JOIN o.ecritureSchemaComptable esc
           JOIN o.compte c
           WHERE esc.writer.code = :code
           AND esc.direction = :direction
           AND o.transaction.id = :transactionId
           AND c.typeCompte.code = :typeCompte
        """)
    Optional<OperationEntity> findOperationByCodeAndDirectionAndTransactionIdAndTypeCompte(
            Code code, DebitCredit direction, Long transactionId, String typeCompte);

    @Query(
            """
            SELECT o
            FROM OperationEntity o
            JOIN o.compte c
            JOIN c.typeCompte tc
            WHERE o.transaction.id = :transactionId
            AND tc.category = :category
            ORDER BY o.id
        """)
    List<OperationEntity> findOperationsByTransactionIdAndCategoryOrderById(Long transactionId, Category category);

    @Query(
            """
           SELECT SUM(o.amount)
           FROM OperationEntity o
           JOIN o.transaction t
           WHERE o.compte.id = :compteId
           AND t.status = :status
        """)
    List<BigDecimal> getTotalByCompteIdAndTransactionStatus(Long compteId, Character status);

    @Query(
            """
           SELECT SUM(o.amount)
           FROM OperationEntity o
           JOIN o.transaction t
           JOIN t.transactionMouvementSoldes tms
           WHERE o.compte.id = :compteId
           AND t.status in (:status)
           AND tms.status in (:tmsStatus)
           AND tms.id.criticite = :criticite
        """)
    List<BigDecimal> getTotalByCompteIdAndTransactionStatusInAndTmsStatusInAndCriticite(
            Long compteId, List<Character> status, List<Character> tmsStatus, Criticite criticite);

    @Query(
            """
           SELECT SUM(o.amount)
           FROM JournalEntity j, OperationEntity o
           JOIN o.transaction t
           WHERE t.id = j.transaction.id
           AND o.compte.id = :compteId
           AND t.status in (:status)
           AND j.typeOperation = :typeOperation
        """)
    BigDecimal getTotalByCompteIdAndTransactionStatusInAndTypeOperation(
            Long compteId, List<Character> status, DebitCredit typeOperation);

    @Query(
            """
           SELECT SUM(o.amount)
           FROM OperationEntity o
           JOIN o.ecritureSchemaComptable esc
           JOIN o.transaction t
           WHERE esc.writer.code in (:codes)
           AND t.status in (:status)
           AND t.createdAt >= :createdAt
           AND o.compte.id = :compteId
        """)
    List<BigDecimal> getTotalByEcritureSchemaComptableCodesInAndTransactionStatusInAndTransactionCreatedAtAndCompteId(
            List<Code> codes, List<Character> status, LocalDateTime createdAt, Long compteId);

    @Query(
            """
           SELECT DISTINCT t.niveau
           FROM OperationEntity o
           JOIN o.compte c
           JOIN c.owner uo
           JOIN uo.type t
           WHERE o.transaction.id = :transactionId
           AND o.amount <> 0
        """)
    List<Niveau> getNiveauByTransactionId(Long transactionId);

    @Query(
            """
            SELECT o FROM OperationEntity o
            JOIN o.compte c
            JOIN c.typeCompte tc
            JOIN c.owner uo
            JOIN uo.type t
            WHERE o.transaction.id = :transactionId
            AND t.niveau in (:niveaux)
            AND tc.category in (:categories)
            ORDER BY o.id
        """)
    List<OperationEntity> findOperationsByTransactionIdAndNiveauInAndCategoryInOrderById(
            Long transactionId, List<Niveau> niveaux, List<Category> categories);
}
