package com.business_finance_tracker.financetracker.dto.request;

import com.business_finance_tracker.financetracker.model.Category;
import com.business_finance_tracker.financetracker.model.TargetPeriod;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SalesTargetRequest {
    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.01", message = "Amount must be grater than zero")
    private BigDecimal targetAmount;

    @NotNull(message = "Target period is required")
    private TargetPeriod period;

    @Min(value = 2020, message = "Year must be 2020 or later")
    @Max(value = 2100, message = "Year must be realistic")
    @NotNull(message = "Year is required")
    private Integer targetYear;

    @Min(value = 1, message = "Month must be between 1 and 12")
    @Max(value = 12, message = "Mont must be between 1 and 12")
    @NotNull(message = "Month is required")
    private Integer targetMonth;

    @Min(value = 1, message = "Day mus be between 1 and 31")
    @Max(value = 31, message = "Day must be between 1 and 31")
    private Integer targetDay;  // null for monthly target

    private Long categoryId;    // null means target apply for all categories
}
