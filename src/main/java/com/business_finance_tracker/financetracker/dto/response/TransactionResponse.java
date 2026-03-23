package com.business_finance_tracker.financetracker.dto.response;

import com.business_finance_tracker.financetracker.model.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TransactionResponse {
    private Long id;
    private BigDecimal amount;
    private TransactionType type;
    private String categoryName;
    private String note;
    private String createdBy;
    private LocalDateTime createdAt;
}
