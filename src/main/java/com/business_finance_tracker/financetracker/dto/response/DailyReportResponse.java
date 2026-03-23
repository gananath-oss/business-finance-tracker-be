package com.business_finance_tracker.financetracker.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DailyReportResponse {
    private LocalDate date;
    private BigDecimal totalIncome;
    private BigDecimal totalExpense;
    private BigDecimal netAmount;       // Income | Expense
    private BigDecimal dailyTarget;     // null for no target set
    private BigDecimal targetGap;       // Income | target (negative = below target)
    private boolean targetAchieved;
}
