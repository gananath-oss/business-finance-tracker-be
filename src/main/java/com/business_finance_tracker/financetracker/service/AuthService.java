package com.business_finance_tracker.financetracker.service;

import com.business_finance_tracker.financetracker.dto.request.LoginRequest;
import com.business_finance_tracker.financetracker.dto.request.RegisterBusinessRequest;
import com.business_finance_tracker.financetracker.dto.response.AuthResponse;
import com.business_finance_tracker.financetracker.model.Business;
import com.business_finance_tracker.financetracker.model.Role;
import com.business_finance_tracker.financetracker.model.User;
import com.business_finance_tracker.financetracker.repository.BusinessRepository;
import com.business_finance_tracker.financetracker.repository.UserRepository;
import com.business_finance_tracker.financetracker.security.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private final BusinessRepository  businessRepository;
    private final UserRepository      userRepository;
    private final PasswordEncoder     passwordEncoder;
    private final JwtService          jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthService(BusinessRepository businessRepository,
                       UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       JwtService jwtService,
                       AuthenticationManager authenticationManager) {
        this.businessRepository   = businessRepository;
        this.userRepository       = userRepository;
        this.passwordEncoder      = passwordEncoder;
        this.jwtService           = jwtService;
        this.authenticationManager = authenticationManager;
    }

    // ── Register a new business with an owner account ─────
    @Transactional  // both business and user saved together
    public AuthResponse register(RegisterBusinessRequest request) {

        // 1. Check business email not already registered
        if (businessRepository.existsByEmail(request.getBusinessEmail())) {
            throw new IllegalStateException(
                    "Business email already registered: "
                            + request.getBusinessEmail());
        }

        // 2. Check owner email not already registered
        if (userRepository.existsByEmail(request.getOwnerEmail())) {
            throw new IllegalStateException(
                    "Owner email already registered: "
                            + request.getOwnerEmail());
        }

        // 3. Create and save Business
        Business business = new Business();
        business.setName(request.getBusinessName());
        business.setIndustry(request.getIndustry());
        business.setEmail(request.getBusinessEmail());
        Business savedBusiness = businessRepository.save(business);

        // 4. Create and save Owner user
        User owner = new User();
        owner.setName(request.getOwnerName());
        owner.setEmail(request.getOwnerEmail());
        owner.setPassword(passwordEncoder.encode(request.getPassword()));
        owner.setRole(Role.OWNER);
        owner.setBusiness(savedBusiness);
        User savedOwner = userRepository.save(owner);

        // 5. Generate JWT token
        String token = jwtService.generateToken(savedOwner);

        // 6. Return response
        return new AuthResponse(
                token,
                savedOwner.getName(),
                savedOwner.getEmail(),
                savedBusiness.getName(),
                savedOwner.getRole().name()
        );
    }

    // ── Login with email and password ─────────────────────
    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest request) {

        // 1. Authenticate — throws exception if wrong credentials
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        // 2. Load user
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() ->
                        new IllegalStateException("User not found"));

        // 3. Generate token
        String token = jwtService.generateToken(user);

        // 4. Return response
        return new AuthResponse(
                token,
                user.getName(),
                user.getEmail(),
                user.getBusiness().getName(),
                user.getRole().name()
        );
    }
}