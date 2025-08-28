package com.api.shosetsuya.controllers;

import com.api.shosetsuya.helpers.ApiResponse;
import com.api.shosetsuya.models.dtos.auth.LoginDTO;
import com.api.shosetsuya.models.dtos.auth.RegisterDTO;
import com.api.shosetsuya.models.entities.Users;
import com.api.shosetsuya.services.CookieService;
import com.api.shosetsuya.services.JwtService;
import com.api.shosetsuya.services.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.logging.Logger;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
@Tag(name = "Authentication")
@Slf4j
public class AuthController {

    private final UserService userService;
    private final MessageSource messageSource;
    private final CookieService cookieService;
    private final JwtService jwtService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<?>> register(@Valid @RequestBody RegisterDTO dto) {
        userService.register(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(true, messageSource.getMessage("register.success", null, LocaleContextHolder.getLocale()), null));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<?>> login(@Valid @RequestBody LoginDTO dto) {
        Map<String, String> tokens = userService.login(dto);
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookieService.createCookie(tokens.get("refreshToken")).toString())
                .body(new ApiResponse<>(
                        true,
                        messageSource.getMessage(
                            "login.success",
                            null,
                            LocaleContextHolder.getLocale()
                        ),
                        Map.of("accessToken", tokens.get("accessToken"))
                    )
                );
    }

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<?>> refresh(HttpServletRequest request) {
        String refreshToken = cookieService.extractTokenFromCookie(request);
        if(!jwtService.validateRefreshToken(refreshToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .header(HttpHeaders.SET_COOKIE, cookieService.clearCookie().toString())
                    .body(new ApiResponse<>(
                            false,
                            messageSource.getMessage(
                                "error.token.refresh.invalid",
                                null,
                                LocaleContextHolder.getLocale()
                            ),
                            null
                        )
                    );
        }

        String email = jwtService.extractEmailFromRefreshToken(refreshToken);

        String newAccessToken = jwtService.generateToken(email, false);
        String newRefreshToken = jwtService.isNearExpiry(refreshToken) ?
                jwtService.generateToken(email, true) : null;

        ResponseEntity.BodyBuilder responseBuilder = ResponseEntity.ok();

        if(newRefreshToken != null) {
            responseBuilder.header(HttpHeaders.SET_COOKIE, cookieService.createCookie(newRefreshToken).toString());
        }

        return responseBuilder
                .body(new ApiResponse<>(
                    true,
                    messageSource.getMessage(
                        "token.refresh.success",
                        null,
                        LocaleContextHolder.getLocale()
                    ),
                    Map.of("accessToken", newAccessToken)
                )
        );
    }

}
