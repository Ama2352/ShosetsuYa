package com.api.shosetsuya.controllers;

import com.api.shosetsuya.helpers.ApiResponse;
import com.api.shosetsuya.models.dtos.auth.LoginDTO;
import com.api.shosetsuya.models.dtos.auth.RegisterDTO;
import com.api.shosetsuya.services.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
@Tag(name = "Authentication")
public class AuthController {

    private final UserService userService;
    private final MessageSource messageSource;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<?>> register(@Valid @RequestBody RegisterDTO dto) {
        userService.register(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(true, messageSource.getMessage("register.success", null, LocaleContextHolder.getLocale()), null));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<?>> login(@Valid @RequestBody LoginDTO dto) {
        String token = userService.login(dto);
        return ResponseEntity.ok(new ApiResponse<>(
                true,
                messageSource.getMessage(
                        "login.success",
                        null,
                        LocaleContextHolder.getLocale()),
                        Map.of("token", token)
                )
        );
    }



}
