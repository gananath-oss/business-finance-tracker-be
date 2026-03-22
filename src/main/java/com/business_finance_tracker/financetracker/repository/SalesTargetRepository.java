package com.business_finance_tracker.financetracker.repository;

import com.business_finance_tracker.financetracker.model.SalesTarget;
import com.business_finance_tracker.financetracker.model.TargetPeriod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface SalesTargetRepository extends JpaRepository<SalesTarget, Long> {

    // All targets for a business
    List<SalesTarget> findByBusinessId(Long businessId);

    // Monthly targets for a specific month and year
    List<SalesTarget> findByBusinessIdAndPeriodAndTargetMonthAndTargetYear(
            Long businessId,
            TargetPeriod period,
            Integer targetMonth,
            Integer targetYear
    );

    // Daily target for a specific date
    Optional<SalesTarget> findByBusinessIdAndPeriodAndTargetDayAndTargetMonthAndTargetYear(
            Long businessId,
            TargetPeriod period,
            Integer targetDay,
            Integer targetMonth,
            Integer targetYear
    );
}