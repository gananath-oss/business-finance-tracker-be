package com.business_finance_tracker.financetracker.utils;

import com.business_finance_tracker.financetracker.model.User;
import com.business_finance_tracker.financetracker.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

// Every service uses this to get the currently logged-in user
// This is DIP in action — services depend on this abstraction
// not on Spring Security directly

@Component
public class SecurityUtils {

    private final UserRepository userRepository;

    public SecurityUtils(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Get currently authenticated user
    public User getCurrentUser() {
        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        return userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UsernameNotFoundException(
                                "Authenticated user not found: " + email));
    }

    // Get current user's business ID
    // Used in every service to scope queries
    public Long getCurrentBusinessId() {
        return getCurrentUser().getBusiness().getId();
    }
}