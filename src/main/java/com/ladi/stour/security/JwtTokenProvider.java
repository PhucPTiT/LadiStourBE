package com.ladi.stour.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtTokenProvider {

    @Value("${app.jwt.secret:}")
    private String jwtSecretString;

    @Value("${app.jwt.expiration:86400000}")
    private long jwtExpirationMs; // Default: 24 hours (86400000 ms)

    private SecretKey jwtSecret;

    /**
     * Initialize JWT secret key
     */
    private SecretKey getSecretKey() {
        if (jwtSecret != null) {
            return jwtSecret;
        }

        // If custom secret is provided, try to use it
        if (jwtSecretString != null && !jwtSecretString.isEmpty()) {
            try {
                // Try to decode if it's base64 encoded
                byte[] decodedKey = Base64.getDecoder().decode(jwtSecretString);
                // Check if decoded key is at least 64 bytes (512 bits)
                if (decodedKey.length >= 64) {
                    jwtSecret = Keys.hmacShaKeyFor(decodedKey);
                } else {
                    // If decoded key is too short, pad it
                    byte[] paddedKey = new byte[64];
                    System.arraycopy(decodedKey, 0, paddedKey, 0, decodedKey.length);
                    jwtSecret = Keys.hmacShaKeyFor(paddedKey);
                }
            } catch (IllegalArgumentException e) {
                // If not valid base64, treat as plain string
                byte[] keyBytes = jwtSecretString.getBytes();
                if (keyBytes.length >= 64) {
                    jwtSecret = Keys.hmacShaKeyFor(keyBytes);
                } else {
                    // Pad string if too short
                    String padded = jwtSecretString;
                    while (padded.length() < 64) {
                        padded = padded + jwtSecretString;
                    }
                    jwtSecret = Keys.hmacShaKeyFor(padded.substring(0, 64).getBytes());
                }
            }
        } else {
            // Generate a secure key automatically if none provided
            jwtSecret = Keys.secretKeyFor(SignatureAlgorithm.HS512);
        }

        return jwtSecret;
    }

    /**
     * Generate JWT token for user
     * @param userId User ID
     * @param username Username
     * @return JWT token
     */
    public String generateToken(String userId, String username) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationMs);

        SecretKey key = getSecretKey();

        return Jwts.builder()
                .subject(userId)
                .claim("username", username)
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

    /**
     * Get user ID from JWT token
     * @param token JWT token
     * @return User ID
     */
    public String getUserIdFromToken(String token) {
        SecretKey key = getSecretKey();
        Claims claims = Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return claims.getSubject();
    }

    /**
     * Get username from JWT token
     * @param token JWT token
     * @return Username
     */
    public String getUsernameFromToken(String token) {
        SecretKey key = getSecretKey();
        Claims claims = Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return (String) claims.get("username");
    }

    /**
     * Validate JWT token
     * @param token JWT token
     * @return true if token is valid, false otherwise
     */
    public boolean validateToken(String token) {
        try {
            SecretKey key = getSecretKey();
            Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Check if token is expired
     * @param token JWT token
     * @return true if token is expired, false otherwise
     */
    public boolean isTokenExpired(String token) {
        try {
            SecretKey key = getSecretKey();
            Claims claims = Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
            return claims.getExpiration().before(new Date());
        } catch (Exception e) {
            return true;
        }
    }
}
