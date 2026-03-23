package com.business_finance_tracker.financetracker.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {
    private String token;
    private String ownerName;
    private String ownerEmail;
    private String businessName;
    private String role;
}
