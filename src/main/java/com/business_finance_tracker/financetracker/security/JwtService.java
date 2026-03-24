package com.business_finance_tracker.financetracker.security;

import com.business_finance_tracker.financetracker.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private Long expiration;

    // ── Generate token for a user ─────────────────────────
    public String generateToken(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("businessId", user.getBusiness().getId());
        claims.put("role",       user.getRole().name());
        claims.put("name",       user.getName());

        return Jwts.builder()
                .claims(claims)
                .subject(user.getEmail())       // who the token belongs to
                .issuedAt(new Date())            // when created
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSigningKey())       // sign with secret
                .compact();
    }

    // ── Extract email from token ──────────────────────────
    public String extractEmail(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // ── Extract businessId from token ─────────────────────
    public Long extractBusinessId(String token) {
        return extractClaim(token,
                claims -> claims.get("businessId", Long.class));
    }

    // ── Extract role from token ───────────────────────────
    public String extractRole(String token) {
        return extractClaim(token,
                claims -> claims.get("role", String.class));
    }

    // ── Check if token is valid ───────────────────────────
    public boolean isTokenValid(String token, String email) {
        final String tokenEmail = extractEmail(token);
        return tokenEmail.equals(email) && !isTokenExpired(token);
    }

    // ── Private helpers ───────────────────────────────────
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private <T> T extractClaim(String token,
                               Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private SecretKey getSigningKey() {
        byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}