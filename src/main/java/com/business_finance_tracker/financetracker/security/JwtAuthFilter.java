package com.business_finance_tracker.financetracker.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final CustomUserDetailsService userDetailsService;

    public JwtAuthFilter(JwtService jwtService,
                         CustomUserDetailsService userDetailsService) {
        this.jwtService         = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        // 1. Get Authorization header
        final String authHeader = request.getHeader("Authorization");

        // 2. If no token — skip this filter, continue chain
        //    Spring Security will block protected endpoints
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 3. Extract token (remove "Bearer " prefix)
        final String token = authHeader.substring(7);

        // 4. Extract email from token
        final String email = jwtService.extractEmail(token);

        // 5. If email found and user not already authenticated
        if (email != null &&
                SecurityContextHolder.getContext()
                        .getAuthentication() == null) {

            // 6. Load user from database
            UserDetails userDetails =
                    userDetailsService.loadUserByUsername(email);

            // 7. Validate token
            if (jwtService.isTokenValid(token, email)) {

                // 8. Create authentication token
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );

                authToken.setDetails(
                        new WebAuthenticationDetailsSource()
                                .buildDetails(request));

                // 9. Set in SecurityContext — user is now authenticated
                SecurityContextHolder.getContext()
                        .setAuthentication(authToken);
            }
        }

        // 10. Continue the filter chain
        filterChain.doFilter(request, response);
    }
}