package com.api.shosetsuya.helpers.handlers;

import com.api.shosetsuya.helpers.ApiResponse;
import com.api.shosetsuya.models.entities.Users;
import com.api.shosetsuya.repositories.UserRepo;
import com.api.shosetsuya.services.CookieService;
import com.api.shosetsuya.services.JwtService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtService jwtService;
    private final UserRepo userRepo;
    private final MessageSource messageSource;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final CookieService cookieService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String email = oAuth2User.getAttribute("email");
        String username = oAuth2User.getAttribute("name");

        Users user = userRepo.findByEmail(email)
                .orElseGet(() -> createNewUser(email, username));

        String accessToken = jwtService.generateToken(user.getEmail(), false);
        String refreshToken = jwtService.generateToken(user.getEmail(), true);

        response.addHeader(HttpHeaders.SET_COOKIE,cookieService.createCookie(refreshToken).toString());

        ApiResponse<Map<String,String>> apiResponse = new ApiResponse<>(
                true,
                messageSource.getMessage("login.success", null, LocaleContextHolder.getLocale()),
                Map.of("accessToken", accessToken)
        );

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        objectMapper.writeValue(response.getWriter(), apiResponse);
    }

    private Users createNewUser(String email, String username) {
        Users newUser = new Users();
        newUser.setEmail(email);
        newUser.setUsername(username != null ? username : email);
        newUser.setProvider("goggle");
        return userRepo.save(newUser);
    }
}
