package com.business_finance_tracker.financetracker.model;

public enum Role {
    OWNER,      // full access — registers the business
    MANAGER,    // sees all reports, sets targets
    STAFF       // enters daily transactions only
}
