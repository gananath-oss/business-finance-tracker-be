package com.business_finance_tracker.financetracker.repository;

import com.business_finance_tracker.financetracker.model.Category;
import com.business_finance_tracker.financetracker.model.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    // All categories for a business
    List<Category> findByBusinessId(Long businessId);

    // Categories filtered by type (INCOME or EXPENSE)
    List<Category> findByBusinessIdAndType(Long businessId, TransactionType type);

    // Check if category name already exists in this business
    boolean existsByNameAndBusinessId(String name, Long businessId);

    // Find by name and business (for validation)
    Optional<Category> findByNameAndBusinessId(String name, Long businessId);
}
