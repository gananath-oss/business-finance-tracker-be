package com.business_finance_tracker.financetracker.dto.response;

import com.business_finance_tracker.financetracker.model.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CategoryResponse {
    private Long id;
    private  String name;
    private TransactionType type;
}
