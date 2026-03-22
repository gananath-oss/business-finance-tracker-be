package com.business_finance_tracker.financetracker.repository;

import com.business_finance_tracker.financetracker.model.Business;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BusinessRepository extends JpaRepository<Business, Long> {
    // Spring generates: SELECT * FROM businesses WHERE email = ?
    Optional<Business> findByEmail(String email);

    // Spring generates: SELECT COUNT(*) FROM businesses WHERE email = ?
    boolean existsByEmail(String email);
}
