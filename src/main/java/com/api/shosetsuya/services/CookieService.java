package com.api.shosetsuya.services;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class CookieService {

    private static final String COOKIE_NAME = "refresh_token";
    private static final Duration EXPIRY = Duration.ofDays(7);

    public ResponseCookie createCookie(String token) {
        return ResponseCookie.from(COOKIE_NAME, token)
                .httpOnly(true)
//                .secure(true) -> for https only
                .path("/auth/refresh")
                .maxAge(EXPIRY)
                .sameSite("Strict")
                .build();
    }

    public String extractTokenFromCookie(HttpServletRequest request) {
        if(request.getCookies() == null) return null;
        for(Cookie cookie : request.getCookies()) {
            if(COOKIE_NAME.equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        return null;
    }

    public ResponseCookie clearCookie() {
        return ResponseCookie.from(COOKIE_NAME, "")
                .httpOnly(true)
//                .secure(true) -> for https only
                .path("/auth/refresh")
                .maxAge(0)
                .sameSite("Strict")
                .build();
    }
}
