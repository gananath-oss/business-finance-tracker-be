package com.business_finance_tracker.financetracker.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MonthlyReportResponse {
    private int month;
    private int year;
    private BigDecimal totalIncome;
    private BigDecimal totalExpense;
    private BigDecimal netAmount;
    private BigDecimal monthlyTarget;
    private BigDecimal targetGap;
    private boolean targetAchieved;

    // Comparison data
    private BigDecimal lastMonthIncome;
    private BigDecimal incomeGrowth;        // % change vs last month
    private BigDecimal sameMonthLastYearIncome;
    private BigDecimal yearOnYearGrowth;    // % change vs same moth last year
}
