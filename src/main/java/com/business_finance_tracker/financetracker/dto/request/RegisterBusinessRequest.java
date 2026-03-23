package com.business_finance_tracker.financetracker.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor@AllArgsConstructor
public class RegisterBusinessRequest {
    @NotBlank(message = "Business name is required")
    @Size(min = 2, max = 100, message = "Business name must be between 2 and 100 characters")
    private String businessName;

    @NotBlank(message = "Industry is request")
    private String industry;

    @NotBlank(message = "Business email is required")
    @Email(message = "Must be the valid email address")
    private String businessEmail;

    @NotBlank(message = "Owner name is required")
    private String ownerName;

    @NotBlank(message = "Owner email is required")
    @Email(message = "Must be the valid email address")
    private String ownerEmail;

    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters")
    private String password;
}
