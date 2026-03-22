package com.business_finance_tracker.financetracker.repository;

import com.business_finance_tracker.financetracker.model.Role;
import com.business_finance_tracker.financetracker.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // Find user by email — used for login
    Optional<User> findByEmail(String email);

    // Check if email already registered
    boolean existsByEmail(String email);

    // Find all users in a business
    List<User> findByBusinessId(Long businessId);

    // Find all users in a business with a specific role
    List<User> findByBusinessIdAndRole(Long findByBusinessIdAndRole, Role role);
}
