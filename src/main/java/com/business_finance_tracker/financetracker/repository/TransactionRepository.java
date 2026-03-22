package com.business_finance_tracker.financetracker.repository;

import com.business_finance_tracker.financetracker.model.Transaction;
import com.business_finance_tracker.financetracker.model.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    // All transactions for a business
    List<Transaction> findByBusinessId(Long businessId);

    // Transactions by date range — for monthly reports
    List<Transaction> findByBusinessIdAndTransactionDateBetween(
            Long businessId,
            LocalDate startDate,
            LocalDate endDate
    );

    // Transactions by type and date range
    List<Transaction> findByBusinessIdAndTypeAndTransactionDateBetween(
            Long businessId,
            TransactionType type,
            LocalDate startDate,
            LocalDate ensDate
    );

    // Total amount by type and date range — for reports
    // This is a JPQL query — more complex than method naming can handle
    @Query("SELECT COALESCE(SUM(t.amount), 0) FROM Transaction t " +
            "WHERE t.business.id = :businessId " +
            "AND t.type = :type " +
            "AND t.transactionDate BETWEEN :startDate AND :endDate")
    BigDecimal sumByBusinessIdAndTypeAndDateBetween(
            @Param("businessId") Long businessId,
            @Param("type") TransactionType type,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );
}
