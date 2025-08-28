package com.api.shosetsuya.services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String accessSecretKey;

    @Value("${jwt.refresh-secret}")
    private String refreshSecretKey;

    public String generateToken(String email, boolean isRefreshToken) {
        String keyType = isRefreshToken ? refreshSecretKey : accessSecretKey;

        Date expiration = isRefreshToken ?
                new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 7) : // 7 days
                new Date(System.currentTimeMillis() + 1000 * 60 * 60); // 1 hour

        Map<String, Object> claims = new HashMap<>();

        JwtBuilder jwtBuilder = Jwts.builder()
                .subject(email)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(expiration);

        if(!isRefreshToken) jwtBuilder.claims(claims);

        return jwtBuilder
                .signWith(getSigningKey(keyType))
                .compact();
    }

    private SecretKey getSigningKey(String keyType) {
        byte[] keyBytes = Base64.getDecoder().decode(keyType);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String extractEmailFromAccessToken(String token) {
        return extractClaim(token, Claims::getSubject, getSigningKey(accessSecretKey));
    }

    public String extractEmailFromRefreshToken(String token) {
        return extractClaim(token, Claims::getSubject, getSigningKey(refreshSecretKey));
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimResolver, SecretKey key) {
        final Claims claims = extractAllClaims(token, key);
        return claimResolver.apply(claims);
    }

    private Claims extractAllClaims(String token, SecretKey key) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private boolean isTokenExpired(String token, SecretKey key) {
        return extractExpiration(token, key).before(new Date());
    }

    private Date extractExpiration(String token, SecretKey key) {
        return extractClaim(token, Claims::getExpiration, key);
    }

    public boolean validateAccessToken(String token, UserDetails userDetails) {
        final String userName = extractEmailFromAccessToken(token);
        return (userName.equals(userDetails.getUsername()) &&
                !isTokenExpired(token, getSigningKey(accessSecretKey))
        );
    }

    public boolean validateRefreshToken(String token) {
        return !isTokenExpired(token, getSigningKey(refreshSecretKey));
    }

    public boolean isNearExpiry(String refreshToken) {
        Date expiration = extractExpiration(refreshToken, getSigningKey(refreshSecretKey));
        long timeToExpiry = expiration.getTime() - System.currentTimeMillis();
        return timeToExpiry < 1000 * 60 * 60 * 24; // less than 1 day
    }
}
